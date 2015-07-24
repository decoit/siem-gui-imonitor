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
package de.decoit.siemgui.websocket;

import de.decoit.siemgui.domain.rules.CorrelationRule;
import de.decoit.siemgui.exception.ErrorMsgToFrontendException;
import de.decoit.siemgui.exception.ExternalServiceException;
import de.decoit.siemgui.service.RuleService;
import de.decoit.siemgui.stomp.msgs.incoming.MsgRuleDownloadRequest;
import de.decoit.siemgui.stomp.msgs.incoming.MsgRuleEntityIdRequest;
import de.decoit.siemgui.stomp.msgs.incoming.MsgRuleEditRequest;
import de.decoit.siemgui.stomp.msgs.incoming.MsgRuleUploadRequest;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgErrorNotification;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgRuleList;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Controller
public class RulesAdminController {
	private final Logger LOG = LoggerFactory.getLogger(RulesAdminController.class.getName());

	@Autowired
	private RuleService ruleServ;


	@MessageMapping("/admin/rules/local/list")
	@SendToUser("/queue/admin/rules/local/list")
	@Secured("ROLE_SIEMADMIN")
	public MsgRuleList requestLocalRuleList(Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("requestLocalRuleList, no incoming message");
		}

		List<CorrelationRule> ruleList = ruleServ.getLocalRuleList();

		MsgRuleList msg = new MsgRuleList();
		msg.setRuleList(ruleList);

		if(LOG.isDebugEnabled()) {
			LOG.debug("requestLocalRuleList, outgoing message: " + msg.toString());
		}

		return msg;
	}



	@MessageMapping("/admin/rules/remote/list")
	@SendToUser("/queue/admin/rules/remote/list")
	@Secured("ROLE_SIEMADMIN")
	public MsgRuleList requestRemoteRuleList(Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("requestRemoteRuleList, no incoming message");
		}

		List<CorrelationRule> ruleList = ruleServ.getRemoteRuleList();

		MsgRuleList msg = new MsgRuleList();
		msg.setRuleList(ruleList);

		if(LOG.isDebugEnabled()) {
			LOG.debug("requestRemoteRuleList, outgoing message: " + msg.toString());
		}

		return msg;
	}


	@MessageMapping("/admin/rules/local/store")
	@SendToUser("/queue/admin/rules/local/list")
	@Secured("ROLE_SIEMADMIN")
	public MsgRuleList storeLocalRule(MsgRuleEditRequest inMsg, Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("storeLocalRule, incoming message " + inMsg.toString());
		}

		ruleServ.storeLocalRule(inMsg.getRule());

		List<CorrelationRule> ruleList = ruleServ.getLocalRuleList();

		MsgRuleList msg = new MsgRuleList();
		msg.setRuleList(ruleList);

		if(LOG.isDebugEnabled()) {
			LOG.debug("storeLocalRule, outgoing message: " + msg.toString());
		}

		return msg;
	}


	@MessageMapping("/admin/rules/local/delete")
	@SendToUser("/queue/admin/rules/local/list")
	@Secured("ROLE_SIEMADMIN")
	public MsgRuleList deleteLocalRule(MsgRuleEntityIdRequest inMsg, Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("deleteLocalRule, incoming message " + inMsg.toString());
		}

		ruleServ.deleteLocalRule(inMsg.getEntityId());

		List<CorrelationRule> ruleList = ruleServ.getLocalRuleList();

		MsgRuleList msg = new MsgRuleList();
		msg.setRuleList(ruleList);

		if(LOG.isDebugEnabled()) {
			LOG.debug("deleteLocalRule, outgoing message: " + msg.toString());
		}

		return msg;
	}


	@MessageMapping("/admin/rules/local/upload")
	@SendToUser("/queue/admin/rules/remote/list")
	@Secured("ROLE_SIEMADMIN")
	public MsgRuleList uploadLocalRule(MsgRuleUploadRequest inMsg, Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("uploadLocalRule, incoming message " + inMsg.toString());
		}

		boolean success = ruleServ.uploadRule(inMsg.getEntityId(), inMsg.getUploadedRuleName());

		if(success) {
			List<CorrelationRule> ruleList = ruleServ.getRemoteRuleList();

			MsgRuleList msg = new MsgRuleList();
			msg.setRuleList(ruleList);

			if(LOG.isDebugEnabled()) {
				LOG.debug("uploadLocalRule, outgoing message: " + msg.toString());
			}

			return msg;
		}
		else {
			throw new ErrorMsgToFrontendException("Die Regel wurde abgelehnt, sie enthält individuelles Wissen!");
		}
	}


	@MessageMapping("/admin/rules/remote/download")
	@SendToUser("/queue/admin/rules/local/list")
	@Secured("ROLE_SIEMADMIN")
	public MsgRuleList downloadRemoteRule(MsgRuleDownloadRequest inMsg, Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("downloadRemoteRule, incoming message " + inMsg.toString());
		}

		ruleServ.downloadRule(inMsg.getDtoId(), inMsg.getDownloadedRuleName());

		List<CorrelationRule> ruleList = ruleServ.getLocalRuleList();

		MsgRuleList msg = new MsgRuleList();
		msg.setRuleList(ruleList);

		if(LOG.isDebugEnabled()) {
			LOG.debug("downloadRemoteRule, outgoing message: " + msg.toString());
		}

		return msg;
	}


	/**
	 * This method catches ErrorMsgToFrontendException objects thrown by the mapping methods.
	 * It sends an error notification to /queue/error (user specific) including the Exception
	 * message to notify the user of the error.
	 *
	 * @param ex Caught exception
	 * @return Error notification message
	 */
	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public MsgErrorNotification handleException(ErrorMsgToFrontendException ex) {
		LOG.error("ErrorMsgToFrontendException in RulesAdminController", ex);
		return new MsgErrorNotification(ex.getMessage());
	}


	/**
	 * This method catches ExternalServiceException objects thrown by the mapping methods.
	 * It sends an error notification to /queue/error (user specific) to notify the user of the failure.
	 *
	 * @param ex Caught exception
	 * @return Error notification message
	 */
	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public MsgErrorNotification handleException(ExternalServiceException ex) {
		LOG.error("ExternalServiceException in RulesAdminController", ex);
		return new MsgErrorNotification("Verbindung zu externem Dienst fehlgeschlagen, Vorgang konnte nicht ausgeführt werden!");
	}


	/**
	 * This method catches RuntimeException objects thrown by the mapping methods.
	 * It sends an error notification to /queue/error (user specific) to notify the user of the failure.
	 *
	 * @param ex Caught exception
	 * @return Error notification message
	 */
	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public MsgErrorNotification handleException(RuntimeException ex) {
		LOG.error("RuntimeException in RulesAdminController", ex);
		return new MsgErrorNotification("Es ist ein Fehler beim Verarbeiten des Vorgangs aufgetreten!");
	}
}
