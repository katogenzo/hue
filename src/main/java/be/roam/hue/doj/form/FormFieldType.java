/*
 *  Copyright 2009 Roam - roam.be
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package be.roam.hue.doj.form;

/**
 * Enumeration of possible form field types.
 * @author Kevin Wetzels
 */
public enum FormFieldType {

    BUTTON("button"),
    TEXTAREA("textarea"),
    SELECT("select"),
    MULTI_SELECT("select"),
    TEXTFIELD("input", "text"),
    SEARCH("input", "search"),
    RADIOBUTTON("input", "radio"),
    CHECKBOX("input", "checkbox"),
    PASSWORD("input", "password"),
    SUBMIT_BUTTON("input", "submit"),
    RESET_BUTTON("input", "reset"),
    FILE("input", "file"),
    IMAGE("input", "image"),
    HIDDEN("input", "hidden");
    public final boolean input;
    public final String tagName;
    public final String typeValue;

    private FormFieldType(String tagName) {
        this(tagName, null);
    }

    private FormFieldType(String tagName, String typeValue) {
        this.tagName = tagName;
        this.typeValue = typeValue;
        this.input = (typeValue != null);
    }

    public final boolean isInput() {
        return input;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTypeValue() {
        return typeValue;
    }
}
