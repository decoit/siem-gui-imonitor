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

import de.decoit.siemgui.domain.rules.CorrelationRule;
import de.decoit.siemgui.exception.ConversionException;
import java.util.List;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 * @param <LOCALRULE> Implementation specific type for local rules
 * @param <REMOTERULE> Implementation specific type for remote rules
 */
public interface CorrelationRuleConverter<LOCALRULE, REMOTERULE> {
	public CorrelationRule convertLocalCorrelationRule(LOCALRULE rule) throws ConversionException;


	public List<CorrelationRule> convertLocalCorrelationRuleList(List<LOCALRULE> ruleList) throws ConversionException;


	public LOCALRULE convertDomLocalCorrelationRule(CorrelationRule rule) throws ConversionException;


	public List<LOCALRULE> convertDomLocalCorrelationRuleList(List<CorrelationRule> ruleList) throws ConversionException;


	public CorrelationRule convertRemoteCorrelationRule(REMOTERULE rule) throws ConversionException;


	public List<CorrelationRule> convertRemoteCorrelationRuleList(List<REMOTERULE> ruleList) throws ConversionException;


	public REMOTERULE convertDomRemoteCorrelationRule(CorrelationRule rule) throws ConversionException;


	public List<REMOTERULE> convertDomRemoteCorrelationRuleList(List<CorrelationRule> ruleList) throws ConversionException;
}
