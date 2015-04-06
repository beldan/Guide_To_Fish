/*
 * Copyright (C) 2012 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package beldan.guidetofish.Weather;

import beldan.restrung.client.APIDelegate;
import beldan.restrung.client.RestClient;
import beldan.restrung.client.RestClientFactory;

/**
 * @see WorldWeatherOnlineApiClient
 */
public class WorldWeatherOnlineApiClientImpl implements WorldWeatherOnlineApiClient {

    private RestClient client = RestClientFactory.getClient();

    private String endpoint = "http://api2.worldweatheronline.com/free/v2/weather.ashx?";
    private String marineEndpoint = "http://api2.worldweatheronline.com/free/v1/marine.ashx?";

    private String getEndpoint(String path) {
        return String.format("%s%s", endpoint, path);
    }
    private String getMarineEndpoint(String path) { return String.format("%s%s", marineEndpoint, path); }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void query(APIDelegate<WorldWeatherOnlineResponse> delegate, String apiKey, int numberOfDays, Query query) {
        client.getAsync(delegate, getEndpoint("key=%s&q=%s&num_of_days=%s&format=json"), apiKey, query.getValue(), String.valueOf(numberOfDays));
    }

    @Override
    public void marineQuery(APIDelegate<WorldWeatherMarineResponse> delegate, String apiKey, Query query) {
        client.getAsync(delegate, getMarineEndpoint("key=%s&format=json&q=%s"), apiKey, query.getValue());
    }


}
