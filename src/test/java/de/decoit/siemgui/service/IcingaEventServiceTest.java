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

import de.decoit.siemgui.service.IcingaEventService;
import de.decoit.siemgui.dao.events.EventDao;
import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.domain.events.GeneralEvent;
import de.decoit.siemgui.exception.ExternalServiceException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@RunWith(MockitoJUnitRunner.class)
public class IcingaEventServiceTest {
	@Mock
	EventDao eventDaoMock;

	@InjectMocks
	IcingaEventService service;


	@Before
	public void setUp() throws ExternalServiceException {
		LocalDateTime baseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		int dayOfWeek = baseDate.getDayOfWeek().getValue();
		// We have to substract this number of days from today to get monday
		int weekStartOffset = dayOfWeek - 1;
		// We have to add this number of days to today to get sunday
		int weekEndOffset = 7 - dayOfWeek;

		LocalDateTime thisWeekStart = baseDate.withHour(0).withMinute(0).withSecond(0).minusDays(weekStartOffset);
		LocalDateTime thisWeekEnd = baseDate.withHour(23).withMinute(59).withSecond(59).plusDays(weekEndOffset);

		LocalDateTime lastWeekStart = thisWeekStart.minusWeeks(1);
		LocalDateTime lastWeekEnd = thisWeekEnd.minusWeeks(1);

		LocalDateTime todayStart = baseDate.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime todayEnd = baseDate.withHour(23).withMinute(59).withSecond(59);

		LocalDateTime yesterdayStart = todayStart.minusDays(1);
		LocalDateTime yesterdayEnd = todayEnd.minusDays(1);

		List<Event> lastWeekList = new ArrayList<>();
		for(int i=0; i<20; i++) {
			Event e = new GeneralEvent();
			e.setId(i);
			lastWeekList.add(e);
		}

		List<Event> thisWeekList = new ArrayList<>();
		for(int i=0; i<15; i++) {
			Event e = new GeneralEvent();
			e.setId(i);
			thisWeekList.add(e);
		}

		List<Event> yesterdayList = new ArrayList<>();
		for(int i=0; i<10; i++) {
			Event e = new GeneralEvent();
			e.setId(i);
			yesterdayList.add(e);
		}

		List<Event> todayList = new ArrayList<>();
		for(int i=0; i<5; i++) {
			Event e = new GeneralEvent();
			e.setId(i);
			todayList.add(e);
		}

		when(eventDaoMock.getEventsInTimeframe(lastWeekStart, lastWeekEnd)).thenReturn(lastWeekList);
		when(eventDaoMock.getEventsInTimeframe(thisWeekStart, thisWeekEnd)).thenReturn(thisWeekList);
		when(eventDaoMock.getEventsInTimeframe(yesterdayStart, yesterdayEnd)).thenReturn(yesterdayList);
		when(eventDaoMock.getEventsInTimeframe(todayStart, todayEnd)).thenReturn(todayList);

		List<Event> customList = new ArrayList<>();
		for(int i=0; i<13; i++) {
			Event e = new GeneralEvent();
			e.setId(i);
			customList.add(e);
		}
		when(eventDaoMock.getEventsInTimeframe(LocalDateTime.of(2014, Month.AUGUST, 21, 11, 0, 0), LocalDateTime.of(2014, Month.AUGUST, 21, 11, 59, 59))).thenReturn(customList);

		when(eventDaoMock.countEventsInTimeframe(lastWeekStart, lastWeekEnd)).thenReturn((long) lastWeekList.size());
		when(eventDaoMock.countEventsInTimeframe(thisWeekStart, thisWeekEnd)).thenReturn((long) thisWeekList.size());
		when(eventDaoMock.countEventsInTimeframe(yesterdayStart, yesterdayEnd)).thenReturn((long) yesterdayList.size());
		when(eventDaoMock.countEventsInTimeframe(todayStart, todayEnd)).thenReturn((long) todayList.size());
	}


	@After
	public void tearDown() {
		reset(eventDaoMock);
	}


	/**
	 * Test of countEventsLastWeek method, of class IcingaEventService.
	 */
	@Test
	public void testCountEventsLastWeek() throws Exception {
		System.out.println("IcingaEventService.countEventsLastWeek()");

		long expResult = 20L;
		long result = service.countEventsLastWeek();
		assertEquals(expResult, result);
	}


	/**
	 * Test of countEventsThisWeek method, of class IcingaEventService.
	 */
	@Test
	public void testCountEventsThisWeek() throws Exception {
		System.out.println("IcingaEventService.countEventsThisWeek()");

		long expResult = 15L;
		long result = service.countEventsThisWeek();
		assertEquals(expResult, result);
	}


	/**
	 * Test of countEventsToday method, of class IcingaEventService.
	 */
	@Test
	public void testCountEventsToday() throws Exception {
		System.out.println("IcingaEventService.countEventsToday()");

		long expResult = 5L;
		long result = service.countEventsToday();
		assertEquals(expResult, result);
	}


	/**
	 * Test of countEventsYesterday method, of class IcingaEventService.
	 */
	@Test
	public void testCountEventsYesterday() throws Exception {
		System.out.println("IcingaEventService.countEventsYesterday()");

		long expResult = 10L;
		long result = service.countEventsYesterday();
		assertEquals(expResult, result);
	}


	/**
	 * Test of getEventsLastWeek method, of class IcingaEventService.
	 */
	@Test
	public void testGetEventsLastWeek() throws Exception {
		System.out.println("IcingaEventService.getEventsLastWeek()");

		int expResult = 20;
		List<Event> result = service.getEventsLastWeek();

		assertEquals(expResult, result.size());
	}


	/**
	 * Test of getEventsThisWeek method, of class IcingaEventService.
	 */
	@Test
	public void testGetEventsThisWeek() throws Exception {
		System.out.println("IcingaEventService.getEventsThisWeek()");

		int expResult = 15;
		List<Event> result = service.getEventsThisWeek();

		assertEquals(expResult, result.size());
	}


	/**
	 * Test of getEventsToday method, of class IcingaEventService.
	 */
	@Test
	public void testGetEventsToday() throws Exception {
		System.out.println("IcingaEventService.getEventsToday()");

		int expResult = 5;
		List<Event> result = service.getEventsToday();

		assertEquals(expResult, result.size());
	}


	/**
	 * Test of getEventsYesterday method, of class IcingaEventService.
	 */
	@Test
	public void testGetEventsYesterday() throws Exception {
		System.out.println("IcingaEventService.getEventsYesterday()");

		int expResult = 10;
		List<Event> result = service.getEventsYesterday();

		assertEquals(expResult, result.size());
	}


	/**
	 * Test of getEventsInTimeframe method, of class IcingaEventService.
	 */
	@Test
	public void testGetEventsInTimeframe() throws Exception {
		System.out.println("IcingaEventService.getEventsInTimeframe()");

		LocalDateTime from = LocalDateTime.of(2014, Month.AUGUST, 21, 11, 0, 0);
		LocalDateTime to = LocalDateTime.of(2014, Month.AUGUST, 21, 11, 59, 59);

		int expResult = 13;
		List<Event> result = service.getEventsInTimeframe(from, to);
		assertEquals(expResult, result.size());
	}

}
