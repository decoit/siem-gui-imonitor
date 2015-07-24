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

import de.decoit.siemgui.dao.events.EventDao;
import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.exception.ExternalServiceException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * An implementation of EventService that uses Icinga as the event management system.
 * It accesses Icinga by using an instance of EventDao.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
public class IcingaEventService implements EventService {
	@Autowired
	EventDao eventDao;


	@Override
	public long countEventsInTimeframe(LocalDateTime from, LocalDateTime to) throws ExternalServiceException {
		return eventDao.countEventsInTimeframe(from, to);
	}


	@Override
	public long countEventsLastWeek() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusWeeks(1);

		int dayOfWeek = baseDate.getDayOfWeek().getValue();

		// We have to substract this number of days from today to get monday
		int weekStartOffset = dayOfWeek - 1;

		// We have to add this number of days to today to get sunday
		int weekEndOffset = 7 - dayOfWeek;

		LocalDateTime weekStart = baseDate.withHour(0).withMinute(0).withSecond(0).minusDays(weekStartOffset);
		LocalDateTime weekEnd = baseDate.withHour(23).withMinute(59).withSecond(59).plusDays(weekEndOffset);

		long eventCount = eventDao.countEventsInTimeframe(weekStart, weekEnd);

		return eventCount;
	}


	@Override
	public long countEventsThisWeek() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		int dayOfWeek = baseDate.getDayOfWeek().getValue();

		// We have to substract this number of days from today to get monday
		int weekStartOffset = dayOfWeek - 1;

		// We have to add this number of days to today to get sunday
		int weekEndOffset = 7 - dayOfWeek;

		LocalDateTime weekStart = baseDate.withHour(0).withMinute(0).withSecond(0).minusDays(weekStartOffset);
		LocalDateTime weekEnd = baseDate.withHour(23).withMinute(59).withSecond(59).plusDays(weekEndOffset);

		long eventCount = eventDao.countEventsInTimeframe(weekStart, weekEnd);

		return eventCount;
	}


	@Override
	public long countEventsToday() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		LocalDateTime todayStart = baseDate.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime todayEnd = baseDate.withHour(23).withMinute(59).withSecond(59);

		long eventCount = eventDao.countEventsInTimeframe(todayStart, todayEnd);

		return eventCount;
	}


	@Override
	public long countEventsYesterday() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		LocalDateTime yesterdayStart = baseDate.withHour(0).withMinute(0).withSecond(0).minusDays(1);
		LocalDateTime yesterdayEnd = baseDate.withHour(23).withMinute(59).withSecond(59).minusDays(1);

		long eventCount = eventDao.countEventsInTimeframe(yesterdayStart, yesterdayEnd);

		return eventCount;
	}


	@Override
	public List<Event> getEventsLastWeek() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusWeeks(1);

		int dayOfWeek = baseDate.getDayOfWeek().getValue();

		// We have to substract this number of days from today to get monday
		int weekStartOffset = dayOfWeek - 1;

		// We have to add this number of days to today to get sunday
		int weekEndOffset = 7 - dayOfWeek;

		LocalDateTime weekStart = baseDate.withHour(0).withMinute(0).withSecond(0).minusDays(weekStartOffset);
		LocalDateTime weekEnd = baseDate.withHour(23).withMinute(59).withSecond(59).plusDays(weekEndOffset);

		List<Event> eventsThisWeek = eventDao.getEventsInTimeframe(weekStart, weekEnd);

		return eventsThisWeek;
	}


	@Override
	public List<Event> getEventsThisWeek() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		int dayOfWeek = baseDate.getDayOfWeek().getValue();

		// We have to substract this number of days from today to get monday
		int weekStartOffset = dayOfWeek - 1;

		// We have to add this number of days to today to get sunday
		int weekEndOffset = 7 - dayOfWeek;

		LocalDateTime weekStart = baseDate.withHour(0).withMinute(0).withSecond(0).minusDays(weekStartOffset);
		LocalDateTime weekEnd = baseDate.withHour(23).withMinute(59).withSecond(59).plusDays(weekEndOffset);

		List<Event> eventsThisWeek = eventDao.getEventsInTimeframe(weekStart, weekEnd);

		return eventsThisWeek;
	}


	@Override
	public List<Event> getEventsToday() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		LocalDateTime todayStart = baseDate.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime todayEnd = baseDate.withHour(23).withMinute(59).withSecond(59);

		List<Event> eventsToday = eventDao.getEventsInTimeframe(todayStart, todayEnd);

		return eventsToday;
	}


	@Override
	public List<Event> getEventsYesterday() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		LocalDateTime yesterdayStart = baseDate.withHour(0).withMinute(0).withSecond(0).minusDays(1);
		LocalDateTime yesterdayEnd = baseDate.withHour(23).withMinute(59).withSecond(59).minusDays(1);

		List<Event> eventsYesterday = eventDao.getEventsInTimeframe(yesterdayStart, yesterdayEnd);

		return eventsYesterday;
	}


	@Override
	public List<Event> getEventsInTimeframe(LocalDateTime from, LocalDateTime to) throws ExternalServiceException {
		List<Event> eventsList = eventDao.getEventsInTimeframe(from, to);

		return eventsList;
	}


	@Override
	public Map<Integer, Long> countEventsTodayPerHour() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		LocalDateTime todayStart = baseDate.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime todayEnd = baseDate.withHour(23).withMinute(59).withSecond(59);

		Map<Integer, Long> eventMap = eventDao.countEventsPerHourInTimeframe(todayStart, todayEnd);

		return eventMap;
	}


//	@Override
//	public Long[] getEventIdBoundariesForTimeframe(LocalDateTime from, LocalDateTime to) throws ExternalServiceException {
//		return eventDao.getEventIdBoundariesForTimeframe(from, to);
//	}


//	@Override
//	public List<Event> getEventsInIdRange(long lowerId, long upperId) throws ExternalServiceException {
//		return eventDao.getEventsInIdRange(lowerId, upperId);
//	}


	@Override
	public List<Event> getEventPage(LocalDateTime start, LocalDateTime end, int pageId) throws ExternalServiceException {
		return eventDao.getPagedEventsInTimeframe(start, end, pageId);
	}
}
