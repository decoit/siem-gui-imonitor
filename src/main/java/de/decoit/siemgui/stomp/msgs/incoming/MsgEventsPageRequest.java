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
 * An incoming message to request a list of events.
 * Returned events are limited by the included timestamps which must be set. Requesting all events is not possible.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Getter
@ToString
@EqualsAndHashCode
public class MsgEventsPageRequest {
	private final String fromDate;
	private final String toDate;
	private final int pageIndex;


	@JsonCreator
	public MsgEventsPageRequest(@JsonProperty("from") String fromDate, @JsonProperty("to") String toDate, @JsonProperty("pageIndex") int pageIndex) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.pageIndex = pageIndex;
	}
}
