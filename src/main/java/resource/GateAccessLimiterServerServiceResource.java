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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public boolean opengate(double latuser,double lonuser)
    {
        if(location.getMaxdistance() < DistanceCalculator.calculateDistance(location.getLocation_lat(), location.getLocation_lon(), latuser,lonuser))
        {
            throw new WebApplicationException("Zu weit von der Schranke entfernt",423);
        }
        /*
            Wenn ein Permanenter Schlüssel oder ein
            Temporärer Schlüssel der aktuell Gültig ist verwendet wurde
            kann die Schranke geöffnet werden.
         */
        //todo: Hier GIOP schalten
        return true;
    }

    @Override
    public Tempkey createTempkey(String apiKey, String startdate, String enddate, String purpose)
    {
        Permkey foundkey = PermDAO.findKey(apiKey);
        if(foundkey == null) // Wenn kein Permanenter Key
        {
            throw new WebApplicationException("Temporäre Schlüssel dürfen keine Schlüssel erstellen",420); //Fehler für Fehlerhaften Schlüssel
        }
        Tempkey newtempKey = new Tempkey();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        newtempKey.setStartdate(LocalDateTime.parse(startdate,formatter));
        newtempKey.setEnddate(LocalDateTime.parse(enddate,formatter));
        newtempKey.setParentkey(foundkey);
        newtempKey.setPurpose(purpose);
        int length = 15;
        boolean useLetters = true;
        boolean useNumbers = true;
        String gatekeystring = foundkey.getGatekey().substring(0,5) + RandomStringUtils.random(length, useLetters, useNumbers); ;
        newtempKey.setGatekey(gatekeystring);
        TempDAO.insert(newtempKey);
        return newtempKey;
    }

    @Override
    public List<Tempkey> getTempkeys(String apiKey) {
        Permkey foundkey = PermDAO.findKey(apiKey);
        if(foundkey == null) // Wenn kein Permanenter Key
        {
            throw new WebApplicationException("Ungültiger Schlüssel",420); //Fehler für Fehlerhaften Schlüssel
        }
        return foundkey.getTempkeyListList();
    }
}
