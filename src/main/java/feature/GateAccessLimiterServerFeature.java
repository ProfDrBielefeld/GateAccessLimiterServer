package feature;

import filter.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.jsr310.LocalDateTimeParam;
import model.Key;
import model.Tempkey;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Authenticator
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
     * @return Ob Schranke geöffnet
     */
    @GET
    @Path("/open")
    @UnitOfWork
    public boolean opengate(@QueryParam("latuser") double latuser,
                            @QueryParam("lonuser") double lonuser);

    /**
     *
     * @param apiKey Gatekey as api Key
     * @param startdate Datum wann die Laufzeit des Keys beginnen soll
     * @param enddate Darum wann der Key abläuft
     * @return Erstellten Tempkey ans JSON Object
     */
    @POST
    @Path("/tempkey")
    @UnitOfWork
    public Tempkey createTempkey(@QueryParam("apiKey") String apiKey,
                                 @QueryParam("startdate") String startdate,
                                 @QueryParam("enddate") String enddate,
                                 @QueryParam("purpose") String purpose);


    /**
     * Funktion die alle Tempkeys die zu einem Permkey gehören zurück gibt
     * @param apiKey Permkey deren tempkeys gesucht werden
     * @return gefundenen tempkeys
     */
    @GET
    @Path("/tempkey")
    @UnitOfWork
    public List<Tempkey> getTempkeys(@QueryParam("apiKey") String apiKey);



}
