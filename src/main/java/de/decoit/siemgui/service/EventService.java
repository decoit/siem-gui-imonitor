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

import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.exception.ExternalServiceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * An interface to define a service that reads data from the event management system.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public interface EventService {
	/**
	 * Retrieve a list of all events that happened today.
	 * This means they happened between 00:00:00 and 23:59:59 on the date that
	 * LocalDateTime.now() returns. Default time zone set in Java is used.
	 *
	 * @return A list of events in chronological order, starting with the newest
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public List<Event> getEventsToday() throws ExternalServiceException;


	/**
	 * Retrieve a list of all events that happened yesterday.
	 * This means they happened between 00:00:00 and 23:59:59 on the date that
	 * LocalDateTime.now().minusDays(1) returns. Default time zone set in Java is used.
	 *
	 * @return A list of events in chronological order, starting with the newest
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public List<Event> getEventsYesterday() throws ExternalServiceException;


	/**
	 * Retrieve a list of all events that happened this week.
	 * "This week" means they happened between 00:00:00 on monday and 23:59:59 on sunday
	 * of the week that LocalDateTime.now() lies in. Default time zone set in Java is used.
	 *
	 * @return A list of events in chronological order, starting with the newest
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public List<Event> getEventsThisWeek() throws ExternalServiceException;


	/**
	 * Retrieve a list of all events that happened this week.
	 * "This week" means they happened between 00:00:00 on monday and 23:59:59 on sunday
	 * of the week that LocalDateTime.now().minusWeeks(1) lies in. Default time zone set in Java is used.
	 *
	 * @return A list of events in chronological order, starting with the newest
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public List<Event> getEventsLastWeek() throws ExternalServiceException;


	/**
	 * Retrieve a list of all events that happened in the specified timeframe.
	 * The start and end of the timeframe will be interpreted with seconds as smallest chrono unit.
	 *
	 * @param from Start of timeframe
	 * @param to   End of timeframe
	 * @return A list of events in chronological order, starting with the newest
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public List<Event> getEventsInTimeframe(LocalDateTime from, LocalDateTime to) throws ExternalServiceException;


	public long countEventsInTimeframe(LocalDateTime from, LocalDateTime to) throws ExternalServiceException;


	/**
	 * Retrieve the number of events that happened today.
	 * This means they happened between 00:00:00 and 23:59:59 on the date that
	 * LocalDateTime.now() returns. Default time zone set in Java is used.
	 *
	 * @return Number of events
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public long countEventsToday() throws ExternalServiceException;


	/**
	 * Retrieve the number of events that happened yesterday.
	 * This means they happened between 00:00:00 and 23:59:59 on the date that
	 * LocalDateTime.now().minusDays(1) returns. Default time zone set in Java is used.
	 *
	 * @return Number of events
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public long countEventsYesterday() throws ExternalServiceException;


	/**
	 * Retrieve the number of events that happened this week.
	 * "This week" means they happened between 00:00:00 on monday and 23:59:59 on sunday
	 * of the week that LocalDateTime.now() lies in. Default time zone set in Java is used.
	 *
	 * @return Number of events
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public long countEventsThisWeek() throws ExternalServiceException;


	/**
	 * Retrieve the number of events that happened this week.
	 * "Last week" means they happened between 00:00:00 on monday and 23:59:59 on sunday
	 * of the week that LocalDateTime.now().minusWeeks(1) lies in. Default time zone set in Java is used.
	 *
	 * @return Number of events
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public long countEventsLastWeek() throws ExternalServiceException;


	public Map<Integer, Long> countEventsTodayPerHour() throws ExternalServiceException;


//	public Long[] getEventIdBoundariesForTimeframe(LocalDateTime from, LocalDateTime to) throws ExternalServiceException;


//	public List<Event> getEventsInIdRange(long lowerId, long upperId) throws ExternalServiceException;


	public List<Event> getEventPage(LocalDateTime start, LocalDateTime end, int pageId) throws ExternalServiceException;
}
