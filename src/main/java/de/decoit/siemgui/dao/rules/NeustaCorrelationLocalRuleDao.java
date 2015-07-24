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

import de.decoit.siemgui.domain.rules.CorrelationRule;
import de.decoit.siemgui.ext.correlation.dbconnector.services.CorrelationDbConnector;
import de.decoit.siemgui.service.converter.NeustaCorrelationRuleConverter;
import de.neusta.imonitor.correlation.persistency.entities.RuleEntity;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
public class NeustaCorrelationLocalRuleDao implements LocalRuleDao {
	private final Logger LOG = LoggerFactory.getLogger(NeustaCorrelationLocalRuleDao.class.getName());

	@Autowired
	private CorrelationDbConnector ruleConnector;

	@Autowired
	private NeustaCorrelationRuleConverter correlationObjConv;


	@Override
	public List<CorrelationRule> getAllLocalRules() {
		List<RuleEntity> ruleEntityList = ruleConnector.getAllRules();

		List<CorrelationRule> domRuleList = correlationObjConv.convertLocalCorrelationRuleList(ruleEntityList);

		return domRuleList;
	}


	@Override
	public CorrelationRule getLocalRule(long id) {
		RuleEntity ruleEntity = ruleConnector.getRuleById(id);

		CorrelationRule domRule = correlationObjConv.convertLocalCorrelationRule(ruleEntity);

		return domRule;
	}


	@Override
	public void storeLocalRule(CorrelationRule rule) {
		RuleEntity ruleEntity = correlationObjConv.convertDomLocalCorrelationRule(rule);

		ruleConnector.saveAndFlushRule(ruleEntity);
	}


	@Override
	public void deleteLocalRule(long id) {
		ruleConnector.deleteRule(id);
	}
}
