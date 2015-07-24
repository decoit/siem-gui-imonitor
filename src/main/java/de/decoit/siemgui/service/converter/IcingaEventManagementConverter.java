/* 
 * Copyright (C) 2015 DECOIT GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.decoit.siemgui.service.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.decoit.siemgui.domain.events.AndroidAppEvent;
import de.decoit.siemgui.domain.events.AndroidInfoEvent;
import de.decoit.siemgui.domain.events.AndroidMonitorEvent;
import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.domain.events.FileIntegrityEvent;
import de.decoit.siemgui.domain.events.GeneralEvent;
import de.decoit.siemgui.domain.events.NmapEvent;
import de.decoit.siemgui.domain.events.OpenVasEvent;
import de.decoit.siemgui.domain.events.ServiceStatusEvent;
import de.decoit.siemgui.domain.events.SnortEvent;
import de.decoit.siemgui.domain.events.TsadEvent;
import de.decoit.siemgui.exception.ConversionException;
import de.decoit.siemgui.ext.icinga.dbconnector.entities.LogEntryEntity;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * This implementation of EventManagementConverter can be used to convert IcingaEventLogData objects into the Event domain object.
 * IcingaEventLogData objects are used by the DECOIT Icinga REST Connector.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
public class IcingaEventManagementConverter implements EventManagementConverter<LogEntryEntity> {
	private final Logger LOG = LoggerFactory.getLogger(IcingaEventManagementConverter.class.getName());

	@Autowired
	@Qualifier("icingamapper")
	private ObjectMapper jsonMapper;


	/**
	 * {@inheritDoc}
	 *
	 * The method will create an event object according to the type of event in the log. If there
	 * is no valid JSON found in the event, it is considered as a general monitoring event and the
	 * return type will be GeneralEvent. Otherwise the return type will match the event type,
	 * for example SnortEvent for Snort SIEM events.
	 *
	 * @param evt Input object
	 * @return The created domain object
	 * @throws ConversionException if an error occurs during conversion
	 */
	@Override
	public Event convertEvent(LogEntryEntity evt) {
		if(evt == null) {
			throw new ConversionException("Received null pointer for LogEntryEntity conversion");
		}

		Event e;

		// Find the position of JSON in the event content
		Pattern p = Pattern.compile("\\{\\s?\".+?\":");
		Matcher m = p.matcher(evt.getData());

		if(m.find()) {
			int jsonStart = m.start();
			String jsonString = evt.getData().substring(jsonStart);

			try {
				JsonNode root = jsonMapper.readTree(jsonString);

				String evtType;
				if(root.get("Type") != null) {
					evtType = root.get("Type").asText();
					LOG.warn("JSON object with attribute 'Type' detected, should be lowercase! Type string: " + evtType);
				}
				else if(root.get("type") != null) {
					evtType = root.get("type").asText();
				}
				else {
					evtType = "UNKNOWN";
					LOG.warn("Unknown JSON object found: " + jsonString);
					//throw new ConversionException("No 'type' or 'Type' property found in JSON string");
				}

				// Determine which event type to create
				switch(evtType) {
					case "Snort":
						e = buildSnortEvent(jsonString);
						break;
					case "Nmap":
						e = buildNmapEvent(jsonString);
						break;
					case "OpenVAS":
						e = buildOpenVasEvent(jsonString);
						break;
					case "FIM":
						e = buildFileIntegrityEvent(jsonString);
						break;
					case "TSAD":
						e = buildTsadEvent(jsonString);
						break;
					case "Android":
						e = buildAndroidEvent(jsonString, root.get("class").asText());
						break;
					case "service-status":
						e = buildServiceStatusEvent(jsonString);
						break;
					default:
						GeneralEvent ge = new GeneralEvent();
						ge.setData(evt.getData());

						e = ge;
						//throw new ConversionException("Unknown event type detected: " + evtType);
				}
			}
			catch(IOException ex) {
				throw new ConversionException("JSON parsing failed with IOException!", ex);
			}
		}
		else {
			// When parsing the event text fails, this is no SIEM event so we create a generic event and add the plain text as content
			if(LOG.isDebugEnabled()) {
				LOG.debug("No JSON found in event content, building GeneralEvent object");
			}

			GeneralEvent ge = new GeneralEvent();
			ge.setData(evt.getData());

			e = ge;
		}

		e.setId(evt.getId());
		e.setIcingaTimestamp(evt.getTimestamp().toLocalDateTime());

		return e;
	}


	@Override
	public List<Event> convertEventList(List<LogEntryEntity> evtList) throws ConversionException {
		if(evtList == null) {
			throw new ConversionException("Received null pointer for List<LogEntryEntity> conversion");
		}

		List<Event> domList = evtList.stream().map(t -> this.convertEvent(t)).collect(Collectors.toList());

		return domList;
	}


	/**
	 * Parse a SnortEventContent object from the JSON provided in logContent and fill a SnortEvent object with that content.
	 * The SnortEvent object will have no other attributes set than the content.
	 *
	 * @param logContent JSON content string returned by Icinga
	 * @return Event object filled with content
	 * @throws IOException when parsing the JSON failed
	 */
	private SnortEvent buildSnortEvent(String logContent) throws IOException {
		SnortEvent se = jsonMapper.readValue(logContent, SnortEvent.class);

		return se;
	}


	/**
	 * Parse a NmapEventContent object from the JSON provided in logContent and fill a NmapEvent object with that content.
	 * The NmapEvent object will have no other attributes set than the content.
	 *
	 * @param logContent JSON content string returned by Icinga
	 * @return Event object filled with content
	 * @throws IOException when parsing the JSON failed
	 */
	private NmapEvent buildNmapEvent(String logContent) throws IOException {
		NmapEvent ne = jsonMapper.readValue(logContent, NmapEvent.class);

		return ne;
	}


	/**
	 * Parse a OpenVasEventContent object from the JSON provided in logContent and fill a OpenVasEvent object with that content.
	 * The OpenVasEvent object will have no other attributes set than the content.
	 *
	 * @param logContent JSON content string returned by Icinga
	 * @return Event object filled with content
	 * @throws IOException when parsing the JSON failed
	 */
	private OpenVasEvent buildOpenVasEvent(String logContent) throws IOException {
		OpenVasEvent ove = jsonMapper.readValue(logContent, OpenVasEvent.class);

		return ove;
	}


	/**
	 * Parse a FileIntegrityEventContent object from the JSON provided in logContent and fill a FileIntegrityEvent object with that content.
	 * The FileIntegrityEvent object will have no other attributes set than the content.
	 *
	 * @param logContent JSON content string returned by Icinga
	 * @return Event object filled with content
	 * @throws IOException when parsing the JSON failed
	 */
	private FileIntegrityEvent buildFileIntegrityEvent(String logContent) throws IOException {
		FileIntegrityEvent fie = jsonMapper.readValue(logContent, FileIntegrityEvent.class);

		return fie;
	}


	/**
	 * Parse a TsadEventContent object from the JSON provided in logContent and fill a TsadEvent object with that content.
	 * The TsadEvent object will have no other attributes set than the content.
	 *
	 * @param logContent JSON content string returned by Icinga
	 * @return Event object filled with content
	 * @throws IOException when parsing the JSON failed
	 */
	private TsadEvent buildTsadEvent(String logContent) throws IOException {
		TsadEvent te = jsonMapper.readValue(logContent, TsadEvent.class);

		return te;
	}


	/**
	 * Parse an event of type Android.
	 * The actual return type depends on the event class that is submitted in the second parameter.
	 * Valid classes are 'info', 'apps' and 'monitor' which produce AndroidInfoEvent, AndroidAppEvent or
	 * AndroidMonitorEvent instances accordingly.
	 *
	 * @param logContent JSON content string returned by Icinga
	 * @return Event object filled with content
	 * @throws IOException when parsing the JSON failed
	 */
	private Event buildAndroidEvent(String logContent, String eventClass) throws IOException {
		Event e;
		switch(eventClass) {
			case "info":
				e = jsonMapper.readValue(logContent, AndroidInfoEvent.class);
				break;
			case "apps":
				e = jsonMapper.readValue(logContent, AndroidAppEvent.class);
				break;
			case "monitor":
				e = jsonMapper.readValue(logContent, AndroidMonitorEvent.class);
				break;
			default:
				throw new ConversionException("Unknown event class for Android event: " + eventClass);
		}

		return e;
	}


	/**
	 * Parse an event of type service-status.
	 * The event's data field is empty and thus the object's data attribute is filled with a
	 * generic Object instance. This is required to allow JSON parsing without exceptions.
	 *
	 * @param logContent JSON content string returned by Icinga
	 * @return Event object filled with content
	 * @throws IOException when parsing the JSON failed
	 */
	private Event buildServiceStatusEvent(String logContent) throws IOException {
		ServiceStatusEvent sse = jsonMapper.readValue(logContent, ServiceStatusEvent.class);

		return sse;
	}
}
