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
package de.decoit.siemgui.ext.icinga.dbconnector.services;

import de.decoit.siemgui.ext.icinga.dbconnector.entities.LogEntryEntity;
import de.decoit.siemgui.ext.icinga.dbconnector.repositories.LogEntryJpaRepository;
import java.sql.SQLDataException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static de.decoit.siemgui.ext.icinga.dbconnector.entities.specifications.LogEntryEntitySpecifications.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
@Transactional(value = "icingaTransactionManager")
public class IcingaDbConnector {
	private final Logger LOG = LoggerFactory.getLogger(IcingaDbConnector.class);

	@Autowired
	private LogEntryJpaRepository logEntryRepo;


	public LogEntryEntity getLogEntryById(long id) {
		return logEntryRepo.findOne(id);
	}


	public List<LogEntryEntity> getLogEntriesInTimeframe(LocalDateTime fromDate, LocalDateTime toDate) {
		return logEntryRepo.findAll(eventsInTimeframe(Timestamp.valueOf(fromDate), Timestamp.valueOf(toDate)), sortByDateDesc());
	}


	public List<LogEntryEntity> getPagedLogEntriesInTimeframe(LocalDateTime fromDate, LocalDateTime toDate, int pageId, int pageSize) {
		Page requestedPage = logEntryRepo.findAll(eventsInTimeframe(Timestamp.valueOf(fromDate), Timestamp.valueOf(toDate)), constructPageSpecification(pageId, pageSize));

		return requestedPage.getContent();
	}


	public long countLogEntriesInTimeframe(LocalDateTime fromDate, LocalDateTime toDate) {
		return logEntryRepo.countInTimeframe(Timestamp.valueOf(fromDate), Timestamp.valueOf(toDate));
	}


	public Map<Integer, Long> countLogEntriesPerHourInTimeframe(LocalDateTime fromDate, LocalDateTime toDate) {
		List<Object[]> resultList = logEntryRepo.countPerHourInTimeframe(Timestamp.valueOf(fromDate), Timestamp.valueOf(toDate));

		Map<Integer, Long> countMap = new HashMap<>();
		for(int i=0; i<24; i++) {
			countMap.put(i, 0L);
		}

		for(Object[] obj : resultList) {
			Integer hour = (Integer) obj[0];
			Long count = (Long) obj[1];

			countMap.put(hour, count);
		}

		return countMap;
	}


	public Long[] getLogEntryIdBoundariesInTimeframe(LocalDateTime fromDate, LocalDateTime toDate) throws SQLDataException {
		List<Object[]> resultList = logEntryRepo.getIdBoundariesInTimeframe(Timestamp.valueOf(fromDate), Timestamp.valueOf(toDate));
		Long[] result = new Long[3];

		if(resultList.size() == 1 && resultList.get(0).length == 3) {
			Object[] objects = resultList.get(0);

			for(int i=0; i<objects.length; i++) {
				if(objects[i] instanceof Long) {
					result[i] = (Long) objects[i];
				}
				else {
					throw new SQLDataException("Non-Long element returned for boundaries query");
				}
			}
		}
		else {
			throw new SQLDataException("Wrong number of results returned for boundaries query");
		}


		if(LOG.isDebugEnabled()) {
			LOG.debug("Boundaries query result: " + Arrays.toString(result));
			LOG.debug("Result size: " + result.length);
		}

		return result;
	}


	public List<LogEntryEntity> getLogEntriesInIdRange(long lowerId, long upperId) {
		return logEntryRepo.findInIdRange(lowerId, upperId);
	}


	private Pageable constructPageSpecification(int pageIndex, int pageSize) {
		Pageable page = new PageRequest(pageIndex, pageSize, sortByDateDesc());

		return page;
	}


	private Sort sortByDateDesc() {
		return new Sort(Sort.Direction.DESC, "timestamp");
	}
}
