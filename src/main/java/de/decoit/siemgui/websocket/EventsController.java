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

import de.decoit.siemgui.config.RootConfig.SystemConfig;
import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgErrorNotification;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgEventsList;
import de.decoit.siemgui.stomp.msgs.incoming.MsgEventsPageRequest;
import de.decoit.siemgui.exception.ExternalServiceException;
import de.decoit.siemgui.service.EventService;
import de.decoit.siemgui.service.PushNotificationService;
import de.decoit.siemgui.stomp.msgs.incoming.MsgEventsPaginationRequest;
import de.decoit.siemgui.stomp.msgs.outgoing.MsgEventsPagination;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
 * This controller provides mappings to STOMP message channels to request event information.
 * All operations are associated with the "events" view.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Controller
public class EventsController {
	private final Logger LOG = LoggerFactory.getLogger(EventsController.class.getName());
	private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private EventService eventServ;

	@Autowired
	private SystemConfig sysConf;


	/**
	 * This mapping responds with a list containing the events in the requested timeframe.
	 *
	 * @param reqMsg    Inbound message containing the timeframe
	 * @param principal Authentication information provided by Spring Security
	 * @return STOMP message containing the event list
	 *
	 * @throws ExternalServiceException if contacting external services failed
	 */
//	@MessageMapping("/events/reqlist")
//	@SendToUser("/queue/events/list")
//	@Secured("ROLE_SIEM_USER")
//	public MsgEventsList requestEventsList(MsgEventsListRequest reqMsg, Principal principal) throws ExternalServiceException {
//		if(LOG.isDebugEnabled()) {
//			LOG.debug("requestEventsList, incoming message: " + reqMsg.toString());
//		}
//
//		MsgEventsList msg = new MsgEventsList();
//
//		//List<Event> eventsList = eventServ.getEventsInTimeframe(from, to);
//		List<Event> eventsList = eventServ.getEventsInIdRange(reqMsg.getFromId(), reqMsg.getToId());
//
//		msg.setFromId(reqMsg.getFromId());
//		msg.setToId(reqMsg.getToId());
//		msg.setEventList(eventsList);
//
//		if(LOG.isDebugEnabled()) {
//			LOG.debug("requestEventsList, outgoing message: " + msg.toString());
//		}
//
//		return msg;
//	}


//	@MessageMapping("/events/reqboundaries")
//	@SendToUser("/queue/events/boundaries")
//	@Secured("ROLE_SIEM_USER")
//	public MsgEventBoundaries requestEventBoundaries(MsgEventBoundariesRequest reqMsg, Principal principal) throws ExternalServiceException {
//		if(LOG.isDebugEnabled()) {
//			LOG.debug("requestEventBoundaries, incoming message: " + reqMsg.toString());
//		}
//
//		MsgEventBoundaries msg = new MsgEventBoundaries();
//
//		LocalDateTime from = LocalDateTime.parse(reqMsg.getFromDate(), dateFormat);
//		LocalDateTime to = LocalDateTime.parse(reqMsg.getToDate(), dateFormat);
//
//		Long[] boundaries = eventServ.getEventIdBoundariesForTimeframe(from, to);
//		msg.setLowestId(boundaries[0]);
//		msg.setHighestId(boundaries[1]);
//		msg.setTotal(boundaries[2]);
//
//		if(LOG.isDebugEnabled()) {
//			LOG.debug("requestEventBoundaries, outgoing message: " + msg.toString());
//		}
//
//		return msg;
//	}


	@MessageMapping("/events/pagination")
	@SendToUser("/queue/events/pagination")
	@Secured("ROLE_SIEM_USER")
	public MsgEventsPagination requestEventPagination(MsgEventsPaginationRequest inMsg, Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("requestEventPagination, incoming message: " + inMsg.toString());
		}

		LocalDateTime from = LocalDateTime.parse(inMsg.getFromDate(), dateFormat);
		LocalDateTime to = LocalDateTime.parse(inMsg.getToDate(), dateFormat);

		long eventCount = eventServ.countEventsInTimeframe(from, to);

		MsgEventsPagination msg = new MsgEventsPagination();
		msg.setTotal(eventCount);
		msg.setPageSize(sysConf.getEventsPageSize());

		if(LOG.isDebugEnabled()) {
			LOG.debug("requestEventPagination, outgoing message: " + msg.toString());
		}

		return msg;
	}


	@MessageMapping("/events/page")
	@SendToUser("/queue/events/page")
	@Secured("ROLE_SIEM_USER")
	public MsgEventsList requestEventPage(MsgEventsPageRequest inMsg, Principal principal) throws ExternalServiceException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("requestEventPage, incoming message: " + inMsg.toString());
		}

		LocalDateTime from = LocalDateTime.parse(inMsg.getFromDate(), dateFormat);
		LocalDateTime to = LocalDateTime.parse(inMsg.getToDate(), dateFormat);
		int pageIndex = inMsg.getPageIndex();

		List<Event> list = eventServ.getEventPage(from, to, pageIndex);

		MsgEventsList msg = new MsgEventsList();
		msg.setEventList(list);

		if(LOG.isDebugEnabled()) {
			LOG.debug("requestEventPage, outgoing message: " + msg.toString());
		}

		return msg;
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
		LOG.error("ExternalServiceException in EventsController", ex);
		return new MsgErrorNotification("Verbindung zu externem Dienst fehlgeschlagen, Ereignisse konnten nicht geladen werden!");
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
		LOG.error("RuntimeException in EventsController", ex);
		return new MsgErrorNotification("Es ist ein Fehler beim Laden von Ereignissen aufgetreten!");
	}
}
