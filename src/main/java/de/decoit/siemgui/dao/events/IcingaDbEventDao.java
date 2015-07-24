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

import de.decoit.siemgui.config.RootConfig.SystemConfig;
import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.exception.ExternalServiceException;
import de.decoit.siemgui.service.converter.IcingaEventManagementConverter;
import de.decoit.siemgui.ext.icinga.dbconnector.entities.LogEntryEntity;
import de.decoit.siemgui.ext.icinga.dbconnector.services.IcingaDbConnector;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Repository
public class IcingaDbEventDao implements EventDao {
	private final Logger LOG = LoggerFactory.getLogger(IcingaDbEventDao.class.getName());

	@Autowired
	private IcingaDbConnector icingaConn;

	@Autowired
	private IcingaEventManagementConverter eventObjConv;

	@Autowired
	private SystemConfig sysConf;


	@Override
	public long countEventsInTimeframe(LocalDateTime start, LocalDateTime end) throws ExternalServiceException {
		long c = icingaConn.countLogEntriesInTimeframe(start, end);

		if(LOG.isDebugEnabled()) {
			LOG.debug("(COUNT) Event timeframe begin: " + start.toString());
			LOG.debug("(COUNT) Event timeframe end: " + end.toString());
			LOG.debug("(COUNT) Event count: " + c);
		}

		return c;
	}


	@Override
	public List<Event> getEventsInTimeframe(LocalDateTime start, LocalDateTime end) throws ExternalServiceException {
		List<LogEntryEntity> entryList = icingaConn.getLogEntriesInTimeframe(start, end);

		if(LOG.isDebugEnabled()) {
			LOG.debug("(GET) Event timeframe begin: " + start.toString());
			LOG.debug("(GET) Event timeframe end: " + end.toString());
			LOG.debug("(GET) Event count: " + entryList.size());
		}

		return eventObjConv.convertEventList(entryList);
	}


	@Override
	public List<Event> getPagedEventsInTimeframe(LocalDateTime start, LocalDateTime end, int pageId) throws ExternalServiceException {
		List<LogEntryEntity> entryList = icingaConn.getPagedLogEntriesInTimeframe(start, end, pageId, sysConf.getEventsPageSize());

		if(LOG.isDebugEnabled()) {
			LOG.debug("(GET PAGED) Event timeframe begin: " + start.toString());
			LOG.debug("(GET PAGED) Event timeframe end: " + end.toString());
			LOG.debug("(GET PAGED) Event page ID: " + pageId);
			LOG.debug("(GET PAGED) Event count: " + entryList.size());
		}

		return eventObjConv.convertEventList(entryList);
	}


	@Override
	public Map<Integer, Long> countEventsPerHourInTimeframe(LocalDateTime start, LocalDateTime end) throws ExternalServiceException {
		Map<Integer, Long> resultMap = icingaConn.countLogEntriesPerHourInTimeframe(start, end);

		if(LOG.isDebugEnabled()) {
			LOG.debug("(COUNT) Event per hour map: " + resultMap);
		}

		return resultMap;
	}
}
