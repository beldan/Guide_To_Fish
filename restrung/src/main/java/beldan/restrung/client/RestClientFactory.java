/*
 * Copyright (C) 2012 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package beldan.restrung.client;

/**
 * Factory to obtain @see RestClient instances
 */
public class RestClientFactory {

    /**
     * the singleton shared instance
     */
    private final static RestClient instance = new DefaultRestClientImpl();

    /**
     * Static factory method to obtain RestClient instances
     *
     * @return a singleton instance of a RestClient based on @see DefaultRestClientImpl
     */
    public static RestClient getClient() {
        return instance;
    }

}
