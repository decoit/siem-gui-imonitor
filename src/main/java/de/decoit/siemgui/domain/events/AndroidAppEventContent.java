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
package de.decoit.siemgui.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AndroidAppEventContent {
	@JsonProperty("name")
	private String applicationName;
	@JsonProperty("label")
	private String applicationLabel;
	@JsonProperty("version")
	private String version;
	@JsonProperty("running")
	private boolean running;
	@JsonProperty("installTime")
	private long installTime;
	@JsonProperty("updateTime")
	private long updateTime;
	@JsonProperty("permissions")
	private List<String> permissions;


	/**
	 * Return a LocalDateTime object representing the install timestamp.
	 * Time zone will be set to the default time zone of the server.
	 *
	 * @return Install time as LocalDateTime object
	 */
	@JsonProperty("installTimeObject")
	public LocalDateTime getInstallTimeObject() {
		Instant instant = Instant.ofEpochMilli(installTime);
		return ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault()).toLocalDateTime();
	}


	/**
	 * Return a LocalDateTime object representing the update timestamp.
	 * Time zone will be set to the default time zone of the server.
	 *
	 * @return Update time as LocalDateTime object
	 */
	@JsonProperty("updateTimeObject")
	public LocalDateTime getUpdateTimeObject() {
		Instant instant = Instant.ofEpochMilli(updateTime);
		return ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault()).toLocalDateTime();
	}
}
