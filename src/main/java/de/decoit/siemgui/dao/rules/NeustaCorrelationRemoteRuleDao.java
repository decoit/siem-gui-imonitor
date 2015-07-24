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
package de.decoit.siemgui.dao.rules;

import de.decoit.siemgui.config.RootConfig.SystemConfig;
import de.decoit.siemgui.domain.rules.CorrelationRule;
import de.decoit.siemgui.exception.ExternalServiceException;
import de.decoit.siemgui.service.converter.NeustaCorrelationRuleConverter;
import de.neusta.imonitor.knowledgeserver.webservice.RuleDTO;
import de.neusta.imonitor.rulewebservice.RuleWebServiceApi;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
public class NeustaCorrelationRemoteRuleDao implements RemoteRuleDao {
	private final Logger LOG = LoggerFactory.getLogger(NeustaCorrelationRemoteRuleDao.class.getName());

	@Autowired
	private SystemConfig sysConf;

	@Autowired
	private RuleWebServiceApi ruleWebServApi;

	@Autowired
	private NeustaCorrelationRuleConverter correlationRuleConv;


	@Override
	public List<CorrelationRule> getAllRemoteRules() throws ExternalServiceException {
		try {
			List<CorrelationRule> rv = correlationRuleConv.convertRemoteCorrelationRuleList(ruleWebServApi.getRules());

			return rv;
		}
		catch (MalformedURLException ex) {
			throw new ExternalServiceException("Error while accessing rule web service", ex);
		}
	}


	@Override
	public CorrelationRule getRemoteRule(long id) throws ExternalServiceException {
		try {
			CorrelationRule rv = correlationRuleConv.convertRemoteCorrelationRule(ruleWebServApi.getRule(id));

			return rv;
		}
		catch (MalformedURLException ex) {
			throw new ExternalServiceException("Error while accessing rule web service", ex);
		}
	}


	@Override
	public boolean uploadRule(CorrelationRule rule) throws ExternalServiceException {
		RuleDTO ruleDto = correlationRuleConv.convertDomRemoteCorrelationRule(rule);
		try {
			return ruleWebServApi.commitRule(ruleDto);
		}
		catch (Exception ex) {
			throw new ExternalServiceException("Error while accessing rule web service", ex);
		}
	}


	@PostConstruct
	private void init() {
		try {
			ruleWebServApi.setWebserviceUrl(new URL(sysConf.getRulesWebServiceUrl()));
		}
		catch (MalformedURLException ex) {
			LOG.error("MalformedURLException while calling @PostConstruct NeustaCorrelationRemoteRuleDao.init()");
			throw new IllegalArgumentException("Calling @PostConstruct NeustaCorrelationRemoteRuleDao.init() failed", ex);
		}
	}
}
