/*
 * Copyright (c) 2010-2015, Isode Limited, London, England.
 * All rights reserved.
 */
package com.isode.stroke.parser.payloadparsers;

import com.isode.stroke.elements.Subject;
import com.isode.stroke.parser.AttributeMap;
import com.isode.stroke.parser.GenericPayloadParser;

public class SubjectParser extends GenericPayloadParser<Subject> {

    
    private int level_;
    private String text_ = "";

    public SubjectParser() {
        super(new Subject());
    }

    public void handleStartElement(String element, String ns, AttributeMap attributes) {
        ++level_;
    }

    public void handleEndElement(String element, String ns) {
        --level_;
        if (level_ == 0) {
            getPayloadInternal().setText(text_);
        }
    }

    public void handleCharacterData(String data) {
        text_ += data;
    }
}
