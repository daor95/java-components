/**
 * 
 * This class is part of the Programming the Internet of Things
 * project, and is available via the MIT License, which can be
 * found in the LICENSE file at the top level of this repository.
 * 
 * Copyright (c) 2020 by Andrew D. King
 */ 

package programmingtheiot.part03.integration.connection;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;
import programmingtheiot.data.*;
import programmingtheiot.gda.connection.*;

/**
 * This test case class contains very basic integration tests for
 * MqttClientControlPacketTest. It should not be considered complete,
 * but serve as a starting point for the student implementing
 * additional functionality within their Programming the IoT
 * environment.
 *
 */
public class MqttClientControlPacketTest
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(MqttClientControlPacketTest.class.getName());
	
	
	// member var's
	
	private MqttClientConnector mqttClient = null;
	private int keepAliveSeconds;
	
	// test setup methods
	
	@Before
	public void setUp() throws Exception
	{
		this.mqttClient = new MqttClientConnector();
		mqttClient = new MqttClientConnector();
		keepAliveSeconds = ConfigUtil.getInstance().getInteger(
				ConfigConst.MQTT_GATEWAY_SERVICE,
				ConfigConst.KEEP_ALIVE_KEY,
				ConfigConst.DEFAULT_KEEP_ALIVE
		);
	}
	
	@After
	public void tearDown() throws Exception
	{
		if (mqttClient.isConnected()) {
			mqttClient.disconnectClient();
		}
	}
	
	// test methods
	
	@Test
	public void testConnectAndDisconnect()
	{
		// CONNECT → CONNACK
		assertTrue("Should connect on first call", mqttClient.connectClient());
		// calling again should warn and return false
		assertFalse("Should not connect when already connected", mqttClient.connectClient());

		// wait for keep‑alive cycle to trigger PINGREQ/PINGRESP
		try {
			Thread.sleep((keepAliveSeconds * 1000L) + 5_000L);
		} catch (InterruptedException ignored) {}

		// DISCONNECT
		assertTrue("Should disconnect when connected", mqttClient.disconnectClient());
		// and now should return false
		assertFalse("Should not disconnect when already disconnected", mqttClient.disconnectClient());

		_Logger.info("testConnectAndDisconnect() complete.");
	}
	
	@Test
	public void testServerPing()
	{
		assertTrue("connectClient must succeed", mqttClient.connectClient());
		assertTrue("isConnected must report true", mqttClient.isConnected());

		// sleep long enough to force a ping
		try {
			Thread.sleep((keepAliveSeconds * 1000L) + 5_000L);
		} catch (InterruptedException ignored) {}

		// after ping cycle, client should still be connected
		assertTrue("Client should still be connected after ping", mqttClient.isConnected());
		assertTrue("disconnectClient must succeed", mqttClient.disconnectClient());

		_Logger.info("testServerPing() complete.");
	}
	
	@Test
	public void testPubSub()
	{
		// array of QoS levels to exercise QoS 0, 1 and 2 flows
		int[] qosLevels = { 0, 1, 2 };

		assertTrue("connectClient must succeed", mqttClient.connectClient());

		for (int qos : qosLevels) {
			// SUBSCRIBE → SUBACK
			assertTrue(
					"subscribe should succeed for QoS " + qos,
					mqttClient.subscribeToTopic(
							ResourceNameEnum.CDA_MGMT_STATUS_MSG_RESOURCE,
							qos
					)
			);

			// give the broker a moment before publishing
			try {
				Thread.sleep(1_000L);
			} catch (InterruptedException ignored) {}

			// PUBLISH (+ PUBACK if QoS 1, or full QoS 2 handshake)
			assertTrue(
					"publish should succeed for QoS " + qos,
					mqttClient.publishMessage(
							ResourceNameEnum.CDA_MGMT_STATUS_MSG_RESOURCE,
							"Test message: QoS " + qos,
							qos
					)
			);

			// UNSUBSCRIBE → UNSUBACK
			assertTrue(
					"unsubscribe should succeed for QoS " + qos,
					mqttClient.unsubscribeFromTopic(
							ResourceNameEnum.CDA_MGMT_STATUS_MSG_RESOURCE
					)
			);
		}

		assertTrue("disconnectClient must succeed", mqttClient.disconnectClient());
		_Logger.info("testPubSub() complete.");
	}
		// IMPORTANT: be sure to use QoS 1 and 2 to see ALL control packets
	
}
