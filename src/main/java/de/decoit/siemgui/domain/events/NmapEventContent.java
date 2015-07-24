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
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class to hold the event information for Nmap events stored in the event database.
 * It parses various String values into native Java types, for example LocalDateTime and InetAddress
 * for timestamp and IP addresses.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class NmapEventContent {
	@JsonIgnore
	private final Logger LOG = LoggerFactory.getLogger(NmapEventContent.class.getName());
	
	private Set<Integer> allowedPorts;
	private Set<Integer> scannedPorts;


	/**
	 * Default constructor.
	 * Initialize all List attributes with empty ArrayLists.
	 */
	public NmapEventContent() {
		this.allowedPorts = new HashSet<>();
		this.scannedPorts = new HashSet<>();
	}


	@JsonProperty("ports-allowed")
	public Set<Integer> getAllowedPorts() {
		return allowedPorts;
	}


	/**
	 * Set the list of ports that should be found open.
	 * The comma separated list string is converted into a List&lt;Integer&gt;.
	 *
	 * @param allowedPorts Comma separated port list
	 */
	@JsonProperty("ports-allowed")
	public void setAllowedPorts(String allowedPorts) {
		this.allowedPorts = splitPorts(allowedPorts);
	}


	@JsonProperty("ports-scanned")
	public Set<Integer> getScannedPorts() {
		return scannedPorts;
	}


	/**
	 * Set the list of ports that were actually scanned.
	 * The comma separated list string is converted into a List&lt;Integer&gt;.
	 *
	 * @param scannedPorts Comma separated port list
	 */
	@JsonProperty("ports-scanned")
	public void setScannedPorts(String scannedPorts) {
		this.scannedPorts = splitPorts(scannedPorts);
	}


	/**
	 * Return a set of ports that are closed but should be open.
	 * These are the ports that caused a WARNING status from Nmap.
	 *
	 * @return Set of closed ports
	 */
	@JsonProperty("invalid-closed-ports")
	public Set<Integer> getInvalidClosedPorts() {
		Set<Integer> rv = Sets.difference(allowedPorts, scannedPorts);

		return rv;
	}


	/**
	 * Return a set of ports that are open but should be closed.
	 * These are the ports that caused a CRITICAL status from Nmap.
	 *
	 * @return Set of open ports
	 */
	@JsonProperty("invalid-open-ports")
	public Set<Integer> getInvalidOpenPorts() {
		Set<Integer> rv = Sets.difference(scannedPorts, allowedPorts);

		return rv;
	}


	/**
	 * Split a list of port numbers into an Integer list.
	 * The list must be comma separated and can contain positive and negative
	 * values. Negative values are possible in case of the result port list.
	 *
	 * @param portList Comma separated port list
	 * @return List containing port numbers
	 */
	private Set<Integer> splitPorts(String portList) {
		Set<Integer> rv = new HashSet<>();

		if (portList.length() > 0) {
			String[] ports = portList.split(",");

			for (String p : ports) {
				int port = Integer.valueOf(p);
				rv.add(port);
			}
		}

		return rv;
	}
}
