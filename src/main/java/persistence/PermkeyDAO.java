package persistence;


import io.dropwizard.hibernate.AbstractDAO;
import model.Permkey;
import org.hibernate.*;

import javax.persistence.NoResultException;

/***
 * Klasse zum Verwalten der DB einträge Perm Keys
 */
public class PermkeyDAO extends AbstractDAO<Permkey> {

    public PermkeyDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    /**
     * Funktion die aus der DB den api key sucht. Wird benötigt um zu schauen ob der angegebene Key auch wirklich in der DB ist
     * @param key Key der via API übergeben wird
     * @return Permkey objekt mit angegebenen Key. null wenn nichts gefunden.
     */
    public Permkey findKey(String key)
    {
        try
        {
            return(Permkey) namedQuery("model.permkey.getbykey").setParameter("apitoken", key).getSingleResult();
        }
        catch(NoResultException nrx) // Wenn kein Eintrg gefunden return null. Recource Funktion behandelt null result
        {
            return null;
        }
    }

    public Permkey create(Permkey permkey) {
        return persist(permkey);
    }
}
