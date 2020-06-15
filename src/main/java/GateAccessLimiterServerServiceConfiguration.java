import com.fasterxml.jackson.annotation.JsonProperty;
import configuration.Location;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class GateAccessLimiterServerServiceConfiguration extends Configuration {
    public GateAccessLimiterServerServiceConfiguration() {

    }

    /**
     * A factory used to connect to a relational database management system.
     * Factories are used by Dropwizard to group together related configuration
     * parameters such as database connection driver, URI, password etc.
     */
    @NotNull
    @Valid
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    /**
     * A getter for the database factory.
     *
     * @return An instance of database factory deserialized from the
     * configuration file passed as a command-line argument to the application.
     */

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    /**
     * A factory used to get the location of the Server from the config file
     */
    @Valid
    @NotNull
    private Location location = new Location();

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }
}
