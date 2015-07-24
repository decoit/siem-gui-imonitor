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
import de.neusta.imonitor.correlation.persistency.entities.RuleEntity;
import de.neusta.imonitor.knowledgeserver.webservice.RuleDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@RunWith(MockitoJUnitRunner.class)
public class NeustaCorrelationRuleConverterTest {
	@Before
	public void setUp() {
	}


	@After
	public void tearDown() {
	}


	/**
	 * Test of convertDomLocalCorrelationRule method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertDomLocalCorrelationRule() {
		int entityId = 42;
		String entityName = "Test-Rule";
		int dtoId = 21;
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		System.out.println("convertDomLocalCorrelationRule with " + numQuery + "x <query>, " + numUpdateQuery + "x <updatequery>, " + numRecommendation + "x <recommendation>, " + numExplanation + "x <explanation>, risk " + risk);

		CorrelationRule rule = createSingleRuleObject(entityId, dtoId, entityName, numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		RuleEntity result = instance.convertDomLocalCorrelationRule(rule);
		RuleEntity expResult = createSingleRuleEntity(entityId, entityName, numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);

		assertEquals("Entity-ID mismatch", expResult.getId(), result.getId());
		assertEquals("Rule name mismatch", expResult.getName(), result.getName());
		assertEquals("Rule XML mismatch", expResult.getRule(), result.getRule());
	}


	/**
	 * Test of convertDomLocalCorrelationRuleList method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertDomLocalCorrelationRuleList() {
		System.out.println("convertDomLocalCorrelationRuleList");

		int listSize = 3;
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		List<CorrelationRule> ruleList = new ArrayList<>();

		for(int i=0; i<listSize; i++) {
			CorrelationRule rule = createSingleRuleObject((42+i), (21+i), "Test-Rule", numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);
			ruleList.add(rule);
		}

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		List<RuleEntity> result = instance.convertDomLocalCorrelationRuleList(ruleList);

		assertEquals("Result list size mismatch", listSize, result.size());
	}


	/**
	 * Test of convertLocalCorrelationRule method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertLocalCorrelationRule() {
		int entityId = 42;
		String entityName = "Test-Rule";
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		System.out.println("convertLocalCorrelationRule with " + numQuery + "x <query>, " + numUpdateQuery + "x <updatequery>, " + numRecommendation + "x <recommendation>, " + numExplanation + "x <explanation>, risk " + risk);

		RuleEntity rule = createSingleRuleEntity(entityId, entityName, numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		CorrelationRule result = instance.convertLocalCorrelationRule(rule);

		assertEquals("Entity-ID mismatch", entityId, result.getEntityId());
		assertEquals("Rule name mismatch", entityName, result.getName());
		assertEquals("Rule description mismatch (XML)", "RULE-DESCRIPTION", result.getDescription());
		assertEquals("Rule condition query mismatch (XML)", "THIS-SHOULD-BE-SPARQL", result.getCondition().getQuery());
		assertEquals("Rule action risk mismatch (XML)", String.valueOf(risk), result.getAction().getRisk());
		assertEquals("Rule action query amount mismatch (XML)", numQuery, result.getAction().getQuery().size());
		for(int i=0; i<numQuery; i++) {
			assertTrue("Rule action query missing: SPARQL-QUERY-" + i, result.getAction().getQuery().contains("SPARQL-QUERY-" + i));
		}

		assertEquals("Rule action updatequery amount mismatch (XML)", numUpdateQuery, result.getAction().getUpdateQuery().size());
		for(int i=0; i<numUpdateQuery; i++) {
			assertTrue("Rule action updatequery missing: SPARQL-UPDATE-QUERY-" + i, result.getAction().getUpdateQuery().contains("SPARQL-UPDATE-QUERY-" + i));
		}

		assertEquals("Rule action recommendation amount mismatch (XML)", numRecommendation, result.getAction().getRecommendation().size());
		for(int i=0; i<numRecommendation; i++) {
			assertTrue("Rule action recommendation missing: RECOMMENDATION-" + i, result.getAction().getRecommendation().contains("RECOMMENDATION-" + i));
		}

		assertEquals("Rule action explanation amount mismatch (XML)", numExplanation, result.getAction().getExplanation().size());
		for(int i=0; i<numExplanation; i++) {
			assertTrue("Rule action explanation missing: EXPLANATION-" + i, result.getAction().getExplanation().contains("EXPLANATION-" + i));
		}
	}


	/**
	 * Test of convertLocalCorrelationRuleList method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertLocalCorrelationRuleList() {
		System.out.println("convertLocalCorrelationRuleList");

		int listSize = 3;
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		List<RuleEntity> ruleList = new ArrayList<>();

		for(int i=0; i<listSize; i++) {
			RuleEntity rule = createSingleRuleEntity((42+i), "Test-Rule", numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);
			ruleList.add(rule);
		}

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		List<CorrelationRule> result = instance.convertLocalCorrelationRuleList(ruleList);

		assertEquals("Result list size mismatch", listSize, result.size());
	}


	/**
	 * Test of convertRemoteCorrelationRule method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertRemoteCorrelationRule() {
		int dtoId = 42;
		String dtoName = "Test-Rule";
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		System.out.println("convertRemoteCorrelationRule with " + numQuery + "x <query>, " + numUpdateQuery + "x <updatequery>, " + numRecommendation + "x <recommendation>, " + numExplanation + "x <explanation>, risk " + risk);

		RuleDTO rule = createSingleRuleDTO(dtoId, dtoName, numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		CorrelationRule result = instance.convertRemoteCorrelationRule(rule);

		assertEquals("DTO-ID mismatch", dtoId, result.getDtoId());
		assertEquals("Rule name mismatch", dtoName, result.getName());
		assertEquals("Rule description mismatch (XML)", "RULE-DESCRIPTION", result.getDescription());
		assertEquals("Rule condition query mismatch (XML)", "THIS-SHOULD-BE-SPARQL", result.getCondition().getQuery());
		assertEquals("Rule action risk mismatch (XML)", String.valueOf(risk), result.getAction().getRisk());
		assertEquals("Rule action query amount mismatch (XML)", numQuery, result.getAction().getQuery().size());
		for(int i=0; i<numQuery; i++) {
			assertTrue("Rule action query missing: SPARQL-QUERY-" + i, result.getAction().getQuery().contains("SPARQL-QUERY-" + i));
		}

		assertEquals("Rule action updatequery amount mismatch (XML)", numUpdateQuery, result.getAction().getUpdateQuery().size());
		for(int i=0; i<numUpdateQuery; i++) {
			assertTrue("Rule action updatequery missing: SPARQL-UPDATE-QUERY-" + i, result.getAction().getUpdateQuery().contains("SPARQL-UPDATE-QUERY-" + i));
		}

		assertEquals("Rule action recommendation amount mismatch (XML)", numRecommendation, result.getAction().getRecommendation().size());
		for(int i=0; i<numRecommendation; i++) {
			assertTrue("Rule action recommendation missing: RECOMMENDATION-" + i, result.getAction().getRecommendation().contains("RECOMMENDATION-" + i));
		}

		assertEquals("Rule action explanation amount mismatch (XML)", numExplanation, result.getAction().getExplanation().size());
		for(int i=0; i<numExplanation; i++) {
			assertTrue("Rule action explanation missing: EXPLANATION-" + i, result.getAction().getExplanation().contains("EXPLANATION-" + i));
		}
	}


	/**
	 * Test of convertRemoteCorrelationRuleList method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertRemoteCorrelationRuleList() {
		System.out.println("convertRemoteCorrelationRuleList");

		int listSize = 3;
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		List<RuleDTO> ruleList = new ArrayList<>();

		for(int i=0; i<listSize; i++) {
			RuleDTO rule = createSingleRuleDTO((42+i), "Test-Rule", numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);
			ruleList.add(rule);
		}

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		List<CorrelationRule> result = instance.convertRemoteCorrelationRuleList(ruleList);

		assertEquals("Result list size mismatch", listSize, result.size());
	}


	/**
	 * Test of convertDomRemoteCorrelationRule method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertDomRemoteCorrelationRule() {
		int entityId = 42;
		String entityName = "Test-Rule";
		int dtoId = 21;
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		System.out.println("convertDomRemoteCorrelationRule with " + numQuery + "x <query>, " + numUpdateQuery + "x <updatequery>, " + numRecommendation + "x <recommendation>, " + numExplanation + "x <explanation>, risk " + risk);

		CorrelationRule rule = createSingleRuleObject(entityId, dtoId, entityName, numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		RuleDTO result = instance.convertDomRemoteCorrelationRule(rule);
		RuleDTO expResult = createSingleRuleDTO(dtoId, entityName, numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);

		assertEquals("DTO-ID mismatch", expResult.getId(), result.getId());
		assertEquals("Rule name mismatch", expResult.getName(), result.getName());
		assertEquals("Rule XML mismatch", expResult.getXml(), result.getXml());
	}


	/**
	 * Test of convertDomRemoteCorrelationRuleList method, of class NeustaCorrelationRuleConverter.
	 */
	@Test
	public void testConvertDomRemoteCorrelationRuleList() {
		System.out.println("convertDomRemoteCorrelationRuleList");

		int listSize = 3;
		int numQuery = 2;
		int numUpdateQuery = 2;
		int numRecommendation = 2;
		int numExplanation = 2;
		int risk = 5;

		List<CorrelationRule> ruleList = new ArrayList<>();

		for(int i=0; i<listSize; i++) {
			CorrelationRule rule = createSingleRuleObject((42+i), (21+i), "Test-Rule", numQuery, numUpdateQuery, numRecommendation, numExplanation, risk);
			ruleList.add(rule);
		}

		NeustaCorrelationRuleConverter instance = new NeustaCorrelationRuleConverter();
		List<RuleDTO> result = instance.convertDomRemoteCorrelationRuleList(ruleList);

		assertEquals("Result list size mismatch", listSize, result.size());
	}


/* *******************************************************
 * HELPER METHODS
 ******************************************************* */

	/**
	 * Create a RuleEntity object for testing.
	 * The contents of an object generated by this method are considered equal to the contents of
	 * the other helper methods createSingleRuleDTO() and createSingleRuleObject() if called with
	 * the same parameters.
	 *
	 * @param entityId ID of the entity
	 * @param entityName Value for the name field of the entity
	 * @param actionQueries Number of &lt;query&gt; tags to include in the action part
	 * @param actionUpdateQueries Number of &lt;updatequery&gt; tags to include in the action part
	 * @param actionRecommendations Number of &lt;recommendation&gt; tags to include in the action part
	 * @param actionExplanations Number of &lt;explanation&gt; tags to include in the action part
	 * @param actionRisk Risk value to include in the action part
	 * @return Created entity object
	 */
	private RuleEntity createSingleRuleEntity(int entityId, String entityName, int actionQueries, int actionUpdateQueries, int actionRecommendations, int actionExplanations, int actionRisk) {
		String ruleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<rule>"
				+ "<description>RULE-DESCRIPTION</description>"
				+ "<condition><query>THIS-SHOULD-BE-SPARQL</query></condition>"
				+ "<action>"
				+ "<incidentname>Test-Incident</incidentname>"
				+ "<risk>" + actionRisk + "</risk>";

		for(int i=0; i<actionQueries; i++) {
			ruleXml += "<query>SPARQL-QUERY-" + i + "</query>";
		}

		for(int i=0; i<actionUpdateQueries; i++) {
			ruleXml += "<updatequery>SPARQL-UPDATE-QUERY-" + i + "</updatequery>";
		}

		for(int i=0; i<actionRecommendations; i++) {
			ruleXml += "<recommendation>RECOMMENDATION-" + i + "</recommendation>";
		}

		for(int i=0; i<actionExplanations; i++) {
			ruleXml += "<explanation>EXPLANATION-" + i + "</explanation>";
		}

		ruleXml += "</action></rule>";

		RuleEntity rule = new RuleEntity();
		rule.setId(entityId);
		rule.setName(entityName);
		rule.setRule(ruleXml);

		return rule;
	}


	/**
	 * Create a RuleDTO object for testing.
	 * The contents of an object generated by this method are considered equal to the contents of
	 * the other helper methods createSingleRuleEntity() and createSingleRuleObject() if called with
	 * the same parameters.
	 *
	 * @param dtoId ID of the DTO
	 * @param dtoName Value for the name field of the DTO
	 * @param actionQueries Number of &lt;query&gt; tags to include in the action part
	 * @param actionUpdateQueries Number of &lt;updatequery&gt; tags to include in the action part
	 * @param actionRecommendations Number of &lt;recommendation&gt; tags to include in the action part
	 * @param actionExplanations Number of &lt;explanation&gt; tags to include in the action part
	 * @param actionRisk Risk value to include in the action part
	 * @return Created DTO
	 */
	private RuleDTO createSingleRuleDTO(int dtoId, String dtoName, int actionQueries, int actionUpdateQueries, int actionRecommendations, int actionExplanations, int actionRisk) {
		String ruleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<rule>"
				+ "<description>RULE-DESCRIPTION</description>"
				+ "<condition><query>THIS-SHOULD-BE-SPARQL</query></condition>"
				+ "<action>"
				+ "<incidentname>Test-Incident</incidentname>"
				+ "<risk>" + actionRisk + "</risk>";

		for(int i=0; i<actionQueries; i++) {
			ruleXml += "<query>SPARQL-QUERY-" + i + "</query>";
		}

		for(int i=0; i<actionUpdateQueries; i++) {
			ruleXml += "<updatequery>SPARQL-UPDATE-QUERY-" + i + "</updatequery>";
		}

		for(int i=0; i<actionRecommendations; i++) {
			ruleXml += "<recommendation>RECOMMENDATION-" + i + "</recommendation>";
		}

		for(int i=0; i<actionExplanations; i++) {
			ruleXml += "<explanation>EXPLANATION-" + i + "</explanation>";
		}

		ruleXml += "</action></rule>";

		RuleDTO rule = new RuleDTO();
		rule.setId(new Long(dtoId));
		rule.setName(dtoName);
		rule.setXml(ruleXml);

		return rule;
	}


	/**
	 * Create a rule domain object for testing.
	 * The contents of an object generated by this method are considered equal to the contents of
	 * the other helper methods createSingleRuleEntity() and createSingleRuleEntity() if called with
	 * the same parameters.
	 *
	 * @param entityId Entity-ID to store
	 * @param dtoId DTO-ID to store
	 * @param ruleName Value for the name field of either Entity or DTO
	 * @param actionQueries Number of &lt;query&gt; tags to include in the action part
	 * @param actionUpdateQueries Number of &lt;updatequery&gt; tags to include in the action part
	 * @param actionRecommendations Number of &lt;recommendation&gt; tags to include in the action part
	 * @param actionExplanations Number of &lt;explanation&gt; tags to include in the action part
	 * @param actionRisk Risk value to include in the action part
	 * @return Created domain object
	 */
	private CorrelationRule createSingleRuleObject(int entityId, int dtoId, String ruleName, int actionQueries, int actionUpdateQueries, int actionRecommendations, int actionExplanations, int actionRisk) {
		CorrelationRule rule = new CorrelationRule();

		rule.setEntityId(entityId);
		rule.setDtoId(dtoId);
		rule.setName(ruleName);
		rule.setDescription("RULE-DESCRIPTION");

		CorrelationRuleCondition cond = new CorrelationRuleCondition();
		cond.setQuery("THIS-SHOULD-BE-SPARQL");
		rule.setCondition(cond);

		CorrelationRuleAction action = new CorrelationRuleAction();
		action.setIncidentName("Test-Incident");
		action.setRisk(String.valueOf(actionRisk));

		List<String> queryList = new ArrayList<>();
		for(int i=0; i<actionQueries; i++) {
			queryList.add("SPARQL-QUERY-" + i);
		}
		action.setQuery(queryList);

		List<String> updateQueryList = new ArrayList<>();
		for(int i=0; i<actionUpdateQueries; i++) {
			updateQueryList.add("SPARQL-UPDATE-QUERY-" + i);
		}
		action.setUpdateQuery(updateQueryList);

		List<String> recommendationList = new ArrayList<>();
		for(int i=0; i<actionRecommendations; i++) {
			recommendationList.add("RECOMMENDATION-" + i);
		}
		action.setRecommendation(recommendationList);

		List<String> explanationList = new ArrayList<>();
		for(int i=0; i<actionExplanations; i++) {
			explanationList.add("EXPLANATION-" + i);
		}
		action.setExplanation(explanationList);

		rule.setAction(action);

		return rule;
	}
}
