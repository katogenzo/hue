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
package be.roam.hue.bean;

import be.roam.hue.util.DateTime;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link PropertyEvaluator}.
 * @author Kevin Wetzels
 */
public class PropertyEvaluatorTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
    private static PropertyEvaluator evaluator;
    private static final String YESTERDAY = DATE_FORMAT.format(new DateTime().addDays(-1).clearTime().getDate());
    private static final String TODAY = DATE_FORMAT.format(new DateTime().clearTime().getDate());
    private static final String TOMORROW = DATE_FORMAT.format(new DateTime().addDays(1).clearTime().getDate());
    private static final String YESTERDAY_WITH_TIME = DATE_FORMAT.format(new DateTime().addDays(-1).getDate());
    private static final String TODAY_WITH_TIME = DATE_FORMAT.format(new DateTime().getDate());
    private static final String TOMORROW_WITH_TIME = DATE_FORMAT.format(new DateTime().addDays(1).getDate());

    @Test
    public void plain() {
        assertEquals(YESTERDAY, evaluator.getValue("yesterday"));
        assertEquals(TODAY, evaluator.getValue("today"));
        assertEquals(TOMORROW, evaluator.getValue("tomorrow"));
        assertEquals(YESTERDAY_WITH_TIME, evaluator.getValue("yesterdayWithTime"));
        assertEquals(TODAY_WITH_TIME, evaluator.getValue("todayWithTime"));
        assertEquals(TOMORROW_WITH_TIME, evaluator.getValue("tomorrowWithTime"));
        assertEquals("Worth $50.0", evaluator.getValue("withDollarSign"));
        assertEquals("Yes, 1 <> 9", evaluator.getValue("placeholderY"));
        assertEquals("The truth about Bob and Alice is that they never met", evaluator.getValue("placeholderZzz"));
    }

    @Ignore("Still to cover")
    @Test
    public void todo() {
        assertEquals("Worth \\${50.0}", evaluator.getValue("escaped"));
        assertEquals("Worth $1", evaluator.getValue("placeholderX"));        
    }

    @BeforeClass
    public static void beforeClass() {
        Properties properties1 = new Properties();
        properties1.put("yesterday", "${hue.date.yesterday}");
        properties1.put("today", "${hue.date.today}");
        properties1.put("tomorrow", "${hue.date.tomorrow}");
        properties1.put("yesterdayWithTime", "${hue.datetime.yesterday}");
        properties1.put("todayWithTime", "${hue.datetime.now}");
        properties1.put("tomorrowWithTime", "${hue.datetime.tomorrow}");
        properties1.put("withDollarSign", "Worth $50.0");
        properties1.put("escaped", "Worth \\${50.0}");
        properties1.put("placeholderX", "Worth \\$${x}");
        properties1.put("placeholderY", "Yes, ${x} <> ${y}");
        properties1.put("placeholderZzz", "The truth about ${a} and ${b} is that they never met");
        properties1.put("override", "");
        Properties properties2 = new Properties();
        properties2.put("override", "Not empty");
        evaluator = new PropertyEvaluator(properties1, properties2);
        Map values = new HashMap();
        values.put("x", "1");
        values.put("y", "9");
        values.put("a", "Bob");
        values.put("b", "Alice");
        evaluator.addValues(values);
    }
}
