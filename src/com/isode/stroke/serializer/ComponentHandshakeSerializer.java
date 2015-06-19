/*
 * Copyright (c) 2010-2014 Isode Limited.
 * All rights reserved.
 * See the COPYING file for more information.
 */
/*
 * Copyright (c) 2015 Tarun Gupta.
 * Licensed under the simplified BSD license.
 * See Documentation/Licenses/BSD-simplified.txt for more information.
 */

package com.isode.stroke.serializer;

import com.isode.stroke.serializer.GenericElementSerializer;
import com.isode.stroke.elements.ComponentHandshake;
import com.isode.stroke.elements.Element;

public class ComponentHandshakeSerializer extends GenericElementSerializer<ComponentHandshake> {

	public ComponentHandshakeSerializer() {
		super(ComponentHandshake.class);
	}

	public String serialize(Element element) {
		ComponentHandshake handshake = (ComponentHandshake)(element);
		return ("<handshake>" + handshake.getData() + "</handshake>");
	}
}