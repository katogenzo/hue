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

import java.util.Map;

/**
 * Factory to create POJOs from a property file.
 * @author Kevin Wetzels
 */
public abstract class PojoFromPropertiesFactory<PojoType> {

    public Map<String, PojoType> createAll(String prefix) {
        return null;
    }
}
