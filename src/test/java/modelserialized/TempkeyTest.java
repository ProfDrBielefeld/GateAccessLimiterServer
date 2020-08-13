package modelserialized;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import model.Permkey;
import model.Tempkey;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;


public class TempkeyTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startdate = LocalDateTime.parse("2020-05-10 00:00:00",formatter);
        LocalDateTime enddate = LocalDateTime.parse("2023-08-20 00:00:00",formatter);
        final Tempkey tempkey = new Tempkey(0,startdate,enddate,"TestKey","testtempkeyt",new Permkey());
        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/tempkey.json"), Tempkey.class));

        assertThat(MAPPER.writeValueAsString(tempkey)).isEqualTo(expected);
    }
    @Test
    public void deserializesFromJSON() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startdate = LocalDateTime.parse("2020-05-10 00:00:00",formatter);
        LocalDateTime enddate = LocalDateTime.parse("2023-08-20 00:00:00",formatter);
        final Tempkey tempkey = new Tempkey(0,startdate,enddate,"TestKey","testtempkeyt",null);
        final Tempkey deserializedtempkey = MAPPER.readValue(fixture("fixtures/tempkey.json"),Tempkey.class);
        assertThat(deserializedtempkey.getTempkey_id()).isEqualTo(tempkey.getTempkey_id());
        assertThat(deserializedtempkey.getGatekey()).isEqualTo(tempkey.getGatekey());
        assertThat(deserializedtempkey.getStartdate()).isEqualTo(tempkey.getStartdate());
        assertThat(deserializedtempkey.getEnddate()).isEqualTo(tempkey.getEnddate());
        assertThat(deserializedtempkey.getPurpose()).isEqualTo(tempkey.getPurpose());
    }
}
