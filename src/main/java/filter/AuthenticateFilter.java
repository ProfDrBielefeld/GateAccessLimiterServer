package filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import configuration.Secret;
import io.dropwizard.hibernate.UnitOfWork;
import model.Key;
import model.Tempkey;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import persistence.PermkeyDAO;
import persistence.TempkeyDAO;

@Authenticator
public class AuthenticateFilter implements ContainerRequestFilter {

    //private static final String API_SECRET;
    private static final String PARAM_API_KEY = "apiKey";
    private static final String PARAM_TOKEN = "token";
    private static final long SECONDS_IN_MILLISECOND = 1000L;
    private static final int TTL_SECONDS = 10;
    private PermkeyDAO PermDAO;
    private TempkeyDAO TempDAO;
    private Secret secret;



    public AuthenticateFilter(PermkeyDAO PermDAO, TempkeyDAO TempDAO ,Secret secret)
    {
        this.PermDAO = PermDAO;
        this.TempDAO = TempDAO;
        this.secret = secret;
    }

    @Override
    @UnitOfWork
    public void filter(ContainerRequestContext context) throws IOException {
        final String apiKey = extractParam(context, PARAM_API_KEY);
        if (StringUtils.isEmpty(apiKey)) {
            context.abortWith(responseMissingParameter(PARAM_API_KEY));
        }

        final String token = extractParam(context, PARAM_TOKEN);
        if (StringUtils.isEmpty(token)) {
            context.abortWith(responseMissingParameter(PARAM_TOKEN));
        }

        if (!authenticate(apiKey, token)) {
            context.abortWith(responseUnauthorized());
        }
    }

    private String extractParam(ContainerRequestContext context, String param) {
        final UriInfo uriInfo = context.getUriInfo();
        final List user = uriInfo.getQueryParameters().get(param);
        return CollectionUtils.isEmpty(user) ? null : String.valueOf(user.get(0));
    }

    private Response responseMissingParameter(String name) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity("Parameter '" + name + "' is required.")
                .build();
    }

    private Response responseUnauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("Unauthorized")
                .build();
    }

    @UnitOfWork
    private boolean authenticate(String apiKey, String token) {
        final String secretKey = secret.getSecretKey();

        //Checks for API Keys in DB
        Key foundkey = PermDAO.findKey(apiKey);

        if(foundkey == null) // Wenn kein Permanenter Key
        {
            foundkey = TempDAO.findKey(apiKey); // Dann Prüfen ob evt Temp key
            if(foundkey == null) // auch nicht gefunden -> Nicht berechtigt
            {
                throw new WebApplicationException("Ungültiger Schlüssel",420); //Fehler für Fehlerhaften Schlüssel

            }
            else // Wenn es sich um einen temp key handelt, prüfe ob Zeitspanne ok ist
            {
                //Cast zum Tempkey um an die Daten zu kommen
                //Benötigt kein try catch, da es sich hier nur noch um einen Tempkey handeln kann
                Tempkey tfoundkey = (Tempkey) foundkey;

                LocalDateTime date = LocalDateTime.now(); // Aktuelle Datum holen
                if(tfoundkey.getStartdate().isAfter(date)) //Prüfen ob Schlüssel schon gültig ist
                {
                    throw new WebApplicationException("Schlüssel noch nicht gültig",421);
                }
                else if (tfoundkey.getEnddate().isBefore(date)) //Prüfen ob Schlüssel schon abgelaufen ist
                {
                    throw new WebApplicationException("Schlüssel abgelaufen",422);
                }
            }
        }

        final long nowSec = System.currentTimeMillis() / SECONDS_IN_MILLISECOND;
        long startTime = nowSec - TTL_SECONDS;
        long endTime = nowSec + TTL_SECONDS;
        for (; startTime < endTime; startTime++) {
            final String toHash = apiKey + secretKey + startTime;
            final String sha1 = DigestUtils.sha256Hex(toHash);
            if (sha1.equals(token)) {
                return true;
            }
        }
        return false;
    }
}