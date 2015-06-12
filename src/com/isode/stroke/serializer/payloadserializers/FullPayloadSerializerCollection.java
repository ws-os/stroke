/*
 * Copyright (c) 2010-2015, Isode Limited, London, England.
 * All rights reserved.
 */
package com.isode.stroke.serializer.payloadserializers;

import com.isode.stroke.serializer.payloadserializers.MUCAdminPayloadSerializer;
import com.isode.stroke.serializer.payloadserializers.MUCDestroyPayloadSerializer;
import com.isode.stroke.serializer.payloadserializers.MUCInvitationPayloadSerializer;
import com.isode.stroke.serializer.payloadserializers.MUCOwnerPayloadSerializer;
import com.isode.stroke.serializer.payloadserializers.MUCPayloadSerializer;
import com.isode.stroke.serializer.payloadserializers.MUCUserPayloadSerializer;
import com.isode.stroke.serializer.PayloadSerializerCollection;

public class FullPayloadSerializerCollection extends PayloadSerializerCollection {

    public FullPayloadSerializerCollection() {
        /*FIXME: Implement what's needed. */
        //addSerializer(new IBBSerializer());
	addSerializer(new BodySerializer());
	addSerializer(new SubjectSerializer());
	addSerializer(new ChatStateSerializer());
	//addSerializer(new PrioritySerializer());
	addSerializer(new ErrorSerializer());
	addSerializer(new InBandRegistrationPayloadSerializer());
	addSerializer(new RosterSerializer());
	addSerializer(new MUCPayloadSerializer());
	addSerializer(new MUCDestroyPayloadSerializer());
	addSerializer(new MUCAdminPayloadSerializer());
	addSerializer(new MUCInvitationPayloadSerializer());
	addSerializer(new MUCOwnerPayloadSerializer(this));
	addSerializer(new MUCUserPayloadSerializer(this));
	addSerializer(new SoftwareVersionSerializer());
	addSerializer(new StatusSerializer());
	addSerializer(new StatusShowSerializer());
	addSerializer(new DiscoInfoSerializer());
	addSerializer(new DiscoItemsSerializer());
	addSerializer(new CapsInfoSerializer());
	addSerializer(new ResourceBindSerializer());
	addSerializer(new StartSessionSerializer());
	addSerializer(new SecurityLabelSerializer());
	addSerializer(new SecurityLabelsCatalogSerializer());
	//addSerializer(new StreamInitiationSerializer());
	addSerializer(new BytestreamsSerializer());
	addSerializer(new VCardSerializer());
	addSerializer(new VCardUpdateSerializer());
	addSerializer(new RawXMLPayloadSerializer());
	addSerializer(new StorageSerializer());
	addSerializer(new DelaySerializer());
        addSerializer(new FormSerializer());
	addSerializer(new PrivateStorageSerializer(this));
        addSerializer(new CommandSerializer());
	//addSerializer(new NicknameSerializer());
        addSerializer(new SearchPayloadSerializer());
        addSerializer(new ReplaceSerializer());
        addSerializer(new LastSerializer());
        addSerializer(new IdleSerializer());

        addSerializer(new DeliveryReceiptSerializer());
        addSerializer(new DeliveryReceiptRequestSerializer());

		addSerializer(new PubSubSerializer(this));
		addSerializer(new PubSubEventSerializer(this));
		addSerializer(new PubSubOwnerPubSubSerializer(this));
		addSerializer(new PubSubErrorSerializer());
		
        addSerializer(new ResultSetSerializer());
        addSerializer(new ForwardedSerializer(this));
        addSerializer(new MAMResultSerializer(this));
        addSerializer(new MAMQuerySerializer());
        addSerializer(new MAMFinSerializer());
        addSerializer(new UserTuneSerializer(this));
        addSerializer(new UserLocationSerializer(this));
    }

}
