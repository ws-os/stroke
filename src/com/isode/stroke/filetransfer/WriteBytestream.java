/*
 * Copyright (c) 2010-2016 Isode Limited.
 * All rights reserved.
 * See the COPYING file for more information.
 */
/*
 * Copyright (c) 2015 Tarun Gupta.
 * Licensed under the simplified BSD license.
 * See Documentation/Licenses/BSD-simplified.txt for more information.
 */

package com.isode.stroke.filetransfer;

import com.isode.stroke.base.ByteArray;
import com.isode.stroke.signals.Signal1;

public abstract class WriteBytestream {

    /**
     * Write data from a {@link ByteArray} to the bytestream.  On
     * success {@code true} is returned and {@link #onWrite} is called.
     * On failure {@code false} is returned.
     * @param b The {@link ByteArray} to write.
     * @return {@code true} on success, {@code false} on failure.
     */
	public abstract boolean write(final ByteArray b);

	public final Signal1<ByteArray> onWrite = new Signal1<ByteArray>();
}