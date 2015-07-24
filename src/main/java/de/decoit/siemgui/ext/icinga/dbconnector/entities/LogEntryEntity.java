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
package de.decoit.siemgui.ext.icinga.dbconnector.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Entity
@Table(name="icinga_logentries")
@Data
@EqualsAndHashCode(of = { "id" })
public class LogEntryEntity {
	@Id
	@Column(name="logentry_id")
	private long id;

	@Column(name="logentry_time")
	private Timestamp timestamp;

	@Lob
	@Column(name="logentry_data", length=65535, columnDefinition="text")
	private String data;
}
