package configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Location {
    @NotNull
    @Min(-90)
    @Max(90)
    private double location_lat;

    @NotNull
    @Min(-180)
    @Max(180)
    private double location_lon;

    @NotNull
    @Min(10)
    private double maxdistance;

    public Location(){}

    public Location(double location_lat, double location_lon, double maxdistance)
    {
        this.location_lat = location_lat;
        this.location_lon = location_lon;
        this.maxdistance = maxdistance;
    }

    @JsonProperty
    public double getLocation_lat() {
        return location_lat;
    }

    @JsonProperty
    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }

    @JsonProperty
    public double getLocation_lon() {
        return location_lon;
    }

    @JsonProperty
    public void setLocation_lon(double location_lon) {
        this.location_lon = location_lon;
    }

    @JsonProperty
    public double getMaxdistance() {
        return maxdistance;
    }

    @JsonProperty
    public void setMaxdistance(double maxdistance) {
        this.maxdistance = maxdistance;
    }
}
