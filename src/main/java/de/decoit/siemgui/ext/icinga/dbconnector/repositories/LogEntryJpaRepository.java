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
package de.decoit.siemgui.ext.icinga.dbconnector.repositories;

import de.decoit.siemgui.ext.icinga.dbconnector.entities.LogEntryEntity;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Repository
public interface LogEntryJpaRepository extends JpaRepository<LogEntryEntity, Long>, JpaSpecificationExecutor<LogEntryEntity> {

	@Query("SELECT l FROM LogEntryEntity l WHERE l.id >= :lower AND l.id <= :upper AND l.data NOT LIKE 'EXTERNAL COMMAND:%' ORDER BY l.timestamp ASC, l.id ASC")
	public List<LogEntryEntity> findInIdRange(@Param("lower") long low, @Param("upper") long upper);


	@Query("SELECT COUNT(l) FROM LogEntryEntity l WHERE l.timestamp >= :fromDate AND l.timestamp <= :toDate AND l.data NOT LIKE 'EXTERNAL COMMAND:%'")
	public long countInTimeframe(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);


	@Query("SELECT HOUR(l.timestamp), COUNT(l) FROM LogEntryEntity l WHERE l.timestamp >= :fromDate AND l.timestamp <= :toDate AND l.data NOT LIKE 'EXTERNAL COMMAND:%' GROUP BY HOUR(l.timestamp) ORDER BY HOUR(l.timestamp)")
	public List<Object[]> countPerHourInTimeframe(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);


	@Query("SELECT MIN(l.id), MAX(l.id), COUNT(l) FROM LogEntryEntity l WHERE l.timestamp >= :fromDate AND l.timestamp <= :toDate AND l.data NOT LIKE 'EXTERNAL COMMAND:%'")
	public List<Object[]> getIdBoundariesInTimeframe(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);
}
