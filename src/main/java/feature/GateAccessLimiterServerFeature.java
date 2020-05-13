package feature;

import io.dropwizard.hibernate.UnitOfWork;
import model.Key;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("") //API url

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

/**
 * Definition der API
 */
public interface GateAccessLimiterServerFeature
{

    /**
     * Funktion die die Schranke öffnet nach dem der Übertragene Schlüssel geprüft wurde
     * @param sentkey JSON Objekt. Schlüssel der an die API gesentet wird
     * @return Ob Schranke geöffnet
     */
    @GET
    @Path("/open")
    @UnitOfWork
    public boolean opengate(Key sentkey);




}
