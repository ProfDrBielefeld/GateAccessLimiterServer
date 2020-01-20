package feature;

import io.dropwizard.hibernate.UnitOfWork;
import model.Leistungsdiagnostik;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("") //API url

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

/**
 * Definition der API
 */
public interface GateAccessLimiterServerFeature
{
   /* Beispiel:
    @GET
    @Path("/id")
    @UnitOfWork
    Key getbyID(@QueryParam("id") Optional<Integer> id);
    */

}
