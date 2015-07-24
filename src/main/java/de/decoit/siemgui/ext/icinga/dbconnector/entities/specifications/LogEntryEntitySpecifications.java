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
package de.decoit.siemgui.ext.icinga.dbconnector.entities.specifications;

import de.decoit.siemgui.ext.icinga.dbconnector.entities.LogEntryEntity;
import de.decoit.siemgui.ext.icinga.dbconnector.entities.LogEntryEntity_;
import java.sql.Timestamp;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class LogEntryEntitySpecifications {
	private static Logger LOG = LoggerFactory.getLogger(LogEntryEntitySpecifications.class.getName());

	public static Specification<LogEntryEntity> eventsInTimeframe(Timestamp fromDate, Timestamp toDate) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("(eventsInTimeframe) From date: " + fromDate.toString());
			LOG.debug("(eventsInTimeframe) To date: " + toDate.toString());
		}

		return new Specification<LogEntryEntity>() {
			@Override
			public Predicate toPredicate(Root<LogEntryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate greaterThanDate = cb.greaterThan(root.<Timestamp>get(LogEntryEntity_.timestamp), fromDate);
				Predicate lessThanDate = cb.lessThan(root.<Timestamp>get(LogEntryEntity_.timestamp), toDate);
				Predicate andDates = cb.and(greaterThanDate, lessThanDate);
				Predicate notLike = cb.notLike(root.<String>get(LogEntryEntity_.data), "EXTERNAL COMMAND:%");

				return cb.and(andDates, notLike);
			}
		};
	}
}
