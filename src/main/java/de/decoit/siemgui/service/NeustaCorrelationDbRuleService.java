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
package de.decoit.siemgui.service;

import de.decoit.siemgui.dao.rules.LocalRuleDao;
import de.decoit.siemgui.dao.rules.RemoteRuleDao;
import de.decoit.siemgui.domain.rules.CorrelationRule;
import de.decoit.siemgui.exception.ExternalServiceException;
import java.util.Collections;
import java.util.Comparator;
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
public class NeustaCorrelationDbRuleService implements RuleService {
	private final Logger LOG = LoggerFactory.getLogger(NeustaCorrelationDbRuleService.class.getName());

	@Autowired
	private LocalRuleDao localDao;

	@Autowired
	private RemoteRuleDao remoteDao;


	@Override
	public List<CorrelationRule> getLocalRuleList() throws ExternalServiceException {
		List<CorrelationRule> rv = localDao.getAllLocalRules();
		Collections.sort(rv, new CorrelationRuleByNameComparator());

		return rv;
	}


	@Override
	public List<CorrelationRule> getRemoteRuleList() throws ExternalServiceException {
		List<CorrelationRule> rv = remoteDao.getAllRemoteRules();
		Collections.sort(rv, new CorrelationRuleByNameComparator());

		return rv;
	}


	@Override
	public void storeLocalRule(CorrelationRule rule) throws ExternalServiceException {
		localDao.storeLocalRule(rule);
	}


	@Override
	public void deleteLocalRule(long id) throws ExternalServiceException {
		localDao.deleteLocalRule(id);
	}


	@Override
	public boolean uploadRule(long id, String newName) throws ExternalServiceException {
		CorrelationRule rule = localDao.getLocalRule(id);
		rule.setName(newName);

		return remoteDao.uploadRule(rule);
	}


	@Override
	public void downloadRule(long id, String newName) throws ExternalServiceException {
		CorrelationRule rule = remoteDao.getRemoteRule(id);
		rule.setName(newName);

		localDao.storeLocalRule(rule);
	}


	private class CorrelationRuleByNameComparator implements Comparator<CorrelationRule> {
		@Override
		public int compare(CorrelationRule r1, CorrelationRule r2) {
			if(r1.getName() == null && r2.getName() == null) {
				return 0;
			}
			else if(r1.getName() == null) {
				return 1;
			}
			else if(r2.getName() == null) {
				return -1;
			}

			return r1.getName().toLowerCase().compareTo(r2.getName().toLowerCase());
		}
	}
}
