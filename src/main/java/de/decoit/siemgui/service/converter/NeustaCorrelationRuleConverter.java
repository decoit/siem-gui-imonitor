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
import de.decoit.siemgui.domain.rules.CorrelationRuleAction;
import de.decoit.siemgui.domain.rules.CorrelationRuleCondition;
import de.decoit.siemgui.exception.ConversionException;
import de.neusta.imonitor.correlation.persistency.entities.RuleEntity;
import de.neusta.imonitor.knowledgeserver.webservice.RuleDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class NeustaCorrelationRuleConverter implements CorrelationRuleConverter<RuleEntity, RuleDTO> {
	private final Logger LOG = LoggerFactory.getLogger(NeustaCorrelationRuleConverter.class.getName());

	@Override
	public RuleEntity convertDomLocalCorrelationRule(CorrelationRule rule) throws ConversionException {
		if(rule == null) {
			throw new ConversionException("Received null pointer for CorrelationRule to RuleEntity conversion");
		}

		Document doc = createRuleXmlDocument(rule.getDescription(), rule.getCondition(), rule.getAction());

		RuleEntity ruleEnt = new RuleEntity();
		ruleEnt.setId(rule.getEntityId());
		ruleEnt.setName(rule.getName());
		ruleEnt.setRule(doc.asXML());

		if(rule.getDtoId() > 0) {
			ruleEnt.setServerRuleId(rule.getDtoId());
		}
		else {
			ruleEnt.setServerRuleId(null);
		}

		return ruleEnt;
	}


	@Override
	public List<RuleEntity> convertDomLocalCorrelationRuleList(List<CorrelationRule> ruleList) throws ConversionException {
		if(ruleList == null) {
			throw new ConversionException("Received null pointer for List<CorrelationRule> to List<RuleEntity> conversion");
		}

		List<RuleEntity> list = ruleList.stream().map(r -> this.convertDomLocalCorrelationRule(r)).collect(Collectors.toList());

		return list;
	}


	@Override
	public CorrelationRule convertLocalCorrelationRule(RuleEntity rule) throws ConversionException {
		if(rule == null) {
			throw new ConversionException("Received null pointer for RuleEntity conversion");
		}

		Document doc;
		try {
			doc = DocumentHelper.parseText(rule.getRule());
		}
		catch (DocumentException ex) {
			throw new ConversionException("Exception during XML conversion", ex);
		}

		CorrelationRule domRule = new CorrelationRule();
		domRule.setEntityId(rule.getId());
		domRule.setName(rule.getName());
		domRule.setDescription(parseRuleDescriptionFromXml(doc));
		domRule.setCondition(parseRuleConditionFromXml(doc));
		domRule.setAction(parseRuleActionFromXml(doc));

		if(rule.getServerRuldId() != null) {
			domRule.setDtoId(rule.getServerRuldId());
		}
		else {
			domRule.setDtoId(0L);
		}

		return domRule;
	}


	@Override
	public List<CorrelationRule> convertLocalCorrelationRuleList(List<RuleEntity> ruleList) throws ConversionException {
		if(ruleList == null) {
			throw new ConversionException("Received null pointer for List<RuleEntity> conversion");
		}

		List<CorrelationRule> domList = ruleList.stream().map(r -> this.convertLocalCorrelationRule(r)).collect(Collectors.toList());

		return domList;
	}


	@Override
	public CorrelationRule convertRemoteCorrelationRule(RuleDTO rule) throws ConversionException {
		if(rule == null) {
			throw new ConversionException("Received null pointer for RuleDTO conversion");
		}

		Document doc;
		try {
			doc = DocumentHelper.parseText(rule.getXml());
		}
		catch (DocumentException ex) {
			throw new ConversionException("Exception during XML conversion", ex);
		}

		CorrelationRule domRule = new CorrelationRule();

		domRule.setDtoId(rule.getId());
		domRule.setName(rule.getName());
		domRule.setDescription(parseRuleDescriptionFromXml(doc));
		domRule.setCondition(parseRuleConditionFromXml(doc));
		domRule.setAction(parseRuleActionFromXml(doc));

		return domRule;
	}


	@Override
	public List<CorrelationRule> convertRemoteCorrelationRuleList(List<RuleDTO> ruleList) throws ConversionException {
		if(ruleList == null) {
			throw new ConversionException("Received null pointer for List<RuleDTO> conversion");
		}

		List<CorrelationRule> domList = ruleList.stream().map(r -> this.convertRemoteCorrelationRule(r)).collect(Collectors.toList());

		return domList;
	}


	@Override
	public RuleDTO convertDomRemoteCorrelationRule(CorrelationRule rule) throws ConversionException {
		if(rule == null) {
			throw new ConversionException("Received null pointer for CorrelationRule to RuleDTO conversion");
		}

		Document doc = createRuleXmlDocument(rule.getDescription(), rule.getCondition(), rule.getAction());

		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setId(rule.getDtoId());
		ruleDto.setName(rule.getName());
		ruleDto.setXml(doc.asXML());

		return ruleDto;
	}


	@Override
	public List<RuleDTO> convertDomRemoteCorrelationRuleList(List<CorrelationRule> ruleList) throws ConversionException {
		if(ruleList == null) {
			throw new ConversionException("Received null pointer for List<CorrelationRule> to List<RuleDTO> conversion");
		}

		List<RuleDTO> list = ruleList.stream().map(r -> this.convertDomRemoteCorrelationRule(r)).collect(Collectors.toList());

		return list;
	}


	/**
	 * Parse the rule description from a rule XML document.
	 * The XML document must contain the XML structure that defines a correlation rule.
	 *
	 * @param xml Rule XML document
	 * @return Description string
	 */
	private String parseRuleDescriptionFromXml(Document xml) {
		Node node = xml.selectSingleNode("//rule/description");

		String description = null;
		if(node != null) {
			description = node.getText();
		}

		return description;
	}


	/**
	 * Parse the rule condition from a rule XML document.
	 * The XML document must contain the XML structure that defines a correlation rule.
	 *
	 * @param xml Rule XML document
	 * @return Rule condition object
	 */
	private CorrelationRuleCondition parseRuleConditionFromXml(Document xml) {
		Node node = xml.selectSingleNode("//rule/condition/query");

		String query = null;
		if(node != null) {
			query = node.getText();
		}


		CorrelationRuleCondition cond = new CorrelationRuleCondition();
		cond.setQuery(query);

		return cond;
	}


	/**
	 * Parse the rule action from a rule XML document.
	 * The XML document must contain the XML structure that defines a correlation rule.
	 *
	 * @param xml Rule XML document
	 * @return Rule condition object
	 */
	private CorrelationRuleAction parseRuleActionFromXml(Document xml) {
		Node incNameNode = xml.selectSingleNode("//rule/action/incidentname");

		String incName = null;
		if(incNameNode != null) {
			incName = incNameNode.getText();
		}

		Node incRiskNode = xml.selectSingleNode("//rule/action/risk");
		String incRisk = null;
		if(incRiskNode != null) {
			incRisk = incRiskNode.getText();
		}

		List<Node> queryNodeList = xml.selectNodes("//rule/action/query");
		List<String> queryList = queryNodeList.stream().map(Node::getText).collect(Collectors.toList());

		List<Node> updateQueryNodeList = xml.selectNodes("//rule/action/updatequery");
		List<String> updateQueryList = updateQueryNodeList.stream().map(Node::getText).collect(Collectors.toList());

		List<Node> recNodeList = xml.selectNodes("//rule/action/recommendation");
		List<String> recList = recNodeList.stream().map(Node::getText).collect(Collectors.toList());

		List<Node> explNodeList = xml.selectNodes("//rule/action/explanation");
		List<String> explList = explNodeList.stream().map(Node::getText).collect(Collectors.toList());

		CorrelationRuleAction cra = new CorrelationRuleAction();

		cra.setIncidentName(incName);
		cra.setRisk(incRisk);
		cra.setQuery(queryList);
		cra.setUpdateQuery(updateQueryList);
		cra.setRecommendation(recList);
		cra.setExplanation(explList);

		return cra;
	}


	/**
	 * Create a rule XML document from domain object information.
	 *
	 * @param description Rule description string
	 * @param cond Rule condition object
	 * @param action Rule action object
	 * @return Created XML document
	 */
	private Document createRuleXmlDocument(String description, CorrelationRuleCondition cond, CorrelationRuleAction action) {
		Document doc = DocumentHelper.createDocument();
		Element eRule = doc.addElement("rule");

		// Create the <rule><description>...</description></rule> block
		if(description != null) {
			eRule
				.addElement("description")
				.addText(description);
		}

		// Create the <rule><condition><query>...</query></condition></rule> block
		if(cond.getQuery() != null) {
			eRule
				.addElement("condition")
				.addElement("query")
				.addText(cond.getQuery());
		}

		// Create the <rule><action></action></rule> element
		Element eAction = eRule.addElement("action");

		// Create the <rule><action><incidentname>...</incidentname></action></rule> block
		if(action.getIncidentName() != null) {
			eAction
				.addElement("incidentname")
				.addText(action.getIncidentName());
		}

		// Create the <rule><action><risk>...</risk></action></rule> block
		if(action.getRisk() != null) {
			eAction
				.addElement("risk")
				.addText(action.getRisk());
		}

		// Create the <rule><action><query>...</query></action></rule> blocks (multiple)
		action.getQuery().stream().forEach((q) -> {
			eAction.addElement("query").addText(q);
		});

		// Create the <rule><action><updatequery>...</updatequery></action></rule> blocks (multiple)
		action.getUpdateQuery().stream().forEach((uq) -> {
			eAction.addElement("updatequery").addText(uq);
		});

		// Create the <rule><action><recommendation>...</recommendation></action></rule> blocks (multiple)
		action.getRecommendation().stream().forEach((r) -> {
			eAction.addElement("recommendation").addText(r);
		});

		// Create the <rule><action><explanation>...</explanation></action></rule> blocks (multiple)
		action.getExplanation().stream().forEach((e) -> {
			eAction.addElement("explanation").addText(e);
		});

		return doc;
	}
}
