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
package de.decoit.siemgui.stomp.msgs.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Getter
@ToString
@EqualsAndHashCode
public class MsgUserEditRequest {
	private final long userId;
	private final boolean siemAuthorized;
	private final boolean siemAdminAuthorized;

	@JsonCreator
	public MsgUserEditRequest(@JsonProperty("userId") long userId, @JsonProperty("siemAuthorized") boolean siemAuthorized, @JsonProperty("siemAdminAuthorized") boolean siemAdminAuthorized) {
		this.userId = userId;
		this.siemAuthorized = siemAuthorized;
		this.siemAdminAuthorized = siemAdminAuthorized;
	}
}
