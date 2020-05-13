package persistence;


import io.dropwizard.hibernate.AbstractDAO;
import model.Permkey;
import model.Tempkey;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;

/***
 * Klasse zum Verwalten der DB einträge für die Temp Keys
 */
public class TempkeyDAO extends AbstractDAO<Tempkey> {

    public TempkeyDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public Tempkey findKey(String key)
    {
        try
        {
            return (Tempkey) namedQuery("model.tempkey.getbykey").setParameter("apitoken", key).getSingleResult();
        }
        catch(NoResultException nrx) // Wenn kein Eintrg gefunden return null. Recource Funktion behandelt null result
        {
            return null;
        }
    }

}
