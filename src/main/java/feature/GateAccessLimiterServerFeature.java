package feature;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.jsr310.LocalDateTimeParam;
import model.Key;
import model.Tempkey;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

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
     * @param sendkey JSON Objekt. Schlüssel der an die API gesentet wird
     * @return Ob Schranke geöffnet
     */
    @GET
    @Path("/open")
    @UnitOfWork
    public boolean opengate(Key sendkey,
                            @QueryParam("latuser") double latuser,
                            @QueryParam("lonuser") double lonuser);

    /**
     *
     * @param sendkey JSON Objekt mit dem Permkey
     * @param startdate Datum wann die Laufzeit des Keys beginnen soll
     * @param enddate Darum wann der Key abläuft
     * @return Erstellten Tempkey ans JSON Object
     */
    @POST
    @Path("/tempkey")
    @UnitOfWork
    public Tempkey createTempkey(Key sendkey,
                                 @QueryParam("startdate") String startdate,
                                 @QueryParam("enddate") String enddate);



}
