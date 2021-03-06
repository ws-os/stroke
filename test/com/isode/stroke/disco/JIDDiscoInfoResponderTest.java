/*
 * Copyright (c) 2010 Isode Limited.
 * All rights reserved.
 * See the COPYING file for more information.
 */
/*
 * Copyright (c) 2015 Tarun Gupta.
 * Licensed under the simplified BSD license.
 * See Documentation/Licenses/BSD-simplified.txt for more information.
 */

package com.isode.stroke.disco;

import com.isode.stroke.elements.ErrorPayload;
import com.isode.stroke.elements.DiscoInfo;
import com.isode.stroke.elements.IQ;
import com.isode.stroke.queries.IQRouter;
import com.isode.stroke.queries.DummyIQChannel;
import com.isode.stroke.disco.DiscoInfoResponder;
import com.isode.stroke.jid.JID;
import java.util.Vector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;

public class JIDDiscoInfoResponderTest {

	private IQRouter router_;
	private DummyIQChannel channel_;

	@Before
	public void setUp() {
		channel_ = new DummyIQChannel();
		router_ = new IQRouter(channel_);
	}

	@Test
	public void testHandleRequest_GetToplevelInfo() {
		JIDDiscoInfoResponder testling = new JIDDiscoInfoResponder(router_);
		testling.start();
		DiscoInfo discoInfo = new DiscoInfo();
		discoInfo.addFeature("foo");
		testling.setDiscoInfo(new JID("foo@bar.com/baz"), discoInfo);

		DiscoInfo query = new DiscoInfo();
		channel_.onIQReceived.emit(IQ.createRequest(IQ.Type.Get, new JID("foo@bar.com/baz"), "id-1", query));

		assertEquals(1, channel_.iqs_.size());
		DiscoInfo payload = channel_.iqs_.get(0).getPayload(new DiscoInfo());
		assertNotNull(payload);
		assertEquals("", payload.getNode());
		assertTrue(payload.hasFeature("foo"));

		testling.stop();
	}

	@Test
	public void testHandleRequest_GetNodeInfo() {
		JIDDiscoInfoResponder testling = new JIDDiscoInfoResponder(router_);
		testling.start();
		DiscoInfo discoInfo = new DiscoInfo();
		discoInfo.addFeature("foo");
		testling.setDiscoInfo(new JID("foo@bar.com/baz"), discoInfo);
		DiscoInfo discoInfoBar = new DiscoInfo();
		discoInfoBar.addFeature("bar");
		testling.setDiscoInfo(new JID("foo@bar.com/baz"), "bar-node", discoInfoBar);

		DiscoInfo query = new DiscoInfo();
		query.setNode("bar-node");
		channel_.onIQReceived.emit(IQ.createRequest(IQ.Type.Get, new JID("foo@bar.com/baz"), "id-1", query));

		assertEquals(1, channel_.iqs_.size());
		DiscoInfo payload = channel_.iqs_.get(0).getPayload(new DiscoInfo());
		assertNotNull(payload);
		assertEquals("bar-node", payload.getNode());
		assertTrue(payload.hasFeature("bar"));

		testling.stop();
	}

	@Test
	public void testHandleRequest_GetInvalidNodeInfo() {
		JIDDiscoInfoResponder testling = new JIDDiscoInfoResponder(router_);
		DiscoInfo discoInfo = new DiscoInfo();
		discoInfo.addFeature("foo");
		testling.setDiscoInfo(new JID("foo@bar.com/baz"), discoInfo);
		testling.start();

		DiscoInfo query = new DiscoInfo();
		query.setNode("bar-node");
		channel_.onIQReceived.emit(IQ.createRequest(IQ.Type.Get, new JID("foo@bar.com/baz"), "id-1", query));

		assertEquals(1, channel_.iqs_.size());
		ErrorPayload payload = channel_.iqs_.get(0).getPayload(new ErrorPayload());
		assertNotNull(payload);

		testling.stop();
	}

	@Test
	public void testHandleRequest_GetUnknownJID() {
		JIDDiscoInfoResponder testling = new JIDDiscoInfoResponder(router_);
		DiscoInfo discoInfo = new DiscoInfo();
		discoInfo.addFeature("foo");
		testling.setDiscoInfo(new JID("foo@bar.com/baz"), discoInfo);
		testling.start();

		DiscoInfo query = new DiscoInfo();
		channel_.onIQReceived.emit(IQ.createRequest(IQ.Type.Get, new JID("foo@bar.com/fum"), "id-1", query));

		assertEquals(1, channel_.iqs_.size());
		ErrorPayload payload = channel_.iqs_.get(0).getPayload(new ErrorPayload());
		assertNotNull(payload);

		testling.stop();
	}
}