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

import be.roam.hue.doj.Doj;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang.StringUtils;

/**
 * Base class for manipulating forms.
 * @author Kevin Wetzels
 */
public abstract class DojForm<Field extends FormFieldDefinition> {

    /**
     * The root object of the form - should be something like Doj.on("form#form_id")
     */
    protected Doj root;
    /**
     * The form elements inside the form.
     */
    protected Doj formElements;

    /**
     * Constructor.
     * @param root the Doj instance containing the form
     */
    public DojForm(Doj root) {
        this.root = root;
        this.formElements = root.get("input, select, button, textarea");
    }

    /**
     * Constructor.
     * <p>
     * Be careful when using this constructor and multiple forms are present
     * with inputs with the same name.
     * </p>
     * @param page page on which the form is present
     */
    public DojForm(HtmlPage page) {
        this(Doj.on(page).get("form"));
    }

    /**
     * Constructor.
     * @param page page containing the form
     * @param formNameOrId name or id of the form
     * @param useAsName use the formNameOrId parameter as the value of the name or the id attribute
     */
    public DojForm(HtmlPage page, String formNameOrId, boolean useAsName) {
        this(useAsName ? Doj.on(page).get("form").withName(formNameOrId) : Doj.on(page).getById(formNameOrId));
    }

    /**
     * Checks if the form element with the given name has an associated label
     * (i.e. the form element has an id and the value of a label's for attribute
     * is set to that id).
     * @param name name of the form element to check
     * @return true when it does have a label
     */
    public boolean hasLabel(String name) {
        return !label(name).isEmpty();
    }

    /**
     * Returns a Doj instance containing the label for the form element with the
     * given name or empty Doj when no such label exists.
     * @param name name of the form element
     * @return Doj instance containing the label
     */
    public Doj label(String name) {
        String[] ids = formElements.withName(name).ids();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            Doj label = root.get("label").withAttribute("for", id);
            if (!label.isEmpty()) {
                return label;
            }
        }
        return Doj.EMPTY;
    }

    /**
     * Checks if the form element identified by the definition actually matches
     * that definition (i.e. a textarea is a textarea, a radiobutton isn't
     * suddenly changed into a dropdown).
     * @param formField form field definition to check
     * @return true if the definition matches the situation on the page
     */
    public boolean definitionMatches(Field field) {
        Doj element = formElements.withName(field.getHtmlName());
        FormFieldType type = field.getType();
        if (!element.is(type.getTagName())) {
            return false;
        }
        if (type.isInput()) {
            return !element.withType(type.getTypeValue()).isEmpty();
        }
        if (type == FormFieldType.MULTI_SELECT) {
            return element.hasAttribute("multiple");
        }
        return true;
    }

    public String value(Field field) {
        // TODO Return the selected value
        return get(field).value();
    }

    public String[] values(Field field) {
        return get(field).values();
    }

    public Doj get(Field field) {
        return get(field.getHtmlName());
    }

    public Doj get(String name) {
        return formElements.withName(name);
    }
}
