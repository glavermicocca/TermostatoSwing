package thermostat.thread;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import mapper.Mapper;
import thermostat.bean.Sensor;
import thermostat.bean.Values;
import thermostat.gpio.Rele;

public class CalendarQuickstart {
	/** Application name. */
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

	private static final String CALENDAR_NAME = "Crono Termo Piccardi";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(Mapper.ABSOLUTE_PATH_USER_HOME,
			".credentials/calendar-java-quickstart.json");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/calendar-java-quickstart.json
	 */
	private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		//System.out.println(Mapper.ABSOLUTE_PATH_APPLICATION + "resources/client_secret.json");

		InputStream in = new FileInputStream(Mapper.ABSOLUTE_PATH_APPLICATION + "resources/client_secret.json");
		// CalendarQuickstart.class.getResourceAsStream("/client_secret.json");

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		//System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws IOException
	 */
	public static com.google.api.services.calendar.Calendar getCalendarService() throws IOException {
		Credential credential = authorize();
		return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	private static int counter = 0;
	private static final int WAITING_TIME = 5;

	public static void check(JPanel panelContainerSetTemperature, JLabel listSchedule, Values values, Rele rele,
			Sensor sensor, JLabel lblTemperature, JButton btnOnoff, JPanel panelContainerSchedule) throws IOException {
		// Build a new authorized API client service.
		// Note: Do not confuse this class with the
		// com.google.api.services.calendar.model.Calendar class.
		//System.out.println("Start here");

		com.google.api.services.calendar.Calendar service = getCalendarService();

		// List the next 10 events from the primary calendar.
		DateTime now = new DateTime(System.currentTimeMillis());
		DateTime now1m = new DateTime(System.currentTimeMillis() + 86400000); // 60000
		DateTime now1min = new DateTime(System.currentTimeMillis() + 60000);

		// Iterate through entries in calendar list
		String pageToken = null;
		String calendarId = null;
		do {
			CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
			List<CalendarListEntry> items = calendarList.getItems();

			for (CalendarListEntry calendarListEntry : items) {
				//System.out.println(calendarListEntry.getSummary());
				if (calendarListEntry.getSummary().equals(CALENDAR_NAME)) {
					calendarId = calendarListEntry.getId();
				}
			}
			pageToken = calendarList.getNextPageToken();
		} while (pageToken != null);

		Events events = service.events().list(calendarId).setMaxResults(10).setTimeMin(now).setTimeMax(now1m)
				.setOrderBy("startTime").setSingleEvents(true).execute();
		List<Event> items = events.getItems();
		if (items.size() == 0) {
			//System.out.println("No upcoming events found.");
			listSchedule.setText("Nessun evento");
		} else {
			String textEventi = "<html>";
			for (Event event : items) {
				DateTime start = event.getStart().getDateTime();
				if (start == null) {
					start = event.getStart().getDate();
				}

				DateTime end = event.getEnd().getDateTime();
				if (end == null) {
					end = event.getEnd().getDate();
				}

				SimpleDateFormat sdf2 = new SimpleDateFormat("EEE (HH:mm)");
				Date date = new Date(start.getValue());
				String valnow = sdf2.format(date);

				Date dateEnd = new Date(end.getValue());
				String valnowEnd = sdf2.format(dateEnd);

				//System.out.printf("%s (%s -> %s)\n", event.getSummary() == null ? "" : event.getSummary(), valnow, valnowEnd);

				textEventi += (event.getSummary() == null ? "" : event.getSummary()) + " " + valnow + " " + valnowEnd
						+ "<br/>";
			}
			listSchedule.setText(textEventi + "</html>");
		}

		Events eventsSingle = service.events().list(calendarId).setMaxResults(1).setTimeMin(now).setTimeMax(now1min)
				.setOrderBy("startTime").setSingleEvents(true).execute();
		List<Event> itemsSingle = eventsSingle.getItems();
		if (itemsSingle.size() == 0) {
			System.err.println("EVENTI NON TROVATI!");

			panelContainerSchedule.setBounds(162, 47, 154, 125);
			listSchedule.setBounds(2, 2, 149, 119);
			panelContainerSetTemperature.setBounds(4, 47, 154, 190);

			if (values.isToggleButton()) {
				// se sono in manuale non faccio niente!
			} else {
				rele.set(Rele.CALDAIA, Rele.OFF); // altrimenti spengo
				btnOnoff.setText("OFF");
			}
		} else if (itemsSingle.size() > 0) {

			panelContainerSchedule.setBounds(4, 47, 312, 125);
			listSchedule.setBounds(2, 2, 306, 119);
			panelContainerSetTemperature.setBounds(0, 0, 0, 0);

			DateTime start = itemsSingle.get(0).getStart().getDateTime();
			if (start == null) {
				start = itemsSingle.get(0).getStart().getDate();
			}
			System.err.printf("EVENTI TROVATI : %s (%s)\n", itemsSingle.get(0).getSummary() == null ? "" : itemsSingle.get(0).getSummary(), start.toString());

			Pattern pattern = Pattern.compile("[+-]?\\d*\\.?\\d+");
			Matcher matcher = pattern
					.matcher(itemsSingle.get(0).getSummary() == null ? "" : itemsSingle.get(0).getSummary());

			// CERCA LA TEMPERATURA NELL'EVENTO
			if (matcher.find()) {
				System.err.println(matcher.group(0));
				Double currentValue = Double.parseDouble(matcher.group(0));
				values.setTemperature(currentValue);
				lblTemperature.setText("" + currentValue);
			}

			matcher = pattern.matcher(lblTemperature.getText());
			if (matcher.find()) {
				//System.err.println(matcher.group(0));
				Double currentDisplaiedValue = Double.parseDouble(matcher.group(0));
				values.setTemperature(currentDisplaiedValue);
			}

			values.setToggleButton(false); // forzo il bottone a spento se sono
											// in modalita calendario
			btnOnoff.setText("ON");

			if (sensor.isHigher()) {
				if (counter % WAITING_TIME == 0) {
					rele.set(Rele.CALDAIA, Rele.ON);
				}
			} else {
				if (counter % WAITING_TIME == 0) {
					rele.set(Rele.CALDAIA, Rele.OFF);
				}
			}

			counter++;
		}
	}
}