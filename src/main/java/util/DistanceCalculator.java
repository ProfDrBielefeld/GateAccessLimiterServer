package util;

/**
 * Hilfsklasse zur Berechnung von der Distanz zwischen 2 GPS Koordinaten
 */
public class DistanceCalculator
{
    /**
     *
     * @param lat1 lat des Servers
     * @param lon1 lon des Servers
     * @param lat2 lat des Users
     * @param lon2 lon des Users
     * @return Entfernung zurm Server
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2)
    {
        final double earth_radius = 6378.388; // Radius der Erde
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        //Berechnung der Haversine Formel
        double a = Math.pow(Math.sin(latDistance / 2),2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(lonDistance / 2),2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earth_radius * c * 1000; // umwandeln in Meter

        return distance;
    }
}
