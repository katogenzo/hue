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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Base class for manipulating forms.
 * <p>
 * This type heavily interacts with {@link FormFieldDefinition} to strictly
 * define the possible operations. To make full use of this class, define an
 * enum implementing <code>FormFieldDefinition</code> containing the form
 * fields.
 * </p>
 * @author Kevin Wetzels
 */
public class DojForm<Field extends FormFieldDefinition> {

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
     * Shorthand for <code>hasLabel(field.getHtmlName())</code>.
     * @param field field to find a label for
     * @return true when it does have a label
     */
    public boolean hasLabel(Field field) {
        return hasLabel(field.getHtmlName());
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
     * Shorthand for <code>label(field.getHtmlName())</code>.
     * @param field field to find the label for
     * @return Doj instance containing the label
     */
    public Doj label(Field field) {
        return label(field.getHtmlName());
    }

    /**
     * Returns a Doj instance containing the label for the form element with the
     * given name or empty Doj when no such label exists.
     * @param name name of the form element
     * @return Doj instance containing the label
     */
    public Doj label(String name) {
        String[] ids = formElements.withName(name).ids();
        Doj label = Doj.EMPTY;
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            Doj temp = root.get("label").withAttribute("for", id);
            if (!temp.isEmpty()) {
                label = label.merge(temp);
            }
        }
        return label;
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
        if (type == FormFieldType.SELECT) {
            return !element.hasAttribute("multiple");
        }
        return true;
    }

    /**
     * Returns the value of the field.
     * <p>
     * In the case of radiobuttons, checkboxes and selects, the value of the
     * first selected option is returned.
     * </p>
     * @param field field to retrieve the value for
     * @return the value of the field or null if no value is selected in the
     * case of radiobuttons, checkboxes and selects
     */
    public String value(Field field) {
        Doj doj = get(field);
        FormFieldType type = field.getType();
        if (type == FormFieldType.RADIOBUTTON || type == FormFieldType.CHECKBOX) {
            for (Doj input : doj) {
                if (input.isChecked()) {
                    return input.value();
                }
            }
            return null;
        }
        return get(field).value();
    }

    /**
     * Returns the values of the matching fields.
     * <p>
     * In the case of radiobuttons and single selects, all values are returned.
     * In the case of multiple selects and checkboxes, all selected options are
     * returned.
     * </p>
     * @param field field to retrieve the values for
     * @return the values of the field
     */
    public String[] values(Field field) {
        Doj doj = get(field);
        FormFieldType type = field.getType();
        if (type != FormFieldType.CHECKBOX && type != FormFieldType.MULTI_SELECT) {
            return doj.values();
        }
        List<String> selectedValues = new ArrayList<String>();
        doj = (type == FormFieldType.CHECKBOX ? doj.with("checked") : doj.get("option").with("selected"));
        for (Doj selected : doj) {
            selectedValues.add(selected.value());
        }
        return selectedValues.toArray(new String[selectedValues.size()]);
    }

    /**
     * Sets the value for the given fields.
     * <p>
     * In the case of most fields, the value attribute is set to the
     * given value. In the case of textareas, the content is set to the given
     * value.
     * </p>
     * @param field field to set the value for
     * @param value value to set
     * @return current instance
     */
    public DojForm value(Field field, String value) {
        get(field).value(value);
        return this;
    }

    /**
     * Selects the option with the given value.
     * @param field the select
     * @param valueOfOptionToSelect the value of the option to select
     * @return current instance
     */
    public DojForm select(Field field, String valueOfOptionToSelect) {
        get(field).get("option").withValue(valueOfOptionToSelect).select();
        return this;
    }

    /**
     * Deselects the option with the given value.
     * @param field the select
     * @param valueOfOptionToSelect the value of the option to deselect
     * @return current instance
     */
    public DojForm deselect(Field field, String valueOfOptionToSelect) {
        get(field).get("option").withValue(valueOfOptionToSelect).deselect();
        return this;
    }

    /**
     * Checks the given field.
     * @param field field to check
     * @return current instance
     */
    public DojForm check(Field field) {
        get(field).check();
        return this;
    }

    /**
     * Unchecks the given field.
     * @param field field to uncheck
     * @return current instance
     */
    public DojForm uncheck(Field field) {
        get(field).uncheck();
        return this;
    }

    /**
     * Checks the given field with the given value.
     * @param field field to check
     * @param value value of the field to match
     * @return current instance
     */
    public DojForm check(Field field, String value) {
        get(field).withValue(value).check();
        return this;
    }

    /**
     * Unchecks the given field with the given value.
     * @param field field to uncheck
     * @param value value of the field to match
     * @return current instance
     */
    public DojForm uncheck(Field field, String value) {
        get(field).withValue(value).check();
        return this;
    }

    /**
     * Retrieves the Doj instance containing the matching fields.
     * @param field field to match
     * @return Doj instance containing the matched fields
     */
    public Doj get(Field field) {
        return formElements.withName(field.getHtmlName());
    }
}
