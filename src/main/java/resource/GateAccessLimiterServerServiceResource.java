package resource;

import feature.GateAccessLimiterServerFeature;
import persistence.DataStorage;

import javax.persistence.NoResultException;
import javax.ws.rs.WebApplicationException;
import java.util.Optional;

/**
 * Implementierung der API
 */
public class GateAccessLimiterServerServiceResource implements GateAccessLimiterServerFeature
{
    private DataStorage DAO;

    public GateAccessLimiterServerServiceResource(DataStorage DAO){this.DAO =DAO;}


}
