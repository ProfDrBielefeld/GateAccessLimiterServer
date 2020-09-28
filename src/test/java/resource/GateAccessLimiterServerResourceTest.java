package resource;

import configuration.Location;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import model.Permkey;
import model.Tempkey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import persistence.PermkeyDAO;
import persistence.TempkeyDAO;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class GateAccessLimiterServerResourceTest
{
	private static final PermkeyDAO PERMKEY_DAO = mock(PermkeyDAO.class);
	private static final TempkeyDAO TEMPKEY_DAO = mock(TempkeyDAO.class);
	private static final Location SERVERLOCATION = mock(Location.class);
	//private static  final Location LOCATION = new Location(51.845157,6.592370,500.00);
	public static final ResourceExtension RESOURCES = ResourceExtension.builder()
			.addResource(new GateAccessLimiterServerResource(PERMKEY_DAO,TEMPKEY_DAO,SERVERLOCATION))
			.build();
	private Tempkey validTKey;
	private Permkey validPKey;

	@BeforeEach
	public void setUp()
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //formatter for Tempkeys
		//Create Tempkey
		LocalDateTime startdate = LocalDateTime.parse("2020-05-10 00:00:00",formatter);
		LocalDateTime enddate = LocalDateTime.parse("2053-08-20 00:00:00",formatter);
		validTKey = new Tempkey(0,startdate,enddate,"TestKeyValid","testtempkeyt0",null);
		//create Permkey
		List<Tempkey> list = new ArrayList<>();
		list.add(validTKey);
		validPKey = new Permkey(0,"testkey","Test", list);
	}

	@AfterEach
	public void tearDown()
	{
		reset(PERMKEY_DAO);
		reset(TEMPKEY_DAO);
		reset(SERVERLOCATION);
	}


	@Test
	public void listTempkeys() throws Exception
	{
		final List<Tempkey> tempkeys = Collections.singletonList(validTKey);
		when(PERMKEY_DAO.findKey(validPKey.getGatekey())).thenReturn(validPKey);

		final List<Tempkey> response = RESOURCES.target("/tempkey").queryParam("apiKey",validPKey.getGatekey()).request().get(new GenericType<List<Tempkey>>()
		{});
		verify(PERMKEY_DAO).findKey(validPKey.getGatekey());
		assertThat(response.get(0).getTempkey_id()).isEqualTo(tempkeys.get(0).getTempkey_id());
	}

	@Test
	public void listTempkeysFailureInvalidKey() throws Exception
	{
		//final List<Tempkey> tempkeys = Collections.singletonList(validTKey);
		when(PERMKEY_DAO.findKey(validTKey.getGatekey())).thenReturn(null);

		final Response response = RESOURCES.target("/tempkey").queryParam("apiKey",validTKey.getGatekey()).request().get();
		verify(PERMKEY_DAO).findKey(validTKey.getGatekey());

		assertThat(response.getStatusInfo()).isNotEqualTo(Response.Status.OK);
		assertThat(response.readEntity(String.class)).contains("Ungültiger Schlüssel");
	}

	@Test
	public void openGate() throws Exception
	{
		when(SERVERLOCATION.getLocation_lat()).thenReturn(51.50606);
		when(SERVERLOCATION.getLocation_lon()).thenReturn(7.45692);
		when(SERVERLOCATION.getMaxdistance()).thenReturn(500.00);
		final Response response = RESOURCES.target("/open")
				.queryParam("latuser",51.50606)
				.queryParam("lonuser",7.45692)
				.request().get();
		verify(SERVERLOCATION).getLocation_lat();
		verify(SERVERLOCATION).getLocation_lon();
		verify(SERVERLOCATION).getMaxdistance();
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		assertThat(response.readEntity(boolean.class)).isEqualTo(true);
	}

	@Test
	public void openGateFailure() throws Exception {
		when(SERVERLOCATION.getLocation_lat()).thenReturn(51.50606);
		when(SERVERLOCATION.getLocation_lon()).thenReturn(7.45692);
		when(SERVERLOCATION.getMaxdistance()).thenReturn(500.00);
		final Response response = RESOURCES.target("/open")
				.queryParam("latuser", 0.0)
				.queryParam("lonuser", 0.0)
				.request().get();
		verify(SERVERLOCATION).getLocation_lat();
		verify(SERVERLOCATION).getLocation_lon();
		verify(SERVERLOCATION).getMaxdistance();
		assertThat(response.getStatusInfo()).isNotEqualTo(Response.Status.OK);
		assertThat(response.readEntity(String.class)).contains("Zu weit von der Schranke entfernt");
	}


}
