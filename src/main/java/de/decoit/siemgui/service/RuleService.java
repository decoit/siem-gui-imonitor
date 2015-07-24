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

import de.decoit.siemgui.domain.rules.CorrelationRule;
import de.decoit.siemgui.exception.ExternalServiceException;
import java.util.List;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public interface RuleService {
	public List<CorrelationRule> getLocalRuleList() throws ExternalServiceException;


	public List<CorrelationRule> getRemoteRuleList() throws ExternalServiceException;


	public void storeLocalRule(CorrelationRule rule) throws ExternalServiceException;


	public void deleteLocalRule(long id) throws ExternalServiceException;


	public boolean uploadRule(long id, String newName) throws ExternalServiceException;


	public void downloadRule(long id, String newName) throws ExternalServiceException;
}
