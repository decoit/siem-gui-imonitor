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

import de.decoit.siemgui.ext.icinga.dbconnector.entities.LogEntryEntity;
import java.sql.Timestamp;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@StaticMetamodel(LogEntryEntity.class)
public class LogEntryEntity_ {
	public static volatile SingularAttribute<LogEntryEntity, Long> id;
	public static volatile SingularAttribute<LogEntryEntity, Timestamp> timestamp;
	public static volatile SingularAttribute<LogEntryEntity, String> data;
}
