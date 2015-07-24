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

/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public enum EventType {
	GENERAL,
	SNORT,
	NMAP,
	OPEN_VAS,
	TSAD,
	FIM,
	ANDROID,
	SERVICE_STATUS;


	public static EventType fromTypeString(String type) {
		switch(type) {
			case "Snort":
				return SNORT;
			case "Nmap":
				return NMAP;
			case "OpenVAS":
				return OPEN_VAS;
			case "TSAD":
				return TSAD;
			case "FIM":
				return FIM;
			case "Android":
				return ANDROID;
			case "service-status":
				return SERVICE_STATUS;
			default:
				return GENERAL;
		}
	}
}
