/*
 * Copyright (c) 2010 Remko Tron�on
 * Licensed under the GNU General Public License v3.
 * See Documentation/Licenses/GPLv3.txt for more information.
 */
/*
 * Copyright (c) 2010, Isode Limited, London, England.
 * All rights reserved.
 */
package com.isode.stroke.network;

import com.isode.stroke.base.ByteArray;
import com.isode.stroke.signals.Signal;
import com.isode.stroke.signals.Signal1;

public abstract class Connection {

    public enum Error {

        ReadError,
        WriteError
    };

    public Connection() {
    }

    public abstract void listen();

    public abstract void connect(HostAddressPort address);

    public abstract void disconnect();

    public abstract void write(ByteArray data);

    public abstract HostAddressPort getLocalAddress();
    public final Signal1<Boolean /*error*/> onConnectFinished = new Signal1<Boolean>();
    public final Signal1<Error> onDisconnected = new Signal1<Error>();
    public final Signal1<ByteArray> onDataRead = new Signal1<ByteArray>();
    public final Signal onDataWritten = new Signal();
}
