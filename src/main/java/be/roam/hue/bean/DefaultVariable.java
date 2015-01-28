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

import be.roam.hue.util.DateTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Enumeration of the default values available for placeholders.
 * @author Kevin Wetzels
 */
public enum DefaultVariable {

    HUE_DATE_YESTERDAY() {

        @Override
        public String getValue() {
            return DATE_FORMAT.format(new DateTime().addDays(-1).clearTime().getDate());
        }
    },
    HUE_DATE_TODAY() {

        @Override
        public String getValue() {
            return DATE_FORMAT.format(new DateTime().clearTime().getDate());
        }
    },
    HUE_DATE_TOMORROW() {

        @Override
        public String getValue() {
            return DATE_FORMAT.format(new DateTime().addDays(1).clearTime().getDate());
        }
    },
    HUE_DATETIME_YESTERDAY() {

        @Override
        public String getValue() {
            return DATE_FORMAT.format(new DateTime().addDays(-1).getDate());
        }
    },
    HUE_DATETIME_NOW() {

        @Override
        public String getValue() {
            return DATE_FORMAT.format(new DateTime().getDate());
        }
    },
    HUE_DATETIME_TOMORROW() {

        @Override
        public String getValue() {
            return DATE_FORMAT.format(new DateTime().addDays(1).getDate());
        }
    };
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
    public final String key;

    DefaultVariable() {
        key = name().toLowerCase().replace('_', '.');
    }

    public final String getKey() {
        return key;
    }

    public String getValue() {
        return null;
    }

    public static String[] keys() {
        DefaultVariable[] variables = values();
        String[] keys = new String[variables.length];
        for (int index = 0; index < variables.length; ++index) {
            keys[index] = variables[index].key;
        }
        return keys;
    }
}
