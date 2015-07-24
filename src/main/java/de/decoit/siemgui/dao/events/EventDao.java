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
package de.decoit.siemgui.dao.events;

import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.exception.ExternalServiceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * Interface definition for accessing the event database.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public interface EventDao {
	/**
	 * Fetch all events in the specified timeframe and return them as an ordered list.
	 * The list is sorted chronologically with the newest event first. The maximum precision for
	 * the timeframe is seconds. Any smaller chrono unit will be truncated.
	 *
	 * @param start Start of the timeframe
	 * @param end   End of the timeframe
	 * @return Ordered list of events
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public List<Event> getEventsInTimeframe(LocalDateTime start, LocalDateTime end) throws ExternalServiceException;


	/**
	 * Fetch paginated events in the specified timeframe on page number pageId.
	 * The list is sorted chronologically with the newest event first. The maximum precision for
	 * the timeframe is seconds. Any smaller chrono unit will be truncated.
	 *
	 * @param start Start of the timeframe
	 * @param end End of the timeframe
	 * @param pageId Page number
	 * @return Ordered list of events on requested page
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public List<Event> getPagedEventsInTimeframe(LocalDateTime start, LocalDateTime end, int pageId) throws ExternalServiceException;


	/**
	 * Count all events in the specified timeframe.
	 * The maximum precision for the timeframe is seconds. Any smaller chrono unit will be truncated.
	 *
	 * @param start Start of the timeframe
	 * @param end   End of the timeframe
	 * @return Number of events
	 *
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public long countEventsInTimeframe(LocalDateTime start, LocalDateTime end) throws ExternalServiceException;


	/**
	 * Count the number of events in the specified timeframe and return a mapping from hour to number of events.
	 * The maximum precision for the timeframe is seconds. Any smaller chrono unit will be truncated.
	 *
	 * @param start Start of the timeframe
	 * @param end End of the timeframe
	 * @return Mapping of hour to number of events
	 * @throws ExternalServiceException if an error occurs while accessing the external service
	 */
	public Map<Integer, Long> countEventsPerHourInTimeframe(LocalDateTime start, LocalDateTime end) throws ExternalServiceException;
}
