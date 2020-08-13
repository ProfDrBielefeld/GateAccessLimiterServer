package db;

import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import model.Permkey;
import model.Tempkey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import persistence.PermkeyDAO;
import persistence.TempkeyDAO;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TempkeyDAOTest {

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder().addEntityClass(Tempkey.class).addEntityClass(Permkey.class).build();
    private TempkeyDAO tempkeyDAO;

    @BeforeEach
    public void setUp() throws Exception{
        tempkeyDAO = new TempkeyDAO(daoTestRule.getSessionFactory());
        daoTestRule.inTransaction(()->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startdate = LocalDateTime.parse("2020-05-10 00:00:00",formatter);
            LocalDateTime enddate = LocalDateTime.parse("2023-08-20 00:00:00",formatter);
            tempkeyDAO.insert(new Tempkey(0,startdate,enddate,"TestKey","testtempkeyt",null));
        });
    }

    @Test
    public void findKey(){
        final Tempkey testtempkey = daoTestRule.inTransaction(()-> tempkeyDAO.findKey("testtempkeyt"));
        assertThat(testtempkey).extracting("gatekey").containsOnly("testtempkeyt");
    }
}
