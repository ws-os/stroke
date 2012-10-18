/*
 * Copyright (c) 2010, Isode Limited, London, England.
 * All rights reserved.
 */
/*
 * Copyright (c) 2010, Remko Tronçon.
 * All rights reserved.
 */
package com.isode.stroke.queries;

import java.util.Vector;

import com.isode.stroke.elements.ErrorPayload;
import com.isode.stroke.elements.IQ;
import com.isode.stroke.signals.Slot1;

/**
 * This class is responsible for routing all IQ stanzas to the handlers. It's
 * needed because, unlike Message and Presence, there are strict rules about
 * replying to IQ stanzas, replies need to be tracked, handled by the
 * responsible bit of code, replied to, etc. and when it's an outgoing IQ, it
 * needs to be tracked such that when the reply comes in, the callback is
 * called.
 */
public class IQRouter {

    private final Vector<IQHandler> handlers_ = new Vector<IQHandler>();
    private final IQChannel channel_;

    public IQRouter(IQChannel channel) {
        channel_ = channel;
        channel_.onIQReceived.connect(new Slot1<IQ>() {

            public void call(IQ p1) {
                handleIQ(p1);
            }
        });
    }

    public void addHandler(IQHandler handler) {
        synchronized (handlers_) {
            handlers_.add(handler);
        }
    }

    public void removeHandler(IQHandler handler) {
        synchronized (handlers_) {
            handlers_.remove(handler);
        }
    }

    public void sendIQ(IQ iq) {
        channel_.sendIQ(iq);
    }

    public String getNewIQID() {
        return channel_.getNewIQID();
    }

    public boolean isAvailable() {
        return channel_.isAvailable();
    }

    private void handleIQ(IQ iq) {
        boolean handled = false;
        synchronized (handlers_) {
            for (IQHandler handler : handlers_) {
                handled |= handler.handleIQ(iq);
                if (handled) {
                    break;
                }
            }
        }
        if (!handled && (iq.getType().equals(IQ.Type.Get) || iq.getType().equals(IQ.Type.Set))) {
            sendIQ(IQ.createError(iq.getFrom(), iq.getID(), ErrorPayload.Condition.FeatureNotImplemented, ErrorPayload.Type.Cancel));
        }
    }
}
