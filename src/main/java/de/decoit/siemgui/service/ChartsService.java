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
package de.decoit.siemgui.service;

import de.decoit.siemgui.exception.ExternalServiceException;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgChart;


/**
 * An interface to define a service that generates message objects for charts.
 * These objects are used to transfer a chart definition and values to the frontend.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public interface ChartsService {
	/**
	 * Create a column chart that shows today's events per hour.
	 * Each bar represent an hour from 0 to 23. The Event are grouped by the hour value
	 * of their timestamp. The event timestamp is used, not the sensor specific timestamp.
	 *
	 * @param chartIndex Index of the element used for this chart in the frontend
	 * @return A message object that can be transfered using HTTP or WebSocket
	 *
	 * @throws ExternalServiceException if contacting the event database failed
	 */
	public MsgChart getEventsTodayChart(int chartIndex) throws ExternalServiceException;


	/**
	 * Create a bar chart that shows today's event per sensor.
	 * Each bar represents a sensor. Events are grouped by their type.
	 *
	 * @param chartIndex Index of the element used for this chart in the frontend
	 * @return A message object that can be transfered using HTTP or WebSocket
	 *
	 * @throws ExternalServiceException if contacting the event database failed
	 */
	public MsgChart getEventsTodayPerSensorChart(int chartIndex) throws ExternalServiceException;


	/**
	 * Create a bar chart that shows all active incidents per threat level.
	 * Each bar represents a threat level. Incidents are grouped by their threat level value.
	 *
	 * @param chartIndex Index of the element used for this chart in the frontend
	 * @return A message object that can be transfered using HTTP or WebSocket
	 *
	 * @throws ExternalServiceException if contacting the incident database failed
	 */
	public MsgChart getActiveIncidentsByThreatLevel(int chartIndex) throws ExternalServiceException;
}
