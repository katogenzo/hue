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
package be.roam.hue.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evaluates the values of properties.
 * @author Kevin Wetzels
 */
public class PropertyEvaluator {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

    private Map<Object, Object> properties;
    private Map<Object, Object> values;

    public PropertyEvaluator(Map<Object, Object>... properties) {
        this.properties = new HashMap<Object, Object>();
        this.values = new HashMap<Object, Object>();
        if (properties != null) {
            add(properties);
        }
        addDefaultValues();
    }

    public PropertyEvaluator add(Map<Object, Object>... propertiesArray) {
        for (Map<Object, Object> singleProperties : propertiesArray) {
            this.properties.putAll(singleProperties);
        }
        return this;
    }

    public PropertyEvaluator addValues(Map<Object, Object>... valuesArray) {
        for (Map<Object, Object> valueMap : valuesArray) {
            this.values.putAll(valueMap);
        }
        return this;
    }

    public String getValue(String key) {
        String stringValue = getStringValue(key);
        if (stringValue == null) {
            return null;
        }
        if (!stringValue.contains("$")) {
            return stringValue;
        }
        return getEvaluatedProperty(stringValue);
    }

    protected String getEvaluatedProperty(String stringValue) {        
        Matcher matcher = PATTERN.matcher(stringValue);
        StringBuffer buffer = new StringBuffer();
        boolean foundMatch = false;
        while (matcher.find()) {
            foundMatch = true;
            int start = matcher.start(1);
            int end = matcher.end(1);
            String key = stringValue.substring(start, end);
            System.out.println("Looking for value of " + key);
            Object object = values.get(key);
            matcher.appendReplacement(buffer, object == null ? "" : object.toString());
        }
        if (!foundMatch) {
            return stringValue;
        }
        return matcher.appendTail(buffer).toString();
    }
    
    protected String getStringValue(String key) {
        Object object = properties.get(key);
        return object == null ? null : object.toString();
    }

    protected PropertyEvaluator addDefaultValues() {
        for (DefaultVariable variable : DefaultVariable.values()) {
            values.put(variable.getKey(), variable.getValue());
        }
        return this;
    }

}
