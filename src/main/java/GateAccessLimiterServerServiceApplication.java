import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import model.Leistungsdiagnostik;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import persistence.DataStorage;
import resource.GateAccessLimiterServerServiceResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class GateAccessLimiterServerServiceApplication extends Application<GateAccessLimiterServerServiceConfiguration> {

    public static void main(String... args)throws Exception {
        new GateAccessLimiterServerServiceApplication().run(args);
    }



    /**
     * Hibernate bundle.
     */
    private final HibernateBundle<GateAccessLimiterServerServiceConfiguration> hibernateBundle = new HibernateBundle<GateAccessLimiterServerServiceConfiguration>( Leistungsdiagnostik.class)
    {
        @Override
        public DataSourceFactory getDataSourceFactory(GateAccessLimiterServerServiceConfiguration configuration)
        {
            return configuration.getDataSourceFactory();
        }
    };


    @Override
    public void initialize(Bootstrap<GateAccessLimiterServerServiceConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(GateAccessLimiterServerServiceConfiguration gateAccessLimiterServerServiceConfiguration, Environment environment) throws Exception {

        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // DO NOT pass a preflight request to down-stream auth filters
        // unauthenticated preflight requests should be permitted by spec
        cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());

        final DataStorage LDDAO = new DataStorage(hibernateBundle.getSessionFactory());

        final GateAccessLimiterServerServiceResource LDServiceResource = new GateAccessLimiterServerServiceResource(LDDAO);
        environment.jersey().register(LDServiceResource);


    }
}
