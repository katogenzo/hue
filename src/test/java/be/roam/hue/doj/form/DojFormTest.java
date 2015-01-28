/*
 *  Copyright 2009 Roam - roam.be.
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
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link DojForm}.
 * @author Kevin Wetzels
 */
public class DojFormTest {

    private HtmlPage page;
    private Doj onPage;
    private DojForm form;

    @Test
    public void hasLabel() {
        assertTrue(form.hasLabel(TestFormDefinition.KEYWORDS));
        assertTrue(form.hasLabel(TestFormDefinition.SITE));
        assertFalse(form.hasLabel(TestFormDefinition.TEXTBOX));
    }

    @Test
    public void label() {
        assertEquals("Check or uncheck", form.label(TestFormDefinition.CHECKER1).text());
        assertEquals("Site #1", form.label(TestFormDefinition.SITE).text());
        assertEquals("Site #2", form.label(TestFormDefinition.SITE).texts()[1]);
        assertTrue(form.label(TestFormDefinition.TEXTBOX).isEmpty());
    }

    @Test
    public void definitionMatches() {
        assertTrue(form.definitionMatches(TestFormDefinition.CHECKER1));
        assertTrue(form.definitionMatches(TestFormDefinition.TEXTBOX));
        assertTrue(form.definitionMatches(TestFormDefinition.PLAIN_SELECT));
        assertTrue(form.definitionMatches(TestFormDefinition.MULTIPLE_SELECT));
        assertFalse(form.definitionMatches(TestFormDefinition.INCORRECT_TEXTBOX));
        assertFalse(form.definitionMatches(TestFormDefinition.INCORRECT_PLAIN_SELECT));
        assertFalse(form.definitionMatches(TestFormDefinition.INCORRECT_MULTIPLE_SELECT));
    }

    private static enum TestFormDefinition implements FormFieldDefinition {

        KEYWORDS("keywords", FormFieldType.TEXTFIELD),
        SITE("site", FormFieldType.RADIOBUTTON),
        CHECKER1("checker1", FormFieldType.CHECKBOX),
        CHECKER2("checker2", FormFieldType.CHECKBOX),
        PLAIN_SELECT("plain_select", FormFieldType.SELECT),
        INCORRECT_PLAIN_SELECT("plain_select", FormFieldType.MULTI_SELECT),
        MULTIPLE_SELECT("multiple_select", FormFieldType.MULTI_SELECT),
        INCORRECT_MULTIPLE_SELECT("multiple_select", FormFieldType.SELECT),
        INCORRECT_TEXTBOX("textext", FormFieldType.TEXTFIELD),
        TEXTBOX("textext", FormFieldType.TEXTAREA);
        public final String htmlName;
        public final FormFieldType type;

        TestFormDefinition(String htmlName, FormFieldType type) {
            this.htmlName = htmlName;
            this.type = type;
        }

        public String getHtmlName() {
            return htmlName;
        }

        public FormFieldType getType() {
            return type;
        }
    }

    @Before
    public void before() throws Exception {
        WebClient client = new WebClient(BrowserVersion.FIREFOX_3);
        page = client.getPage(DojFormTest.class.getResource("/test.html"));
        onPage = Doj.on(page);
        form = new DojForm(page);
    }
}
