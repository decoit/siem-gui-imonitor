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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Abstract base class for objects containing datasets from the event database.
 * It must be subclassed to add event specific content and other properties.
 *
 * @author Thomas Rix (rix@decoit.de)
 * @param <DATA> Event content data type that will be used by the subclass
 */
public abstract class Event<DATA> {
	@JsonIgnore
	private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	protected long id;
	protected LocalDateTime icingaTimestamp;
	protected EventType type;
	protected LocalDateTime timestamp;
	protected String status;
	protected InetAddress sourceIp;
	protected InetAddress destinationIp;
	protected EventProtocol protocol;
	protected String message;
	protected String eventClass;

	@JsonProperty("data")
	protected DATA data;


	public Event(EventType type) {
		this.type = type;
	}


	@JsonProperty("id")
	public long getId() {
		return id;
	}


	@JsonProperty("id")
	public void setId(long id) {
		this.id = id;
	}


	@JsonProperty("icinga-timestamp")
	public LocalDateTime getIcingaTimestamp() {
		return icingaTimestamp;
	}


	@JsonProperty("icinga-timestamp")
	public void setIcingaTimestamp(LocalDateTime icingaTimestamp) {
		this.icingaTimestamp = icingaTimestamp;
	}


	@JsonProperty("type")
	public EventType getType() {
		return type;
	}


	@JsonProperty("type")
	public void setType(String type) {
		this.type = EventType.fromTypeString(type);
	}


	public void setType(EventType type) {
		this.type = type;
	}


	@JsonProperty("timestamp")
	public LocalDateTime getTimestamp() {
		return timestamp;
	}


	@JsonProperty("timestamp")
	public void setTimestamp(String timestamp) {
		this.timestamp = LocalDateTime.parse(timestamp, dateFormat);
	}


	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}


	@JsonProperty("status")
	public String getStatus() {
		return status;
	}


	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}


	@JsonProperty("ipsrc")
	public InetAddress getSourceIp() {
		return sourceIp;
	}


	@JsonProperty("ipsrc")
	public void setSourceIp(String sourceIp) throws UnknownHostException {
		this.sourceIp = InetAddress.getByName(sourceIp);
	}


	public void setSourceIp(InetAddress sourceIp) {
		this.sourceIp = sourceIp;
	}


	@JsonProperty("ipdest")
	public InetAddress getDestinationIp() {
		return destinationIp;
	}


	@JsonProperty("ipdest")
	public void setDestinationIp(String destinationIp) throws UnknownHostException {
		this.destinationIp = InetAddress.getByName(destinationIp);
	}


	public void setDestinationIp(InetAddress destinationIp) {
		this.destinationIp = destinationIp;
	}


	@JsonProperty("protocol")
	public EventProtocol getProtocol() {
		return protocol;
	}


	@JsonProperty("protocol")
	public void setProtocol(String protocol) {
		this.protocol = EventProtocol.fromEventText(protocol);
	}


	public void setProtocol(EventProtocol protocol) {
		this.protocol = protocol;
	}


	@JsonProperty("message")
	public String getMessage() {
		return message;
	}


	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}


	@JsonProperty("class")
	public String getEventClass() {
		return eventClass;
	}


	@JsonProperty("class")
	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}


	public DATA getData() {
		return data;
	}


	public void setData(DATA data) {
		this.data = data;
	}
}
