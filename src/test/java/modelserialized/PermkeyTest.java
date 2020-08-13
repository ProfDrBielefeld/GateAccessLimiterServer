package modelserialized;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import model.Permkey;
import model.Tempkey;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;


public class PermkeyTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception
    {
        final Permkey permkey = new Permkey(0,"testkey","Test", new ArrayList<Tempkey>());
        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/permkey.json"), Permkey.class));

        assertThat(MAPPER.writeValueAsString(permkey)).isEqualTo(expected);
    }
    @Test
    public void deserializesFromJSON() throws Exception {
        final Permkey permkey = new Permkey(0,"testkey","Test", new ArrayList<Tempkey>());
        final Permkey deserializedpermkey = MAPPER.readValue(fixture("fixtures/permkey.json"),Permkey.class);
        assertThat(deserializedpermkey.getNote()).isEqualTo(permkey.getNote());
        assertThat(deserializedpermkey.getPermkey_id()).isEqualTo(permkey.getPermkey_id());
        assertThat(deserializedpermkey.getGatekey()).isEqualTo(permkey.getGatekey());
        assertThat(deserializedpermkey.getTempkeyList()).isEqualTo(permkey.getTempkeyList());
    }
}
