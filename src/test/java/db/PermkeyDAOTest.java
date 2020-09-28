package db;

import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import model.Permkey;
import model.Tempkey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import persistence.PermkeyDAO;

import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class PermkeyDAOTest {

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder().addEntityClass(Permkey.class).addEntityClass(Tempkey.class).build();
    private PermkeyDAO permkeyDAO;

    @BeforeEach
    public void setUp() throws Exception{
        permkeyDAO = new PermkeyDAO(daoTestRule.getSessionFactory());
        daoTestRule.inTransaction(()->{
           permkeyDAO.create(new Permkey(0,"testkey","Test", new ArrayList<Tempkey>()));
        });
    }

    @Test
    public void findKey(){
        final Permkey testpermkey = daoTestRule.inTransaction(()->
                permkeyDAO.findKey("testkey"));
        assertThat(testpermkey).extracting("gatekey")
                .containsOnly("testkey");
    }
}
