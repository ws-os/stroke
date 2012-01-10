/*
 * Copyright (c) 2012 Isode Limited, London, England.
 * All rights reserved.
 */
/*
 * Copyright (c) 2010 Remko Tronçon
 * All rights reserved.
 */

package com.isode.stroke.elements;

import java.util.ArrayList;
import java.util.List;

import com.isode.stroke.elements.FormField.HiddenFormField;

/**
 * XEP-0004 Data form. For the relevant Fields, the parsers and serialisers
 * protect the API user against the strange multi-value instead of newline thing
 * by transforming them.
 */
public class Form extends Payload {
    /**
     * Attribute "type"
     */
    public static final String FORM_ATTRIBUTE_TYPE = "type";

    /**
     * Element "title"
     */
    public static final String FORM_ELEMENT_TITLE = "title";

    /**
     * Element "instructions"
     */
    public static final String FORM_ELEMENT_INSTRUCTIONS = "instructions";

    /**
     * Element "field"
     */
    public static final String FORM_ELEMENT_FIELD = "field";

    /**
     * Type of form
     */
    public enum Type {
        /**
         * The form-processing entity is asking the form-submitting entity to
         * complete a form.
         */
        FORM_TYPE("form"),
        /**
         * The form-submitting entity is submitting data to the form-processing
         * entity.
         */
        SUBMIT_TYPE("submit"),
        /**
         * The form-submitting entity has cancelled submission of data to the
         * form-processing entity.
         */
        CANCEL_TYPE("cancel"),
        /**
         * The form-processing entity is returning data (e.g., search results)
         * to the form-submitting entity, or the data is a generic data set.
         */
        RESULT_TYPE("result");

        private String stringForm_;

        private Type(String stringForm) {
            stringForm_ = stringForm;
        }

        /**
         * Get type from its string form.
         *
         * @param stringForm String form of type, can be null
         *
         * @return Corresponding type if match found, otherwise
         *         {@link Type#FORM_TYPE}. Will never be null.
         */
        public static Type getType(String stringForm) {
            if (stringForm != null) {
                for (Type type : Type.values()) {
                    if (type.stringForm_.equals(stringForm)) {
                        return type;
                    }
                }
            }

            return FORM_TYPE;
        }

        /**
         * @return String form of type, will never be null
         */
        public String getStringForm() {
            return stringForm_;
        }
    };

    private List<FormField> fields_ = new ArrayList<FormField>();
    private String title_ = "";
    private String instructions_ = "";
    private Type type_;

    /**
     * Create a form of the given type.
     *
     * @param type Form type, must not be null
     */
    public Form(Type type) {
        setType(type);
    }

    /**
     * Create a form of {@link Type#FORM_TYPE}.
     */
    public Form() {
        this(Type.FORM_TYPE);
    }

    /**
     * Add to the list of fields for the form.
     *
     * @param field Field to add, must not be null. The instance of the form
     *            field is stored in the object, a copy is not made.
     */
    public void addField(FormField field) {
        if (field == null) {
            throw new NullPointerException("'field' must not be null");
        }

        fields_.add(field);
    }

    /**
     * @return List of fields for the form, will never be null. The instance of
     *         the list stored in the object is not returned, a copy is made.
     *         But the instances of the form fields stored in the list is the
     *         same as the list in the object, a copy is not made.
     */
    public List<FormField> getFields() {
        return new ArrayList<FormField>(fields_);
    }

    /**
     * Set title of the form.
     *
     * @param title title of the form, must not be null
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new NullPointerException("'title' must not be null");
        }

        title_ = title;
    }

    /**
     * @return title of the form, will never be null
     */
    public String getTitle() {
        return title_;
    }

    /**
     * Set natural-language instructions for the form.
     *
     * @param instructions instructions for the form, must not be null
     */
    public void setInstructions(String instructions) {
        if (instructions == null) {
            throw new NullPointerException("'instructions' must not be null");
        }

        instructions_ = instructions;
    }

    /**
     * @return natural-language instructions for the form, will never be null
     */
    public String getInstructions() {
        return instructions_;
    }

    /**
     * Set type of the form.
     *
     * @param type Form type, must not be null
     */
    public void setType(Type type) {
        if (type == null) {
            throw new NullPointerException("'type' must not be null");
        }

        type_ = type;
    }

    /**
     * @return type of the form, will never be null
     */
    public Type getType() {
        return type_;
    }

    /**
     * @return Value of the "FORM_TYPE" hidden form field if it is present in
     *         the form, an empty string otherwise, will never be null
     */
    public String getFormType() {
        String value = null;

        FormField field = getField("FORM_TYPE");
        try {
            HiddenFormField f = (HiddenFormField) field;
            if (f != null) {
                value = f.getValue();
            }
        } catch (ClassCastException e) {
            // value remains null
        }

        return ((value != null) ? value : "");
    }

    /**
     * Get form field for the given name.
     *
     * @param name Name of form field to retrieve, must not be null
     *
     * @return Form field with the given name if it is present in the form, null
     *         otherwise. The instance of the form field stored in the object is
     *         returned, a copy is not made.
     */
    public FormField getField(String name) {
        if (name == null) {
            throw new NullPointerException("'name' must not be null");
        }

        for (FormField field : fields_) {
            if (field.getName().equals(name)) {
                return field;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return Form.class.getSimpleName() + "\ntitle: " + title_
                + "\ninstructions: " + instructions_ + "\ntype: " + type_;
    }
}