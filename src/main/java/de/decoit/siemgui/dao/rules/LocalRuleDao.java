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
import java.util.List;



/**
 * This interface defines a DAO that can read from and write to a local repository of correlation rules.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public interface LocalRuleDao {
	/**
	 * Read a single rule from the local repository.
	 * The rule entity must be identified by a ID of type long.
	 *
	 * @param id Rule ID
	 * @return Rule domain object
	 */
	public CorrelationRule getLocalRule(long id);


	/**
	 * Get a list of all rules that are present in the local repository.
	 * No filtering or ordering is applied.
	 *
	 * @return List of rule domain objects
	 */
	public List<CorrelationRule> getAllLocalRules();


	/**
	 * Save a new or modified rule into the local repository.
	 * If the rule does not contain an ID, a new repository entry will be created.
	 * If a rule ID is present, the entry with that ID will be updated.
	 *
	 * @param rule Rule domain object to be stored
	 */
	public void storeLocalRule(CorrelationRule rule);


	/**
	 * Delete a rule from the local repository.
	 * The rule entity must be identified by a ID of type long.
	 *
	 * @param id Rule ID
	 */
	public void deleteLocalRule(long id);
}
