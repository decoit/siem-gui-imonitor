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

import de.decoit.siemgui.service.converter.IcingaEventManagementConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.decoit.siemgui.domain.events.Event;
import de.decoit.siemgui.domain.events.GeneralEvent;
import de.decoit.siemgui.domain.events.SnortEvent;
import de.decoit.siemgui.domain.events.SnortEventContent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@RunWith(MockitoJUnitRunner.class)
public class IcingaEventManagementConverterTest {
	@Spy
	private ObjectMapper mapper;

	@InjectMocks
	private IcingaEventManagementConverter converter;


	public IcingaEventManagementConverterTest() {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}


	/**
	 * Test of convertEvent method, of class IcingaEventManagementConverter.
	 * @throws java.net.UnknownHostException
	 */
	@Test
	public void testConvertEvent() throws UnknownHostException {
//		System.out.println("IcingaEventManagementConverter.convertEvent()");
//
//		IcingaEventLogData snortEvt = new IcingaEventLogData();
//		snortEvt.setTimestamp(1407919204L);
//		snortEvt.setData_time("2014-08-13 10:40:04");
//		snortEvt.setLog_entry("PASSIVE SERVICE CHECK: Snort;Alerts;0;{ \"Type\": \"Snort\", \"Timestamp\": \"2014-08-13 10:39:54\", \"Sensor-ID\": \"2\", \"Event-ID\": \"25477\", \"Signature\": \"4\", \"Signature-Name\": \"ICMP PING *NIX\", \"Sensor-Name\": \"siem-snort\", \"Interface\": \"eth0\", \"IP-src\": \"10.240.1.20\", \"IP-dest\": \"10.240.1.40\", \"TCP-src\": \"\", \"TCP-dest\": \"\", \"UDP-src\": \"\", \"UDP-dest\": \"\", \"ICMP-Type\": \"8\" }");
//
//		Event snortResult = converter.convertEvent(snortEvt);
//		if(snortResult instanceof SnortEvent) {
//			SnortEvent snortResultEvt = (SnortEvent) snortResult;
//			assertEquals("Snort event date mismatch", LocalDateTime.of(2014, Month.AUGUST, 13, 10, 40, 4), snortResultEvt.getDate());
//			assertEquals("Snort event type mismatch", "Snort", snortResultEvt.getType());
//
//			SnortEventContent sec = snortResultEvt.getContent();
//			assertEquals("Snort event content timestamp mismatch", LocalDateTime.of(2014, Month.AUGUST, 13, 10, 39, 54), sec.getTimestamp());
//			assertEquals("Snort event content sensor ID mismatch", 2L, sec.getSensorId());
//			assertEquals("Snort event content event ID mismatch", 25477L, sec.getEventId());
//			assertEquals("Snort event content signature mismatch", 4L, sec.getSignature());
//			assertEquals("Snort event content signature name ID mismatch", "ICMP PING *NIX", sec.getSignatureName());
//			assertEquals("Snort event content interface mismatch", "eth0", sec.getIfName());
//			assertEquals("Snort event content source IP mismatch", InetAddress.getByName("10.240.1.20"), sec.getSourceIp());
//			assertEquals("Snort event content destination IP mismatch", InetAddress.getByName("10.240.1.40"), sec.getDestinationIp());
//			assertEquals("Snort event content TCP source mismatch", null, sec.getTcpSourcePort());
//			assertEquals("Snort event content TCP destination mismatch", null, sec.getTcpDestinationPort());
//			assertEquals("Snort event content UDP source mismatch", null, sec.getUdpSourcePort());
//			assertEquals("Snort event content UDP destination mismatch", null, sec.getUdpDestinationPort());
//			assertEquals("Snort event content ICMP type mismatch", "8", sec.getIcmpType());
//		}
//		else {
//			fail("Snort event log entry was not converted to SnortEvent object");
//		}
//
//		IcingaEventLogData generalEvt = new IcingaEventLogData();
//		generalEvt.setTimestamp(1407916344L);
//		generalEvt.setData_time("2014-08-13 09:52:24");
//		generalEvt.setLog_entry("Auto-save of retention data completed successfully.");
//
//		Event generalResult = converter.convertEvent(generalEvt);
//		if(generalResult instanceof GeneralEvent) {
//			GeneralEvent generalResultEvt = (GeneralEvent) generalResult;
//			assertEquals("General event date mismatch", LocalDateTime.of(2014, Month.AUGUST, 13, 9, 52, 24), generalResultEvt.getDate());
//			assertEquals("General event type mismatch", "Allgemein", generalResultEvt.getType());
//			assertEquals("General event content mismatch", "Auto-save of retention data completed successfully.", generalResultEvt.getContent());
//		}
//		else {
//			fail("General event log entry was not converted to GeneralEvent object");
//		}
	}


	/**
	 * Test of convertEventList method, of class IcingaEventManagementConverter.
	 * @throws java.net.UnknownHostException
	 */
	@Test
	public void testConvertEventList() throws UnknownHostException {
//		System.out.println("IcingaEventManagementConverter.convertEventList()");
//		List<IcingaEventLogData> evtList = new ArrayList<>();
//
//		IcingaEventLogData snortEvt = new IcingaEventLogData();
//		snortEvt.setTimestamp(1407919204L);
//		snortEvt.setData_time("2014-08-13 10:40:04");
//		snortEvt.setLog_entry("PASSIVE SERVICE CHECK: Snort;Alerts;0;{ \"Type\": \"Snort\", \"Timestamp\": \"2014-08-13 10:39:54\", \"Sensor-ID\": \"2\", \"Event-ID\": \"25477\", \"Signature\": \"4\", \"Signature-Name\": \"ICMP PING *NIX\", \"Sensor-Name\": \"siem-snort\", \"Interface\": \"eth0\", \"IP-src\": \"10.240.1.20\", \"IP-dest\": \"10.240.1.40\", \"TCP-src\": \"\", \"TCP-dest\": \"\", \"UDP-src\": \"\", \"UDP-dest\": \"\", \"ICMP-Type\": \"8\" }");
//		evtList.add(snortEvt);
//
//		IcingaEventLogData generalEvt = new IcingaEventLogData();
//		generalEvt.setTimestamp(1407916344L);
//		generalEvt.setData_time("2014-08-13 09:52:24");
//		generalEvt.setLog_entry("Auto-save of retention data completed successfully.");
//		evtList.add(generalEvt);
//
//		List<Event> result = converter.convertEventList(evtList);
//
//		assertEquals("Result list size mismatch", 2, result.size());
//
//		Event snortResult = result.get(0);
//		if(snortResult instanceof SnortEvent) {
//			SnortEvent snortResultEvt = (SnortEvent) snortResult;
//			assertEquals("Snort event date mismatch", LocalDateTime.of(2014, Month.AUGUST, 13, 10, 40, 4), snortResultEvt.getDate());
//			assertEquals("Snort event type mismatch", "Snort", snortResultEvt.getType());
//
//			SnortEventContent sec = snortResultEvt.getContent();
//			assertEquals("Snort event content timestamp mismatch", LocalDateTime.of(2014, Month.AUGUST, 13, 10, 39, 54), sec.getTimestamp());
//			assertEquals("Snort event content sensor ID mismatch", 2L, sec.getSensorId());
//			assertEquals("Snort event content event ID mismatch", 25477L, sec.getEventId());
//			assertEquals("Snort event content signature mismatch", 4L, sec.getSignature());
//			assertEquals("Snort event content signature name ID mismatch", "ICMP PING *NIX", sec.getSignatureName());
//			assertEquals("Snort event content interface mismatch", "eth0", sec.getIfName());
//			assertEquals("Snort event content source IP mismatch", InetAddress.getByName("10.240.1.20"), sec.getSourceIp());
//			assertEquals("Snort event content destination IP mismatch", InetAddress.getByName("10.240.1.40"), sec.getDestinationIp());
//			assertEquals("Snort event content TCP source mismatch", null, sec.getTcpSourcePort());
//			assertEquals("Snort event content TCP destination mismatch", null, sec.getTcpDestinationPort());
//			assertEquals("Snort event content UDP source mismatch", null, sec.getUdpSourcePort());
//			assertEquals("Snort event content UDP destination mismatch", null, sec.getUdpDestinationPort());
//			assertEquals("Snort event content ICMP type mismatch", "8", sec.getIcmpType());
//		}
//		else {
//			fail("Snort event log entry was not converted to SnortEvent object");
//		}
//
//		Event generalResult = result.get(1);
//		if(generalResult instanceof GeneralEvent) {
//			GeneralEvent generalResultEvt = (GeneralEvent) generalResult;
//			assertEquals("General event date mismatch", LocalDateTime.of(2014, Month.AUGUST, 13, 9, 52, 24), generalResultEvt.getDate());
//			assertEquals("General event type mismatch", "Allgemein", generalResultEvt.getType());
//			assertEquals("General event content mismatch", "Auto-save of retention data completed successfully.", generalResultEvt.getContent());
//		}
//		else {
//			fail("General event log entry was not converted to GeneralEvent object");
//		}
	}
}
