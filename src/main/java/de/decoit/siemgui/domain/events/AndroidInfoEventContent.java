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
public class AndroidInfoEventContent {
	@JsonProperty("mac")
	private String macAddress;
	@JsonProperty("imei")
	private String imei;
	@JsonProperty("imsi")
	private String imsi;
	@JsonProperty("kernel")
	private String kernelVersion;
	@JsonProperty("firmware")
	private String firmwareVersion;
	@JsonProperty("root")
	private boolean rooted;
	@JsonProperty("selinux")
	private String selinux;
	@JsonProperty("baseband")
	private String basebandVersion;
	@JsonProperty("build")
	private String buildNumber;
	@JsonProperty("brand")
	private String brand;
	@JsonProperty("manufacturer")
	private String manufacturer;
	@JsonProperty("cellnumber")
	private String cellnumber;
}
