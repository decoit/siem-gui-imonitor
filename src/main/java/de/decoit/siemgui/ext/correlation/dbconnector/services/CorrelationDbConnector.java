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
package de.decoit.siemgui.ext.correlation.dbconnector.services;

import de.decoit.siemgui.ext.correlation.dbconnector.repositories.IncidentRepository;
import de.decoit.siemgui.ext.correlation.dbconnector.repositories.RuleRepository;
import de.neusta.imonitor.correlation.persistency.entities.IncidentEntity;
import de.neusta.imonitor.correlation.persistency.entities.RuleEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Service
@Transactional(value = "correlationTransactionManager")
public class CorrelationDbConnector {
	@Autowired
	private IncidentRepository incidentRepo;

	@Autowired
	private RuleRepository ruleRepo;


	public IncidentEntity getIncidentById(long id) {
		return incidentRepo.findOne(id);
	}


	public List<IncidentEntity> getAllIncidents() {
		return incidentRepo.findAll();
	}


	public List<IncidentEntity> getNewIncidents(long lastId) {
		return incidentRepo.getNewIncidents(lastId);
	}


	public IncidentEntity saveAndFlushIncident(IncidentEntity ie) {
		return incidentRepo.saveAndFlush(ie);
	}


	public long countActiveIncidents() {
		return incidentRepo.countActiveIncidents();
	}


	public long countResolvedIncidents() {
		return incidentRepo.countResolvedIncidents();
	}


	public RuleEntity getRuleById(long id) {
		return ruleRepo.findOne(id);
	}


	public List<RuleEntity> getAllRules() {
		return ruleRepo.findAll();
	}


	public RuleEntity saveAndFlushRule(RuleEntity re) {
		return ruleRepo.saveAndFlush(re);
	}


	public void deleteRule(long id) {
		ruleRepo.delete(id);
		ruleRepo.flush();
	}
}
