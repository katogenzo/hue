/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.roam.hue.doj;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class DojCssSelectorTest {

    @Test
    public void simpleElement() {
        assertMatch("div", "div");
    }

    @Test
    public void simpleClass() {
        assertMatch(".something", ".something");
    }

    @Test
    public void simpleId() {
        assertMatch("#id-with-hyphens", "#id-with-hyphens");
    }

    @Test
    public void elementWithClass() {
        assertMatch("blockquote.special", "blockquote", ".special");
    }

    @Test
    public void elementWithId() {
        assertMatch("blockquote#special", "blockquote", "#special");
    }

    @Test
    public void elementWithIdAndClass() {
        assertMatch("blockquote#the_id.the_class", "blockquote", "#the_id", ".the_class");
        assertMatch("blockquote.the_class#the_id", "blockquote", ".the_class", "#the_id");
    }

    @Test
    public void withDescendants() {
        assertMatch("blockquote#the_id.the_class p.last.wow.oh-yeah a", "blockquote", "#the_id", ".the_class", " ", "p", ".last", ".wow", ".oh-yeah", " ", "a");
    }

    @Test
    public void withGroup() {
        final String groupedSelector = "blockquote#the_id.the_class p.last.wow.oh-yeah a  , div.totally p.rocks.your, div#socks a.off";
        assertMatch(groupedSelector, 0, "blockquote", "#the_id", ".the_class", " ", "p", ".last", ".wow", ".oh-yeah", " ", "a");
        assertMatch(groupedSelector, 1, "div", ".totally", " ", "p", ".rocks", ".your");
        assertMatch(groupedSelector, 2, "div", "#socks", " ", "a", ".off");
    }

    protected void assertMatch(String selectorUnderTest, String... expectedSelectors) {
        assertMatch(selectorUnderTest, 0, expectedSelectors);
    }

    protected void assertMatch(String selectorUnderTest, int index, String... expectedSelectors) {
        assertMatch(new DojCssSelector().compile(selectorUnderTest).get(index), expectedSelectors);
    }

    protected void assertMatch(List<DojCssSelector> selectors, String... expectedSelectors) {
        assertEquals(selectors.size(), expectedSelectors.length);
        for (int index = 0; index < selectors.size(); ++index) {
            DojCssSelector foundSelector = selectors.get(index);
            String expectedSelector = expectedSelectors[index];
            if (StringUtils.isBlank(expectedSelector)) {
                assertSame(DojCssSelector.Type.DESCENDANT, foundSelector.getType());
            } else if (expectedSelector.startsWith(".")) {
                assertSame(DojCssSelector.Type.HTML_CLASS, foundSelector.getType());
                assertEquals(expectedSelector.substring(1), foundSelector.getValue());
            } else if (expectedSelector.startsWith("#")) {
                assertSame(DojCssSelector.Type.ID, foundSelector.getType());
                assertEquals(expectedSelector.substring(1), foundSelector.getValue());
            } else {
                assertSame(DojCssSelector.Type.ELEMENT, foundSelector.getType());
                assertEquals(expectedSelector, foundSelector.getValue());
            }
        }
    }
}
