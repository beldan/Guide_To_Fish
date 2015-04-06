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

package beldan.restrung.async.asynctasks;

import android.os.AsyncTask;

import java.io.File;

import beldan.restrung.async.AsyncOperation;
import beldan.restrung.client.APICredentialsDelegate;
import beldan.restrung.client.APIDelegate;
import beldan.restrung.client.APIPostParams;
import beldan.restrung.marshalling.request.JSONSerializable;
import beldan.restrung.marshalling.response.JSONResponse;

/**
 * Abstract class for all AsyncTasks
 */
public abstract class APIAsyncTask<T extends JSONResponse> extends AsyncTask<String, Integer, T> {

    /**
     * Async operation delegate
     */
    private AsyncOperation<T> delegate;

    /**
     * Constructs an async task
     *
     * @param url                    the url
     * @param delegate               the API delegate
     * @param apiCredentialsDelegate the api credentials delegate
     * @param params                 a set of params to be replaced in the url
     */
    public APIAsyncTask(String url, APIDelegate<T> delegate, APICredentialsDelegate apiCredentialsDelegate, Object... params) {
        super();
        this.delegate = new AsyncOperation<T>(url, delegate, apiCredentialsDelegate, params);
    }

    /**
     * Constructs an async task
     *
     * @param url                    the url
     * @param body                   the object to be sent in the post or put body
     * @param file                   the file to be sent on a multipart
     * @param delegate               the API delegate
     * @param delegateParams         the post params to obtain progress and provide post related params
     * @param apiCredentialsDelegate the api credentials delegate
     * @param params                 a set of params to be replaced in the url
     */
    public APIAsyncTask(String url, JSONSerializable body, File file, APIDelegate<T> delegate, APIPostParams delegateParams, APICredentialsDelegate apiCredentialsDelegate, Object... params) {
        super();
        this.delegate = new AsyncOperation<T>(url, body, file, delegate, delegateParams, apiCredentialsDelegate, params);
    }

    /**
     * Handles the task results in the main thread
     *
     * @see android.os.AsyncTask#onPostExecute(Object)
     */
    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        delegate.onPostExecute(result);
    }

    /**
     * Wraps
     *
     * @see android.os.AsyncTask#execute(Object[])
     */
    public void execute() {
        super.execute();
    }

    /**
     * The async operation
     *
     * @return the async operation
     */
    public AsyncOperation<T> getOperation() {
        return delegate;
    }
}