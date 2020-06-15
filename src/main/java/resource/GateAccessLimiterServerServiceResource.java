package resource;

import configuration.Location;
import feature.GateAccessLimiterServerFeature;
import model.Key;
import model.Permkey;
import model.Tempkey;
import org.apache.commons.lang3.RandomStringUtils;
import persistence.PermkeyDAO;
import persistence.TempkeyDAO;
import util.DistanceCalculator;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDate;

/**
 * Implementierung der API
 */
public class GateAccessLimiterServerServiceResource implements GateAccessLimiterServerFeature
{
    private PermkeyDAO PermDAO;
    private TempkeyDAO TempDAO;
    private Location location;

    public GateAccessLimiterServerServiceResource(PermkeyDAO PermDAO, TempkeyDAO TempDAO, Location location){this.PermDAO = PermDAO; this.TempDAO = TempDAO; this.location = location;}


    @Override
    public boolean opengate(Key sendkey,double latuser,double lonuser)
    {
        Key foundkey = PermDAO.findKey(sendkey.getGatekey());
        if(foundkey == null) // Wenn kein Permanenter Key
        {
            foundkey = TempDAO.findKey(sendkey.getGatekey()); // Dann Prüfen ob evt Temp key
            if(foundkey == null) // auch nicht gefunden -> Nicht berechtigt
            {
                throw new WebApplicationException("Falscher Schlüssel",420); //Fehler für Fehlerhaften Schlüssel

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
        System.out.println(lonuser);
        System.out.println(latuser);
        System.out.println("Debug entfernung: " + DistanceCalculator.calculateDistance(location.getLocation_lat(), location.getLocation_lon(), latuser,lonuser));
        if(location.getMaxdistance() < DistanceCalculator.calculateDistance(location.getLocation_lat(), location.getLocation_lon(), latuser,lonuser))
        {
            throw new WebApplicationException("Zu weit von der Schranke enfernt",423);
        }
        /*
            Wenn ein Permanenter Schlüssel oder ein
            Temporärer Schlüssel der aktuell Gültig ist verwendet wurde
            kann die Schranke geöffnet werden.
         */
        //Hier GIOP schalten
        return true;
    }

    @Override
    public Tempkey createTempkey(Key sendkey, String startdate, String enddate)
    {
        Permkey foundkey = PermDAO.findKey(sendkey.getGatekey());
        if(foundkey == null) // Wenn kein Permanenter Key
        {
            throw new WebApplicationException("Falscher Schlüssel",420); //Fehler für Fehlerhaften Schlüssel
        }
        Tempkey newtempKey = new Tempkey();
        newtempKey.setStartdate(LocalDate.parse(startdate));
        newtempKey.setEnddate(LocalDate.parse(enddate));
        newtempKey.setParentkey(foundkey);
        int length = 15;
        boolean useLetters = true;
        boolean useNumbers = true;
        String gatekeystring = foundkey.getGatekey().substring(0,5) + RandomStringUtils.random(length, useLetters, useNumbers); ;
        newtempKey.setGatekey(gatekeystring);
        TempDAO.insert(newtempKey);
        return newtempKey;
    }
}
