package persistence;


import io.dropwizard.hibernate.AbstractDAO;
import model.Key;
import org.hibernate.*;

import javax.ws.rs.WebApplicationException;
import java.util.List;

/***
 * Klasse zum Verwalten der DB einträge
 */
public class DataStorage extends AbstractDAO<Key> {

    public DataStorage(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

}
