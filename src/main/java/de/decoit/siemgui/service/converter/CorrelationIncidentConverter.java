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

import de.decoit.siemgui.domain.incidents.Incident;
import de.decoit.siemgui.domain.incidents.IncidentExplanation;
import de.decoit.siemgui.domain.incidents.IncidentRecommendation;
import de.decoit.siemgui.domain.incidents.IncidentStatus;
import de.decoit.siemgui.exception.ConversionException;
import java.util.List;


/**
 * This interface has to be implemented by all classed that realize conversion from and to correlation domain objects.
 * It is defined as a generic interface to leave definition of correlation implementation specific types to the implementing class.
 *
 * @author Thomas Rix (rix@decoit.de)
 * @param <INCIDENT> Implementation specific incident type
 * @param <STATUS>   Implementation specific incident status type
 * @param <RECOMMENDATION> Implementation specific incident recommendation type
 * @param <EXPLANATION> Implementation specific incident explanation type
 */
public interface CorrelationIncidentConverter<INCIDENT, STATUS, RECOMMENDATION, EXPLANATION> {
	/**
	 * Convert an implementation specific incident object to a general Incident domain object.
	 *
	 * @param inc Input object
	 * @return Created domain object
	 *
	 * @throws ConversionException if an error occurs during conversion
	 */
	public Incident convertIncident(INCIDENT inc) throws ConversionException;


	public List<Incident> convertIncidentList(List<INCIDENT> incList) throws ConversionException;


	/**
	 * Convert an implementation specific incident status object to a general IncidentStatus enum.
	 *
	 * @param status Input incident status
	 * @return Converted incident status
	 */
	public IncidentStatus convertIncidentStatus(STATUS status) throws ConversionException;


	/**
	 * Convert a general IncidentStatus enum to an implementation specific enum.
	 *
	 * @param status Input enum
	 * @return Converted enum
	 */
	public STATUS convertDomIncidentStatus(IncidentStatus status) throws ConversionException;


	/**
	 * Convert an implementation specific recommendation object to a general IncidentRecommendation domain object.
	 *
	 * @param rec Input object
	 * @return Created domain object
	 * @throws ConversionException if an error occurs during conversion
	 */
	public IncidentRecommendation convertRecommendation(RECOMMENDATION rec) throws ConversionException;


	public List<IncidentRecommendation> convertRecommendationList(List<RECOMMENDATION> recList) throws ConversionException;


	public IncidentExplanation convertExplanation(EXPLANATION expl) throws ConversionException;


	public List<IncidentExplanation> convertExplanationList(List<EXPLANATION> explList) throws ConversionException;
}
