package persistence;


import io.dropwizard.hibernate.AbstractDAO;
import model.Permkey;
import model.Tempkey;
import org.hibernate.SessionFactory;

/***
 * Klasse zum Verwalten der DB einträge für die Temp Keys
 */
public class TempkeyDAO extends AbstractDAO<Tempkey> {

    public TempkeyDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public Tempkey findKey(String apikey)
    {
        return (Tempkey) namedQuery("model.tempkey.getbykey").getSingleResult();
    }

}
