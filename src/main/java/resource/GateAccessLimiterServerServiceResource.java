package resource;

import feature.GateAccessLimiterServerFeature;
import persistence.PermkeyDAO;
import persistence.TempkeyDAO;

/**
 * Implementierung der API
 */
public class GateAccessLimiterServerServiceResource implements GateAccessLimiterServerFeature
{
    private PermkeyDAO PermDAO;
    private TempkeyDAO TempDAO;

    public GateAccessLimiterServerServiceResource(PermkeyDAO PermDAO, TempkeyDAO TempDAO){this.PermDAO = PermDAO; this.TempDAO = TempDAO;}


    @Override
    public boolean opengate() {
        return false;
    }
}
