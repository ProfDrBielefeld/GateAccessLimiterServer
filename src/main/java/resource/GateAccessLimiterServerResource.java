package resource;

import com.pi4j.io.gpio.*;
import configuration.Location;
import feature.GateAccessLimiterServerFeature;
import model.Permkey;
import model.Tempkey;
import org.apache.commons.lang3.RandomStringUtils;
import persistence.PermkeyDAO;
import persistence.TempkeyDAO;
import util.DistanceCalculator;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementierung der API
 */
public class GateAccessLimiterServerResource implements GateAccessLimiterServerFeature
{
    private PermkeyDAO PermDAO;
    private TempkeyDAO TempDAO;
    private Location serverlocation;
    //create gpio controler
    final GpioController gpio = GpioFactory.getInstance();
    //PIN für das Relais bereitstellen, GPIO2 Pin, ausgeschaltet laden
    final GpioPinDigitalOutput relaispin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09,"Relais", PinState.LOW);

    public GateAccessLimiterServerResource(PermkeyDAO PermDAO, TempkeyDAO TempDAO, Location location){this.PermDAO = PermDAO; this.TempDAO = TempDAO; this.serverlocation = location;}

    static boolean isLinuxSystem()
    { String osName = System.getProperty("os.name").toLowerCase();
        return osName.indexOf("linux") >= 0;
    }
    @Override
    public boolean opengate(double latuser,double lonuser)
    {
        //Berechnung der Entfernung zwischen client und Server
        if(serverlocation.getMaxdistance() < DistanceCalculator.calculateDistance(serverlocation.getLocation_lat(), serverlocation.getLocation_lon(), latuser,lonuser))
        {
            throw new WebApplicationException("Zu weit von der Schranke entfernt",423);
        }
        //Thread erstellen um das Relais zu schalten.
        new Thread(() ->
        {
            relaispin.high(); //PIN auf HIGH um Relais zu schalten
            try {
                Thread.sleep(1000); // Signal bleibt 1000ms bestehen
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            relaispin.low(); // PIN auf LOW um Relais zu schließen
        }).start();


        /*if (isLinuxSystem())
        { System.out.println("Linux System");
            try {
                Process process = Runtime.getRuntime().exec("python servo.py");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
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
        return foundkey.getTempkeyList();
    }
}
