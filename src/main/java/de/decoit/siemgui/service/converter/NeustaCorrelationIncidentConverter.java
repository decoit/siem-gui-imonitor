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

import de.decoit.siemgui.config.RootConfig.SystemConfig;
import de.decoit.siemgui.domain.incidents.Incident;
import de.decoit.siemgui.domain.incidents.IncidentExplanation;
import de.decoit.siemgui.domain.incidents.IncidentRecommendation;
import de.decoit.siemgui.domain.incidents.IncidentStatus;
import de.decoit.siemgui.domain.incidents.ThreatLevel;
import de.decoit.siemgui.exception.ConversionException;
import de.neusta.imonitor.correlation.persistency.entities.ExplanationEntity;
import de.neusta.imonitor.correlation.persistency.entities.IncidentEntity;
import de.neusta.imonitor.correlation.persistency.entities.RecommendationEntity;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * This implementation of CorrelationConverter can be used to convert IncidentEntity objects into the Incident domain object.
 * IncidentEntity objects are used by the neusta correlation, accessed via Hibernate.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
public class NeustaCorrelationIncidentConverter implements CorrelationIncidentConverter<IncidentEntity, de.neusta.imonitor.correlation.persistency.enums.IncidentStatus, RecommendationEntity, ExplanationEntity> {
	private final Logger LOG = LoggerFactory.getLogger(NeustaCorrelationIncidentConverter.class.getName());

	@Autowired
	private DateConverter dateConv;

	@Autowired
	private SystemConfig sysConf;


	@Override
	public Incident convertIncident(IncidentEntity inc) throws ConversionException {
		if(inc == null) {
			throw new ConversionException("Received null pointer for IncidentEntity conversion");
		}

		try {
			Incident i = new Incident();

			i.setId(inc.getId());
			i.setTimestamp(dateConv.dateToLocalDateTime(inc.getTimestamp()));
			i.setName(inc.getName());

			// Source address may be null, need to catch that case
			InetAddress src;
			if(inc.getSourceIp() != null) {
				src = InetAddress.getByName(inc.getSourceIp());
			}
			else {
				src = null;
			}
			i.setSource(src);
			i.setSourceName(inc.getSourceIpOntology());

			// Destination address may be null, need to catch that case
			InetAddress dest;
			if(inc.getDestinationIp() != null) {
				dest = InetAddress.getByName(inc.getDestinationIp());
			}
			else {
				dest = null;
			}
			i.setDestination(dest);
			i.setDestinationName(inc.getDestinationIpOntology());

			i.setRisk(inc.getRisk());
			i.setThreatLevel(determineThreatLevel(inc.getRisk()));
			i.setStatus(convertIncidentStatus(inc.getStatus()));

			List<IncidentRecommendation> recList;
			if(inc.getRecommendations() != null) {
				recList = convertRecommendationList(inc.getRecommendations());
			}
			else {
				recList = new ArrayList<>();
			}
			i.setRecommendations(recList);

			List<IncidentExplanation> explList;
			if(inc.getExplanations() != null) {
				explList = convertExplanationList(inc.getExplanations());
			}
			else {
				explList = new ArrayList<>();
			}
			i.setExplanations(explList);

			return i;
		}
		catch(UnknownHostException ex) {
			throw new ConversionException("Invalid source or destination address", ex);
		}
	}


	@Override
	public List<Incident> convertIncidentList(List<IncidentEntity> incList) throws ConversionException {
		if (incList == null) {
			throw new ConversionException("Received null pointer for List<IncidentEntity> conversion");
		}

		List<Incident> domList = incList.stream().map(i -> this.convertIncident(i)).collect(Collectors.toList());

		return domList;
	}


	@Override
	public IncidentStatus convertIncidentStatus(de.neusta.imonitor.correlation.persistency.enums.IncidentStatus status) throws ConversionException {
		switch(status) {
			case New:
				return IncidentStatus.NEW;
			case InProgress:
				return IncidentStatus.IN_PROGRESS;
			case Done:
				return IncidentStatus.DONE;
			default:
				if(LOG.isDebugEnabled()) {
					LOG.debug("IncidentStatus conversion(spec->dom) hit default case with: " + status);
				}
				return IncidentStatus.UNKNOWN;
		}
	}


	/**
	 * {@inheritDoc}
	 *
	 * If the enum cannot be converted, the enum constant Unknown will be returned.
	 *
	 * @param status Input enum
	 * @return Converted enum
	 */
	@Override
	public de.neusta.imonitor.correlation.persistency.enums.IncidentStatus convertDomIncidentStatus(IncidentStatus status) throws ConversionException {
		switch(status) {
			case NEW:
				return de.neusta.imonitor.correlation.persistency.enums.IncidentStatus.New;
			case IN_PROGRESS:
				return de.neusta.imonitor.correlation.persistency.enums.IncidentStatus.InProgress;
			case DONE:
				return de.neusta.imonitor.correlation.persistency.enums.IncidentStatus.Done;
			default:
				if(LOG.isDebugEnabled()) {
					LOG.debug("IncidentStatus (dom->spec) conversion hit default case with: " + status);
				}
				return de.neusta.imonitor.correlation.persistency.enums.IncidentStatus.Unknown;
		}
	}


	@Override
	public IncidentRecommendation convertRecommendation(RecommendationEntity rec) throws ConversionException {
		if(rec == null) {
			throw new ConversionException("Received null pointer for RecommendationEntity conversion");
		}

		IncidentRecommendation domRec = new IncidentRecommendation(rec.getRecommendation());

		return domRec;
	}


	@Override
	public List<IncidentRecommendation> convertRecommendationList(List<RecommendationEntity> recList) throws ConversionException {
		if(recList == null) {
			throw new ConversionException("Received null pointer for List<RecommendationEntity> conversion");
		}

		List<IncidentRecommendation> domList = recList.stream().map(r -> this.convertRecommendation(r)).collect(Collectors.toList());

		return domList;
	}


	@Override
	public IncidentExplanation convertExplanation(ExplanationEntity expl) throws ConversionException {
		if(expl == null) {
			throw new ConversionException("Received null pointer for ExplanationEntity conversion");
		}

		IncidentExplanation domExpl = new IncidentExplanation(expl.getExplanation());

		return domExpl;
	}


	@Override
	public List<IncidentExplanation> convertExplanationList(List<ExplanationEntity> explList) throws ConversionException {
		if(explList == null) {
			throw new ConversionException("Received null pointer for List<ExplanationEntity> conversion");
		}

		List<IncidentExplanation> domList = explList.stream().map(e -> this.convertExplanation(e)).collect(Collectors.toList());

		return domList;
	}


	/**
	 * Determine the threat level based on a risk value.
	 * This method uses the threat level limits defined in the configuration file.
	 *
	 * @param risk Risk value to determine threat level for
	 * @return Threat level
	 */
	private ThreatLevel determineThreatLevel(int risk) {
		if(risk >= sysConf.getThreatLevelLowMin() && risk <= sysConf.getThreatLevelLowMax()) {
			return ThreatLevel.LOW;
		}
		else if(risk >= sysConf.getThreatLevelMedMin() && risk <= sysConf.getThreatLevelMedMax()) {
			return ThreatLevel.MEDIUM;
		}
		else if(risk >= sysConf.getThreatLevelHighMin() && risk <= sysConf.getThreatLevelHighMax()) {
			return ThreatLevel.HIGH;
		}
		else {
			throw new ConversionException("Incident risk does not fit into configured threat level limits");
		}
	}
}
