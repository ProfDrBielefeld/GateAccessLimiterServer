package resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import feature.GateAccessLimiterServerFeature;
import model.Key;
import model.Permkey;
import model.Tempkey;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import persistence.PermkeyDAO;
import persistence.TempkeyDAO;

import javax.ws.rs.WebApplicationException;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.Date;

/**
 * Implementierung der API
 */
public class GateAccessLimiterServerServiceResource implements GateAccessLimiterServerFeature
{
    private PermkeyDAO PermDAO;
    private TempkeyDAO TempDAO;

    public GateAccessLimiterServerServiceResource(PermkeyDAO PermDAO, TempkeyDAO TempDAO){this.PermDAO = PermDAO; this.TempDAO = TempDAO;}


    @Override
    public boolean opengate(Key sentkey)
    {
        Key foundkey = PermDAO.findKey(sentkey.getGatekey());
        if(foundkey == null) // Wenn kein Permanenter Key
        {
            foundkey = TempDAO.findKey(sentkey.getGatekey()); // Dann Prüfen ob evt Temp key
            if(foundkey == null) // auch nicht gefunden -> Nicht berechtigt
            {
                throw new WebApplicationException("Falscher Key",420); //Fehler für Fehlerhaften Schlüssel

            }
            else // Wenn es sich um einen temp key handelt, prüfe ob Zeitspanne ok ist
            {
                //Cast zum Tempkey um an die Daten zu kommen
                //Benötigt kein try catch, da es sich hier nur noch um einen Tempkey handeln kann
                Tempkey tfoundkey = (Tempkey) foundkey;

                LocalDate date = LocalDate.now(); // Aktuelle Datum holen
                if(tfoundkey.getStartdate().isAfter(date)) //Prüfen ob Schlüssel schon Gültig ist
                {
                    throw new WebApplicationException("Schlüssel noch nicht Gültig",421);
                }
                else if (tfoundkey.getEnddate().isBefore(date)) //Prüfen ob Schlüssel schon abgelaufen ist
                {
                    throw new WebApplicationException("Schlüssel abgelaufen",422);
                }
            }
        }
        /*
            Wenn ein Permanenter Schlüssel oder ein
            Temporärer Schlüssel der aktuell Gültig ist verwendet wurde
            kann die Schranke geöffnet werden.
         */
        //Hier GIOP schalten
        return true;
    }
}
