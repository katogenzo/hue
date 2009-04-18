/*
 * Copyright 2009 Roam - roam.be
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.roam.hue.doj;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for {@link Doj), API version 1.0.
 * @author Kevin Wetzels
 */
public class DojVersion1Dot0Test {

    private static HtmlPage page;
    private static Doj onPage;

    @Test
    public void getByIndex() {
        Doj doj = onPage.get("div");
        assertEquals("header", doj.get(1).id());
        assertEquals("footer", doj.get(-1).id());
        assertEquals("sidebar", doj.get(-2).id());
        assertTrue(doj.get(10654).isEmpty());
    }

    @Test
    public void getElement() {
        assertEquals("header", onPage.get("div").getElement(1).getId());
        assertNull(onPage.get("bdo").getElement(0));
    }

    @Test
    public void remove() {
        Doj doj = onPage.get("li");
        int expectedSize = doj.size() - 1;
        assertEquals(expectedSize, doj.remove(5).size());
        assertEquals(expectedSize, doj.remove(0).size());
        assertEquals(expectedSize, doj.remove(-1).size());
        assertEquals(expectedSize, doj.remove(1).size());
        assertEquals(expectedSize + 1, doj.remove(expectedSize + 1).size());
        assertEquals(expectedSize - 1, doj.remove(0).remove(0).size());
        assertTrue(doj.get("bdo").remove(0).isEmpty());
    }

    @Test
    public void get_selectorAndIndex() {
        assertEquals(1, onPage.get(".article", 0).size());
        assertEquals("article-1", onPage.get(".article", 0).id());
        assertEquals("article-2", onPage.get(".article", 1).id());
        assertEquals("article-3", onPage.get(".article", 2).id());
        assertEquals("article-3", onPage.get(".article", -1).id());
    }

    @Test
    public void getById() {
        assertEquals(1, onPage.getById("header").size());
        assertEquals(0, onPage.getById("THIS_ID_DOES_NOT_EXIST").size());
        assertEquals(1, onPage.getById("footer").size());
        // Following is expected behavior since the HtmlUnit docs say for
        // getElementById: "the element in this element's *page* with the
        // specified ID"
        assertEquals(1, onPage.getById("header").getById("header").size());
        assertEquals(1, onPage.getById("footer").getById("header").size());
    }

    @Test
    public void next() {
        assertTrue(onPage.get("div").next().is("div"));
        assertTrue(onPage.get("div").next().is("hr"));
        assertEquals("navigation", onPage.getById("header").next().id());
        assertEquals("content", onPage.getById("header").next().next().id());
        assertTrue(onPage.get("body").next().isEmpty());
    }

    @Test
    public void nextTag() {
        assertEquals("the_plain_select", onPage.getById("keywords").next("select").id());
        assertTrue(onPage.getById("keywords").next("bdo").isEmpty());
    }

    @Test
    public void previous() {
        assertTrue(onPage.get("div").previous().is("div"));
        assertTrue(onPage.get("div").previous().is("hr"));
        assertEquals("navigation", onPage.getById("content").previous().id());
        assertEquals("header", onPage.getById("content").previous().previous().id());
        assertTrue(onPage.get("head").previous().isEmpty());
    }

    @Test
    public void previousTag() {
        assertEquals("checker2", onPage.getById("the_multiple_select").previous("input").id());
        assertTrue(onPage.getById("keywords").previous("bdo").isEmpty());
    }

    @Test
    public void parent() {
        assertEquals("container", onPage.getById("content").parent().id());
        assertEquals(1, onPage.getById("content").parent().size());
        assertEquals(6, onPage.get("li").parent().size());
        assertEquals(5, onPage.get("li").parent().withTag("ol").size());
    }

    @Test
    public void parentTag() {
        assertEquals("sidebar", onPage.get("ol").withClass("ol-simple").get("li").parent("div").id());
        assertEquals(1, onPage.get("option").parent("form").size());
    }

    @Test
    public void unique() {
        assertEquals(5, onPage.get("div").get("ol").unique().size());
        // ...which - since uniaue() is applied automatically - is actually
        // equivalent to...
        assertEquals(5, onPage.get("div").get("ol").size());
    }

    @Test
    public void get() {
        assertEquals("Item #1", onPage.get("#content div li").get(0).trimmedText());
        Doj articleHeaderLinks = onPage.get("div.article h2 a");
        assertEquals(3, articleHeaderLinks.size());
        String[] texts = articleHeaderLinks.trimmedTexts();
        assertEquals("Article title 1", texts[0]);
        assertEquals("Article title 2", texts[1]);
        assertEquals("Article title 3", texts[2]);
        assertEquals("header", onPage.get("#header").id());
        assertEquals("navigation", onPage.get(".col-3.module").id());
        assertEquals("navigation", onPage.get(".module.col-3").id());
    }

    @Test
    public void getByTag() {
        assertEquals(5, onPage.getByTag("input").size());
        assertEquals(2, onPage.getByTag("select").size());
        assertTrue(onPage.getByTag("bdo").isEmpty());
    }

    @Test
    public void id() {
        assertEquals("container", onPage.get("div").get(0).id());
        assertEquals("navigation", onPage.get("div").get("div").get(1).id());
        assertEquals("", onPage.getById("article-1").get("div").id());
        assertNull("", onPage.get("bdo").id());
    }

    @Test
    public void ids() {
        String[] ids = onPage.get("div").slice(0, 5).ids();
        assertEquals(5, ids.length);
        assertEquals("container", ids[0]);
        assertEquals("header", ids[1]);
        assertEquals("navigation", ids[2]);
        assertEquals("content", ids[3]);
        assertEquals("main", ids[4]);
        assertEquals(0, onPage.get("bdo").ids().length);
    }

    @Test
    public void getByAttribute_Existing() {
        // Since the <html> element is the root in this case: only 1
        assertEquals(1, onPage.getByAttribute("lang", MatchType.EXISTING, null).size());
        assertTrue(onPage.getByAttribute("align", MatchType.EXISTING, "ol").isEmpty());
    }

    @Test
    public void getByAttribute_Matching() {
        assertEquals(1, onPage.getByAttribute("class", MatchType.EQUALS, "ol-simple").size());
        assertTrue(onPage.getByAttribute("class", MatchType.EQUALS, "ol").isEmpty());
    }

    @Test
    public void getByAttribute_ContainedWithWhitespace() {
        assertEquals(3, onPage.getByAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "ol-simple").size());
        assertEquals(1, onPage.getByAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "dummy").size());
    }

    @Test
    public void getByAttribute_StartingWith() {
        assertEquals(2, onPage.getByAttribute("class", MatchType.STARTING_WITH, "ol-simple").size());
        assertEquals(3, onPage.getByAttribute("class", MatchType.STARTING_WITH, "ol").size());
    }

    @Test
    public void getByAttribute_EndingWith() {
        assertEquals(2, onPage.getByAttribute("class", MatchType.ENDING_WITH, "imple").size());
        assertEquals(1, onPage.getByAttribute("class", MatchType.ENDING_WITH, "my").size());
    }

    @Test
    public void getByAttribute_Containing() {
        assertEquals(4, onPage.getByAttribute("class", MatchType.CONTAINING, "simple").size());
        assertEquals(1, onPage.getByAttribute("class", MatchType.CONTAINING, "module").size());
    }

    @Test
    public void getByAttribute_ContainedWithHyphens() {
        assertEquals(1, onPage.getByAttribute("lang", MatchType.CONTAINED_WITH_HYPHENS, "en").size());
        assertEquals(1, onPage.getByAttribute("lang", MatchType.CONTAINED_WITH_HYPHENS, "US").size());
        assertEquals(1, onPage.getByAttribute("class", MatchType.CONTAINED_WITH_HYPHENS, "simple").size());
    }

    @Test
    public void hasClass() {
        assertTrue(onPage.get(".article").hasClass("article"));
        assertTrue(onPage.get("div").hasClass("module"));
        assertTrue(onPage.get("#content").hasClass("col-3"));
        assertFalse(onPage.get("#content").hasClass("col-2"));
        assertFalse(onPage.get("#content").hasClass("col"));
    }

    @Test
    public void is() {
        assertTrue(onPage.get("div").is("div"));
        assertTrue(onPage.get("#article-1 p").parent().is("div"));
        assertTrue(onPage.get("#article-1 p").parent().is("blockquote"));
        assertFalse(onPage.get("#article-1 p").parent().get(0).is("blockquote"));
    }

    @Test
    public void withTag() {
        assertTrue(onPage.get("#article-1 p").parent().withTag("div").is("div"));
        assertFalse(onPage.get("#article-1 p").parent().withTag("div").is("blockquote"));
    }

    @Ignore("See withAttribute_Existing")
    @Test
    public void with() {
        // Ignore
    }

    @Ignore("See withAttribute_Matching")
    @Test
    public void withAttribute() {
        // Ignore
    }

    @Test
    public void withAttribute_Existing() {
        // Now the HTML element is part of the context
        assertEquals(1, onPage.withAttribute("lang", MatchType.EXISTING, null).size());
        assertEquals(3, onPage.get("div.article div").withAttribute("class", MatchType.EXISTING, null).size());
    }

    @Test
    public void withAttribute_Matching() {
        // Now the HTML element is part of the context
        assertEquals(1, onPage.withAttribute("lang", MatchType.EQUALS, "en-US").size());
        assertEquals(3, onPage.get("div.article div").withAttribute("class", MatchType.EQUALS, "content").size());
        assertEquals(0, onPage.get("div.article div").withAttribute("class", MatchType.EQUALS, " content").size());
    }

    @Test
    public void withAttribute_ContainedWithWhitespace() {
        // Now the HTML element is part of the context
        assertEquals(1, onPage.withAttribute("lang", MatchType.CONTAINED_WITH_WHITESPACE, "en-US").size());
        assertEquals(3, onPage.get("ol").withAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "ol-simple").size());
        assertEquals(1, onPage.get("ol").withAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "ol").size());
    }

    @Test
    public void withAttribute_StartingWith() {
        assertEquals(2, onPage.get("ol").withAttribute("class", MatchType.STARTING_WITH, "ol-simple").size());
        assertEquals(3, onPage.get("ol").withAttribute("class", MatchType.STARTING_WITH, "ol").size());
    }

    @Test
    public void withAttribute_EndingWith() {
        assertEquals(2, onPage.get("ol").withAttribute("class", MatchType.ENDING_WITH, "imple").size());
        assertEquals(1, onPage.get("ol").withAttribute("class", MatchType.ENDING_WITH, "my").size());
    }

    @Test
    public void withAttribute_Containing() {
        assertEquals(4, onPage.get("ol").withAttribute("class", MatchType.CONTAINING, "simple").size());
        assertEquals(1, onPage.get("div").withAttribute("class", MatchType.CONTAINING, "module").size());
    }

    @Test
    public void withAttribute_ContainedWithHyphens() {
        assertEquals(1, onPage.withAttribute("lang", MatchType.CONTAINED_WITH_HYPHENS, "en").size());
        assertEquals(1, onPage.withAttribute("lang", MatchType.CONTAINED_WITH_HYPHENS, "US").size());
        assertEquals(1, onPage.get("ol").withAttribute("class", MatchType.CONTAINED_WITH_HYPHENS, "simple").size());
    }

    @Ignore("See withAttribute_Containing")
    @Test
    public void withAttributeContaining() {
        // Ignore
    }

    @Ignore("See withAttribute_ContainedWithWhitespace")
    @Test
    public void withClass() {
        // Ignore
    }

    @Ignore("See withAttribute_Matching")
    @Test
    public void withId() {
        // Ignore
    }

    @Ignore("See withAttribute_Matching")
    @Test
    public void withType() {
        // Ignore
    }

    @Ignore("See withAttribute_Matching")
    @Test
    public void withName() {
        // Ignore
    }

    @Ignore("See hasAttribute_Existing")
    @Test
    public void hasAttribute() {
        // Ignore
    }

    @Ignore("See hasAttribute_Matching")
    @Test
    public void hasAttributeWithValue() {
        // Ignore
    }

    @Test
    public void hasAttribute_Existing() {
        Doj doj = onPage.get(".article");
        assertTrue(doj.hasAttribute("class", MatchType.EXISTING, null));
        assertFalse(doj.hasAttribute("bdo", MatchType.EXISTING, null));
    }

    @Test
    public void hasAttribute_Matching() {
        Doj doj = onPage.get(".article");
        assertTrue(doj.hasAttribute("class", MatchType.EQUALS, "article"));
        assertFalse(doj.hasAttribute("class", MatchType.EQUALS, " article"));
        assertTrue(onPage.get("div").hasAttribute("class", MatchType.EQUALS, "col-2"));
    }

    @Test
    public void hasAttribute_ContainedWithWhitespace() {
        assertTrue(onPage.get("div").hasAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "module"));
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "ol"));
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "ol-simple"));
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "dummy"));
        assertFalse(onPage.get("ol").hasAttribute("class", MatchType.CONTAINED_WITH_WHITESPACE, "nope"));
    }

    @Test
    public void hasAttribute_StartingWith() {
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.STARTING_WITH, "ol-simple"));
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.STARTING_WITH, "ol"));
        assertFalse(onPage.get("div").hasAttribute("class", MatchType.STARTING_WITH, "ola"));
    }

    @Test
    public void hasAttribute_EndingWith() {
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.ENDING_WITH, "imple"));
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.ENDING_WITH, "my"));
        assertFalse(onPage.get("ol").hasAttribute("class", MatchType.ENDING_WITH, "nono"));
    }

    @Test
    public void hasAttribute_Containing() {
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.CONTAINING, "simple"));
        assertTrue(onPage.get("div").hasAttribute("class", MatchType.CONTAINING, "module"));
        assertFalse(onPage.get("div").hasAttribute("class", MatchType.CONTAINING, "ol-simple"));
    }

    @Test
    public void hasAttribute_ContainedWithHyphens() {
        assertTrue(onPage.hasAttribute("lang", MatchType.CONTAINED_WITH_HYPHENS, "en"));
        assertTrue(onPage.hasAttribute("lang", MatchType.CONTAINED_WITH_HYPHENS, "US"));
        assertTrue(onPage.get("ol").hasAttribute("class", MatchType.CONTAINED_WITH_HYPHENS, "simple"));
        assertFalse(onPage.get("div").hasAttribute("class", MatchType.CONTAINED_WITH_HYPHENS, "simple"));
    }

    @Test
    public void text() {
        assertTrue(onPage.text().contains("Article title 2"));
        assertTrue(onPage.get("#article-2").text().contains("Article title 2"));
        assertFalse(onPage.get("#article-3").text().contains("Article title 2"));
    }

    @Test
    public void texts() {
        String[] texts = onPage.get(".article h2").texts();
        assertEquals(3, texts.length);
        assertTrue(texts[0].contains("Article title 1"));
        assertFalse(texts[0].contains("Article title 2"));
        assertFalse(texts[0].contains("Article title 3"));
        assertTrue(texts[1].contains("Article title 2"));
        assertFalse(texts[1].contains("Article title 1"));
        assertFalse(texts[1].contains("Article title 3"));
        assertTrue(texts[2].contains("Article title 3"));
        assertFalse(texts[2].contains("Article title 1"));
        assertFalse(texts[2].contains("Article title 2"));
    }

    @Test
    public void trimmedText() {
        assertEquals("Article title 2", onPage.get("#article-2 h2 a").trimmedText());
        assertEquals("Item #1", onPage.get("ol li").trimmedText());
    }

    @Test
    public void trimmedTexts() {
        String[] texts = onPage.get(".article h2").trimmedTexts();
        assertEquals(3, texts.length);
        assertTrue(texts[0].equals("Article title 1"));
        assertTrue(texts[1].equals("Article title 2"));
        assertTrue(texts[2].equals("Article title 3"));
    }

    @Test
    public void attribute() {
        assertEquals("header", onPage.get("#header").attribute("id"));
        assertEquals("article", onPage.get("#article-3").attribute("class"));
        assertEquals("google", onPage.get("input").withName("site").with("checked").attribute("value"));
        assertEquals("", onPage.get("#article-3").attribute("style"));
    }

    @Test
    public void attributes() {
        String[] values = onPage.get("input").withName("site").attributes("value");
        assertEquals(2, values.length);
        assertEquals("google", values[0]);
        assertEquals("thisone", values[1]);
        values = onPage.get("input").withName("site").attributes("checked");
        assertEquals(2, values.length);
        assertEquals("checked", values[0]);
        assertEquals("", values[1]);
    }

    @Test
    public void attribute_set() {
        HtmlElement element = page.getElementById("header");
        Doj doj = Doj.on(element);
        assertEquals("header", element.getId());
        assertEquals("header", doj.id());
        assertFalse(onPage.get("#header").isEmpty());
        assertEquals("not-header", doj.attribute("id", "not-header").id());
        assertEquals("not-header", element.getId());
        assertTrue(onPage.get("#header").isEmpty());
        // Clean up
        doj.attribute("id", "header");
    }

    @Test
    public void value() {
        // Get the selected value for the select
        assertEquals("4", onPage.get("select").withName("plain_select").value());
        // Get the first selected value for a multiple select
        assertEquals("2", onPage.get("select").withName("multiple_select").value());
        // Get the value of the option
        assertEquals("1", onPage.get("select").withName("plain_select").get("option").value());
        assertEquals(" The textarea content. ", onPage.get("textarea").value());
        assertEquals("Enter keywords here", onPage.get("#keywords").value());
        // Checkboxes and radiobuttons simply return the value of the first element,
        // not the first checked element
        assertEquals("google", onPage.get("input").withName("site").value());
    }

    @Test
    public void value_set() {
        assertEquals("4", onPage.get("#the_plain_select").value());
        assertEquals("2", onPage.get("#the_plain_select").value("2").value());
        // Reset
        onPage.get("#the_plain_select").value("4");

        String original = onPage.get("#keywords").value();
        assertEquals(original + original, onPage.get("#keywords").value(original + original).value());
        // Reset
        onPage.get("#keywords").value(original);

        original = onPage.get("textarea").value();
        String newValue = "This is the new content of the textarea. Yeah!";
        assertEquals(newValue, onPage.get("textarea").value(newValue).value());
        // Reset
        onPage.get("textarea").value(original);
    }

    @Test
    public void values() {
        String[] values = onPage.get("select").withName("multiple_select").values();
        assertEquals(2, values.length);
        assertEquals("2", values[0]);
        assertEquals("4", values[1]);
        values = onPage.get("input").withName("site").values();
        assertEquals(2, values.length);
        assertEquals("google", values[0]);
        assertEquals("thisone", values[1]);
        assertEquals(0, onPage.get("select").withName("that_does_not_exist").values().length);
    }

    @Test
    public void click() {
        // TODO
    }

    @Ignore("See plenty of other tests")
    @Test
    public void size() {
        // Ignore
    }

    @Ignore("See plenty of other tests")
    @Test
    public void isEmpty() {
        // Ignore
    }

    @Test
    public void first() {
        assertEquals(1, onPage.get(".article").first().size());
        assertEquals("article-1", onPage.get(".article").first().id());
    }

    @Test
    public void firstElement() {
        assertNotNull(onPage.get(".article").firstElement());
        assertEquals("article-1", onPage.get(".article").firstElement().getId());
    }

    @Test
    public void last() {
        assertEquals(1, onPage.get(".article").last().size());
        assertEquals("article-3", onPage.get(".article").last().id());
    }

    @Test
    public void lastElement() {
        assertNotNull(onPage.get(".article").lastElement());
        assertEquals("article-3", onPage.get(".article").lastElement().getId());
    }

    @Test
    public void slice() {
        assertEquals(3, onPage.get(".article").slice(0, 3).size());
        assertEquals(2, onPage.get(".article").slice(0, 2).size());
        assertEquals(1, onPage.get(".article").slice(0, 1).size());
        assertEquals(3, onPage.get(".article").slice(0, 16506).size());
        assertEquals(1, onPage.get(".article").slice(-1, 3).size());
        assertEquals(1, onPage.get(".article").slice(-1, 2).size());
        assertEquals(1, onPage.get(".article").slice(-1, 1).size());
        assertEquals(1, onPage.get(".article").slice(-1, 16506).size());
        assertEquals(2, onPage.get(".article").slice(-2, 3).size());
        assertEquals(1, onPage.get(".article").slice(-2, 1).size());
        assertEquals(1, onPage.get(".article").slice(-10306, 1).size());
        assertEquals(3, onPage.get(".article").slice(-10306, 3210650).size());
        assertEquals(1, onPage.get(".article").slice(10306, 1).size());
        assertEquals(1, onPage.get(".article").slice(10306, 103506).size());
        assertEquals("article-2", onPage.get(".article").slice(-2, 1).id());
        assertEquals("article-3", onPage.get(".article").slice(-1, 1).id());
        String[] ids = onPage.get(".article").slice(-2, 2).ids();
        assertEquals(2, ids.length);
        assertEquals("article-2", ids[0]);
        assertEquals("article-3", ids[1]);
    }

    @Ignore("See slice")
    @Test
    public void sliceElements() {
        // Ignore
    }

    @Test
    public void verifyNotEmpty_notEmpty() throws Exception {
        onPage.get("#container").verifyNotEmpty().get("div").verifyNotEmpty();
    }

    @Test(expected=DojIsEmptyException.class)
    public void verifyNotEmpty_empty() throws Exception {
        onPage.get("#does_not_exist").verifyNotEmpty();
    }

    @Test
    public void withTextContaining() {
        assertEquals(1, onPage.get("a").withTextContaining("Home").size());
        assertEquals("#about", onPage.get("a").withTextContaining("About").attribute("href"));        
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        WebClient client = new WebClient(BrowserVersion.FIREFOX_3);
        page = client.getPage(DojVersion1Dot0Test.class.getResource("/test.html"));
        onPage = Doj.on(page);
    }
}
