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
package de.decoit.siemgui.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class to hold the event information for Snort events stored in the event database.
 * It parses various String values into native Java types, for example LocalDateTime and InetAddress
 * for timestamp and IP addresses.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class SnortEventContent {
	@JsonIgnore
	private final Logger LOG = LoggerFactory.getLogger(SnortEventContent.class.getName());

	private long sensorId;
	private String sensorName;
	private long eventId;
	private String ifName;
	private Integer sourcePort;
	private Integer destinationPort;
	private String icmpType;


	@JsonProperty("sensor-id")
	public long getSensorId() {
		return sensorId;
	}


	/**
	 * Set the ID of the Snort sensor.
	 * The string parameter is parsed into a long.
	 *
	 * @param sensorId Sensor ID as string (must be numerical)
	 */
	@JsonProperty("sensor-id")
	public void setSensorId(String sensorId) {
		this.sensorId = Long.valueOf(sensorId);
	}


	@JsonProperty("sensor-name")
	public String getSensorName() {
		return sensorName;
	}


	@JsonProperty("sensor-name")
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}


	@JsonProperty("event-id")
	public long getEventId() {
		return eventId;
	}


	/**
	 * Set the Snort internal event ID.
	 * The string parameter is parsed into a long.
	 *
	 * @param eventId Event ID as string (must be numerical)
	 */
	@JsonProperty("event-id")
	public void setEventId(String eventId) {
		this.eventId = Long.valueOf(eventId);
	}


	@JsonProperty("interface")
	public String getIfName() {
		return ifName;
	}


	@JsonProperty("interface")
	public void setIfName(String ifName) {
		this.ifName = ifName;
	}


	/**
	 * Return the TCP source port of the event.
	 * Will be null of no TCP source port was provided by the event data.
	 *
	 * @return Source port
	 */
	@JsonProperty("src")
	public Integer getSourcePort() {
		return sourcePort;
	}


	/**
	 * Set the source port of the event.
	 * The string parameter is parsed into an Integer object.
	 * If the provided value cannot be parsed into an Integer, the attribute will be set to null.
	 * This is fine since port information is optional in Snort events.
	 *
	 * @param tcpSourcePort Source port as string
	 */
	@JsonProperty("src")
	public void setSourcePort(String tcpSourcePort) {
		try {
			this.sourcePort = Integer.valueOf(tcpSourcePort);
		}
		catch (NumberFormatException ex) {
			this.sourcePort = null;
		}
	}


	/**
	 * Return the destination port of the event.
	 * Will be null of no destination port was provided by the event data.
	 *
	 * @return Destination port
	 */
	@JsonProperty("dest")
	public Integer getTcpDestinationPort() {
		return destinationPort;
	}


	/**
	 * Set the destination port of the event.
	 * The string parameter is parsed into an Integer object.
	 * If the provided value cannot be parsed into an Integer, the attribute will be set to null.
	 * This is fine since port information is optional in Snort events.
	 *
	 * @param tcpDestinationPort Destination port as string
	 */
	@JsonProperty("dest")
	public void setTcpDestinationPort(String tcpDestinationPort) {
		try {
			this.destinationPort = Integer.valueOf(tcpDestinationPort);
		}
		catch (NumberFormatException ex) {
			this.destinationPort = null;
		}
	}


	@JsonProperty("icmp-type")
	public String getIcmpType() {
		return icmpType;
	}


	@JsonProperty("icmp-type")
	public void setIcmpType(String icmpType) {
		this.icmpType = icmpType;
	}
}
