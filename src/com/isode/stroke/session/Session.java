/*
 * Copyright (c) 2010 Remko Tronçon
 * Licensed under the GNU General Public License v3.
 * See Documentation/Licenses/GPLv3.txt for more information.
 */
/*
 * Copyright (c) 2010-2011, Isode Limited, London, England.
 * All rights reserved.
 */
package com.isode.stroke.session;

import com.isode.stroke.base.ByteArray;
import com.isode.stroke.base.SafeByteArray;
import com.isode.stroke.elements.Element;
import com.isode.stroke.elements.ProtocolHeader;
import com.isode.stroke.elements.StreamType;
import com.isode.stroke.eventloop.EventLoop;
import com.isode.stroke.jid.JID;
import com.isode.stroke.network.Connection;
import com.isode.stroke.parser.PayloadParserFactoryCollection;
import com.isode.stroke.serializer.PayloadSerializerCollection;
import com.isode.stroke.signals.Signal1;
import com.isode.stroke.signals.Slot;
import com.isode.stroke.signals.Slot1;
import com.isode.stroke.signals.SignalConnection;
import com.isode.stroke.streamstack.ConnectionLayer;
import com.isode.stroke.streamstack.StreamStack;
import com.isode.stroke.streamstack.XMPPLayer;

public abstract class Session {

    public enum SessionError {

        ConnectionReadError,
        ConnectionWriteError,
        XMLError,
        AuthenticationFailedError,
        NoSupportedAuthMechanismsError,
        UnexpectedElementError,
        ResourceBindError,
        SessionStartError,
        TLSError,
        ClientCertificateLoadError,
        ClientCertificateError
    };

    public Session(
            final Connection connection,
            final PayloadParserFactoryCollection payloadParserFactories,
            final PayloadSerializerCollection payloadSerializers) {
            this.connection = connection;
            this.payloadParserFactories = payloadParserFactories;
            this.payloadSerializers = payloadSerializers;
            xmppLayer = null;
            connectionLayer = null;
            streamStack = null;
            finishing = false;
    }
    

    public void startSession() {
        initializeStreamStack();
        handleSessionStarted();
    }

    public void finishSession() {
        if (finishing) {
            return;
        }
        finishing = true;
        if (xmppLayer != null) {
            xmppLayer.writeFooter();
        }
        connection.disconnect();
    }

    public void sendElement(Element stanza) {
        xmppLayer.writeElement(stanza);
    }

    public JID getLocalJID() {
        return localJID;
    }

    public JID getRemoteJID() {
        return remoteJID;
    }
    public final Signal1<Element> onElementReceived = new Signal1<Element>();
    public final Signal1<SessionError> onSessionFinished = new Signal1<SessionError>();
    public final Signal1<SafeByteArray> onDataWritten = new Signal1<SafeByteArray>();
    public final Signal1<SafeByteArray> onDataRead = new Signal1<SafeByteArray>();

    protected void setRemoteJID(JID j) {
        remoteJID = j;
    }

    protected void setLocalJID(JID j) {
        localJID = j;
    }

    protected void finishSession(SessionError error) {
        if (finishing) {
            return;
        }
        finishing = true;
        if (xmppLayer != null) {
            xmppLayer.writeFooter();
        }
        connection.disconnect();
    }

    protected void handleSessionStarted() {
    }

    protected void handleSessionFinished(SessionError error) {
    }

    protected abstract void handleElement(Element element);

    protected abstract void handleStreamStart(ProtocolHeader header);

    protected void initializeStreamStack() {
        xmppLayer = new XMPPLayer(payloadParserFactories, payloadSerializers, StreamType.ClientStreamType);
        xmppLayer.onStreamStart.connect(new Slot1<ProtocolHeader>() {

            public void call(ProtocolHeader header) {
                handleStreamStart(header);
            }
        });
        xmppLayer.onElement.connect(new Slot1<Element>() {

            public void call(Element p1) {
                handleElement(p1);
            }
        });
        xmppLayer.onError.connect(new Slot() {

            public void call() {
                finishSession(SessionError.XMLError);
            }
        });
        xmppLayer.onDataRead.connect(onDataRead);
        xmppLayer.onWriteData.connect(onDataWritten);
        onDisconnectedConnection = connection.onDisconnected.connect(new Slot1<Connection.Error>() {

            public void call(Connection.Error p1) {
                handleDisconnected(p1);
            }
        });
        connectionLayer = new ConnectionLayer(connection);
        streamStack = new StreamStack(xmppLayer, connectionLayer);
    }

    public XMPPLayer getXMPPLayer() {
        return xmppLayer;


    }

    public StreamStack getStreamStack() {
        return streamStack;
    }

    /*protected void setFinished();*/ /* This seems to be unused in Swiften*/

    private void handleDisconnected(Connection.Error connectionError) {
        onDisconnectedConnection.disconnect();
        if (connectionError != null) {
            switch (connectionError) {
                case ReadError:
                    handleSessionFinished(SessionError.ConnectionReadError);
                    onSessionFinished.emit(SessionError.ConnectionReadError);
                    break;
                case WriteError:
                    handleSessionFinished(SessionError.ConnectionWriteError);
                    onSessionFinished.emit(SessionError.ConnectionWriteError);
                    break;
            }
        }
        else {
            SessionError error = null;
            handleSessionFinished(error);
            onSessionFinished.emit(error);
        }
    }

    private JID localJID = new JID();
    private JID remoteJID = new JID();
    private Connection connection;
    private PayloadParserFactoryCollection payloadParserFactories;
    private PayloadSerializerCollection payloadSerializers;
    private XMPPLayer xmppLayer;
    private ConnectionLayer connectionLayer;
    private StreamStack streamStack;
    private boolean finishing;
    private SignalConnection onDisconnectedConnection;
}
