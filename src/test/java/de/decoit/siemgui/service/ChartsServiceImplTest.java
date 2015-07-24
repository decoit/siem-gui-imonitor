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

import de.decoit.siemgui.service.ChartsServiceImpl;
import de.decoit.siemgui.service.EventService;
import de.decoit.siemgui.service.IncidentService;
import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.domain.events.GeneralEvent;
import de.decoit.siemgui.domain.incidents.Incident;
import de.decoit.siemgui.domain.events.SnortEvent;
import de.decoit.siemgui.domain.incidents.ThreatLevel;
import de.decoit.siemgui.domain.charts.SimpleChartSeries;
import de.decoit.siemgui.domain.charts.XYChartData;
import de.decoit.siemgui.domain.charts.XYChartSeries;
import de.decoit.siemgui.domain.events.SnortEventContent;
import de.decoit.siemgui.exception.ExternalServiceException;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgChart;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
public class ChartsServiceImplTest {
	@Mock
	private EventService eventServMock;

	@Mock
	private IncidentService incServMock;

	@InjectMocks
	private ChartsServiceImpl service;

	LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);


	@Before
	public void setUp() throws ExternalServiceException {
		List<Event> evtList = new ArrayList<>();
		for(int i=0; i<24; i++) {
			if(i % 2 == 0) {
				GeneralEvent e = new GeneralEvent();
				e.setIcingaTimestamp(now.withHour(i));
				e.setData("Even hour test event " + i);

				evtList.add(e);

				SnortEvent e1 = new SnortEvent();
				e1.setIcingaTimestamp(now.withHour(i).withMinute(30));

				SnortEventContent sec = new SnortEventContent();
				e1.setData(sec);

				evtList.add(e1);
			}
			else {
				GeneralEvent e = new GeneralEvent();
				e.setIcingaTimestamp(now.withHour(i));
				e.setData("Odd hour test event " + i);

				evtList.add(e);
			}
		}
		when(eventServMock.getEventsToday()).thenReturn(evtList);

		List<Incident> incList = new ArrayList<>();
		for(int i=0; i<15; i++) {
			Incident inc = new Incident();

			if(i < 5) {
				// 5 low risk
				inc.setThreatLevel(ThreatLevel.LOW);
			}
			else if(i < 12) {
				// 7 medium risk
				inc.setThreatLevel(ThreatLevel.MEDIUM);
			}
			else {
				// 3 high risk
				inc.setThreatLevel(ThreatLevel.HIGH);
			}

			incList.add(inc);
		}
		when(incServMock.getActiveIncidents()).thenReturn(incList);
	}


	@After
	public void tearDown() {
		reset(eventServMock);
		reset(incServMock);
	}


	/**
	 * Test of getEventsTodayChart method, of class ChartsServiceImpl.
	 */
//	@Test
//	public void testGetEventsTodayChart() throws Exception {
//		System.out.println("ChartsServiceImpl.getEventsTodayChart()");
//
//		int chartIndex = 0;
//		MsgChart result = service.getEventsTodayChart(chartIndex);
//
//		assertEquals("Chart index mismatch", 0, result.getChartIndex());
//
//		XYChartSeries<Long> xycs = (XYChartSeries<Long>) result.getSeries().get(0);
//		List<XYChartData<Long>> dataList = xycs.getData();
//
//		assertEquals("Chart data list size mismatch", 24, dataList.size());
//
//		for(XYChartData<Long> cd : dataList) {
//			LocalDateTime time = LocalDateTime.ofEpochSecond(cd.getX() / 1000, 0, ZoneOffset.UTC);
//
//			if(time.getHour() % 2 == 0) {
//				assertEquals("Event count per even hour mismatch", Long.valueOf(2), cd.getY());
//			}
//			else {
//				assertEquals("Event count per odd hour mismatch", Long.valueOf(1), cd.getY());
//			}
//		}
//	}


	/**
	 * Test of getEventsTodayPerSensorChart method, of class ChartsServiceImpl.
	 */
	@Test
	public void testGetEventsTodayPerSensorChart() throws Exception {
		System.out.println("ChartsServiceImpl.getEventsTodayPerSensorChart()");

		int chartIndex = 0;
		MsgChart result = service.getEventsTodayPerSensorChart(chartIndex);

		assertEquals("Chart index mismatch", 0, result.getChartIndex());

		SimpleChartSeries<Long> scs = (SimpleChartSeries<Long>) result.getSeries().get(0);
		List<Long> dataList = scs.getData();

		assertEquals("Chart data list size mismatch", 2, dataList.size());

		List<String> cats = result.getXAxis().getCategories();

		int snortIndex = cats.indexOf("SNORT");
		int generalIndex = cats.indexOf("GENERAL");
		assertEquals(Long.valueOf(12), dataList.get(snortIndex));
		assertEquals(Long.valueOf(24), dataList.get(generalIndex));
	}


	/**
	 * Test of getActiveIncidentsByThreatLevel method, of class ChartsServiceImpl.
	 */
	@Test
	public void testGetActiveIncidentsByThreatLevel() throws Exception {
		System.out.println("ChartsServiceImpl.getActiveIncidentsByThreatLevel()");

		int chartIndex = 0;
		MsgChart result = service.getActiveIncidentsByThreatLevel(chartIndex);

		assertEquals("Chart index mismatch", 0, result.getChartIndex());

		SimpleChartSeries<Long> scs = (SimpleChartSeries<Long>) result.getSeries().get(0);
		List<Long> dataList = scs.getData();

		assertEquals("Chart data list size mismatch", 3, dataList.size());

		List<String> cats = result.getXAxis().getCategories();

		int lowIndex = cats.indexOf("LOW");
		int medIndex = cats.indexOf("MEDIUM");
		int highIndex = cats.indexOf("HIGH");
		assertEquals(Long.valueOf(5), dataList.get(lowIndex));
		assertEquals(Long.valueOf(7), dataList.get(medIndex));
		assertEquals(Long.valueOf(3), dataList.get(highIndex));
	}
}
