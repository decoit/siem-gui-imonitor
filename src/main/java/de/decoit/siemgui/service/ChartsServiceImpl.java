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
import de.decoit.siemgui.domain.incidents.Incident;
import de.decoit.siemgui.domain.incidents.ThreatLevel;
import de.decoit.siemgui.domain.charts.AxisType;
import de.decoit.siemgui.domain.charts.ChartType;
import de.decoit.siemgui.domain.charts.SimpleChartSeries;
import de.decoit.siemgui.domain.charts.XAxisDefinition;
import de.decoit.siemgui.domain.charts.XYChartData;
import de.decoit.siemgui.domain.charts.XYChartSeries;
import de.decoit.siemgui.domain.charts.YAxisDefinition;
import de.decoit.siemgui.domain.events.EventType;
import de.decoit.siemgui.exception.ExternalServiceException;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgChart;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of ChartsService that uses the chart domain objects defined in the package de.decoit.siemgui.domain.charts.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
public class ChartsServiceImpl implements ChartsService {
	@Autowired
	private EventService eventServ;

	@Autowired
	private IncidentService incServ;


	@Override
	public MsgChart getEventsTodayChart(int chartIndex) throws ExternalServiceException {
//		List<Event> evtList = eventServ.getEventsToday();
		Map<Integer, Long> evtMap = eventServ.countEventsTodayPerHour();
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

		List<XYChartSeries<Long>> series = new ArrayList<>();
		XYChartSeries<Long> s1 = new XYChartSeries<>("Ereignisse");
		List<XYChartData<Long>> s1data = new ArrayList<>();

		for (int i = 0; i < 24; i++) {
			long count = evtMap.get(i);

			// Calculate timestamp for X value, highcharts needs timestamp in milliseconds thus seconds are multiplied by 1000
			long timestamp = now.withHour(i).toEpochSecond(ZoneOffset.UTC) * 1000;

			// Add the calculated values to the data list
			s1data.add(new XYChartData<>(timestamp, count));
		}

		// Add data to the series
		s1.setData(s1data);

		// Add the series s1 to the list of series
		series.add(s1);

		// Create axis definitions
		XAxisDefinition xAxis = new XAxisDefinition(AxisType.DATETIME, "Uhrzeit");
		YAxisDefinition yAxis = new YAxisDefinition(AxisType.LINEAR, "Ereignisse");

		// Create the chart message
		MsgChart msg = new MsgChart(chartIndex, ChartType.COLUMN, xAxis, yAxis);
		msg.setTitle("Heutige Ereignisse nach Uhrzeit");
		msg.setSeries(series);

		return msg;
	}


	@Override
	public MsgChart getEventsTodayPerSensorChart(int chartIndex) throws ExternalServiceException {
		List<Event> evtList = eventServ.getEventsToday();
		Map<EventType, List<Event>> eventsBySensor = evtList.stream().collect(Collectors.groupingBy(Event::getType));
		List<String> categories = new ArrayList<>();

		List<SimpleChartSeries<Long>> series = new ArrayList<>();
		SimpleChartSeries<Long> s1 = new SimpleChartSeries<>("Ereignisse");

		for (Map.Entry<EventType, List<Event>> e : eventsBySensor.entrySet()) {
			categories.add(e.getKey().toString());
			long count = (long) e.getValue().size();

			s1.addDataPoint(count);
		}

		// Add the series s1 to the list of series
		series.add(s1);

		// Create axis definitions
		XAxisDefinition xAxis = new XAxisDefinition(AxisType.CATEGORY, "Sensor");
		xAxis.setCategories(categories);

		YAxisDefinition yAxis = new YAxisDefinition(AxisType.LINEAR, "Ereignisse");

		// Create the chart message
		MsgChart msg = new MsgChart(chartIndex, ChartType.BAR, xAxis, yAxis);
		msg.setTitle("Heutige Ereignisse nach Sensor");
		msg.setSeries(series);

		return msg;
	}


	@Override
	public MsgChart getActiveIncidentsByThreatLevel(int chartIndex) throws ExternalServiceException {
		List<Incident> incList = incServ.getActiveIncidents();
		Map<ThreatLevel, List<Incident>> incidentsByThreatLevel = incList.stream().collect(Collectors.groupingBy(Incident::getThreatLevel));
		List<String> categories = new ArrayList<>();

		List<SimpleChartSeries<Long>> series = new ArrayList<>();
		SimpleChartSeries<Long> s1 = new SimpleChartSeries<>("Vorfälle");

		long lowCount = 0;
		if(incidentsByThreatLevel.containsKey(ThreatLevel.LOW)) {
			lowCount = incidentsByThreatLevel.get(ThreatLevel.LOW).size();
		}

		long mediumCount = 0;
		if(incidentsByThreatLevel.containsKey(ThreatLevel.MEDIUM)) {
			mediumCount = incidentsByThreatLevel.get(ThreatLevel.MEDIUM).size();
		}

		long highCount = 0;
		if(incidentsByThreatLevel.containsKey(ThreatLevel.HIGH)) {
			highCount = incidentsByThreatLevel.get(ThreatLevel.HIGH).size();
		}

		categories.add(ThreatLevel.HIGH.toString());
		s1.addDataPoint(highCount);

		categories.add(ThreatLevel.MEDIUM.toString());
		s1.addDataPoint(mediumCount);

		categories.add(ThreatLevel.LOW.toString());
		s1.addDataPoint(lowCount);

		// Add the series s1 to the list of series
		series.add(s1);

		// Create axis definitions
		XAxisDefinition xAxis = new XAxisDefinition(AxisType.CATEGORY, "Bedrohungsstufe");
		xAxis.setCategories(categories);

		YAxisDefinition yAxis = new YAxisDefinition(AxisType.LINEAR, "Vorfälle");

		// Create the chart message
		MsgChart msg = new MsgChart(chartIndex, ChartType.BAR, xAxis, yAxis);
		msg.setTitle("Aktive Vorfälle nach Bedrohungsstufe");
		msg.setSeries(series);

		return msg;
	}
}
