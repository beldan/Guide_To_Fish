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

package beldan.restrung.async.loaders;

import java.util.concurrent.Callable;

import beldan.restrung.async.AsyncOperation;
import beldan.restrung.client.APICredentialsDelegate;
import beldan.restrung.client.APIDelegate;
import beldan.restrung.client.RestClientFactory;
import beldan.restrung.marshalling.request.JSONSerializable;
import beldan.restrung.marshalling.response.JSONResponse;

/**
 * Loader for DELETE requests
 *
 * @param <T> an implementer of JSONResponse
 */
public class APIDeleteLoader<T extends JSONResponse> extends APILoader<T> {

    /**
     * @param url                    the service endpoint
     * @param delegate               the delegate that will be notified on successful requests
     * @param apiCredentialsDelegate an optional delegate to handle invalid credentials
     * @param params                 the params to be replaced on the url placeholders
     */
    public APIDeleteLoader(String url, APIDelegate<T> delegate, APICredentialsDelegate apiCredentialsDelegate, JSONSerializable body, Object... params) {
        super(delegate, apiCredentialsDelegate, url, body, null, null, params);
    }

    @Override
    public Callable<T> getCallable() {
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                return getOperation().executeWithExceptionHandling(new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return RestClientFactory.getClient().delete(getOperation().getApiDelegate(), getOperation().getUrl(), getOperation().getBody(), AsyncOperation.DEFAULT_REQUEST_TIMEOUT);
                    }
                });
            }
        };
    }

}
