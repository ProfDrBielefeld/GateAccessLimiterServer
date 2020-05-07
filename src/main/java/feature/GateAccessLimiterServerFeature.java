package feature;

import io.dropwizard.hibernate.UnitOfWork;

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

    @GET
    @Path("/open")
    @UnitOfWork
    boolean opengate();




}
