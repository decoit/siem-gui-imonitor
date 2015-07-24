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
package de.decoit.siemgui.service.converter;

import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.exception.ConversionException;
import java.util.List;


/**
 * This interface has to be implemented by all classed that realize conversion from and to event management domain objects.
 * It is defined as a generic interface to leave definition of correlation implementation specific types to the implementing class.
 *
 * @author Thomas Rix (rix@decoit.de)
 * @param <EVENT> Implementation specific event type
 */
public interface EventManagementConverter<EVENT> {
	/**
	 * Convert an implementation specific event object into a general Event domain object.
	 *
	 * @param evt Input object
	 * @return The created domain object
	 *
	 * @throws ConversionException if an error occurs during conversion
	 */
	public Event convertEvent(EVENT evt) throws ConversionException;


	/**
	 * Convert a list of implementation specific event object into a list of general Event domain object.
	 * For conversion rules and restrictions please read the documentation of convertEvent().
	 *
	 * @param evtList List of input objects
	 * @return List of created domain objects
	 *
	 * @throws ConversionException if an error occurs during conversion
	 */
	public List<Event> convertEventList(List<EVENT> evtList) throws ConversionException;
}
