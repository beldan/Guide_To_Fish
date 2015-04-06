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

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.concurrent.atomic.AtomicInteger;

import beldan.restrung.cache.CacheInfo;
import beldan.restrung.cache.RequestCache;
import beldan.restrung.exceptions.SerializationException;
import beldan.restrung.marshalling.request.RequestOperation;
import beldan.restrung.marshalling.response.JSONResponse;
import beldan.restrung.marshalling.response.ResponseOperation;

/**
 * Wraps the API delegate with commodity values for cache policies, etc...
 */
public abstract class ContextAwareAPIDelegate<Result extends JSONResponse> implements APIDelegate<Result>, ResponseTypeFactory {

    private static final AtomicInteger defaultOperationId = new AtomicInteger();

    private int operationId;

    /**
     * The context provider
     */
    private ContextProvider contextProvider;

    /**
     * The active cache loading policy for this delegate
     */
    private RequestCache.LoadPolicy cacheLoadPolicy = RequestCache.LoadPolicy.LOAD_IF_OFFLINE;

    /**
     * The active cache storage policy for this delegate
     */
    private RequestCache.StoragePolicy cacheStoragePolicy = RequestCache.StoragePolicy.PERMANENTLY;

    /**
     * The cache info fo this delegate
     */
    private CacheInfo cacheInfo;

    /**
     * the expected response type
     */
    private Class<Result> expectedResponseType;

    /**
     * Constructor for FragmentActivity, Activity and other Context implementers with default cache policies
     * @param context the context
     * @param target the expected response type
     */
    protected ContextAwareAPIDelegate(Context context, Class<Result> target) {
        this(context, target, null, null);
    }

    /**
     * Constructor for FragmentActivity, Activity and other Context implementers with specific cache policies
     * @param context the context
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     */
    protected ContextAwareAPIDelegate(Context context, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy) {
        this(context, target, cacheLoadPolicy, null);
    }

    /**
     * Constructor for FragmentActivity, Activity and other Context implementers with specific cache policies
     * @param context the context
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     * @param cacheStoragePolicy the cache storage policy
     */
    protected ContextAwareAPIDelegate(Context context, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        init(target, cacheLoadPolicy, cacheStoragePolicy);
        setContextProvider(DefaultContextProvider.get(context));
    }

    /**
     * Constructor for Fragments with default cache policies
     * @param fragment the fragment
     * @param target the expected response type
     */
    protected ContextAwareAPIDelegate(Fragment fragment, Class<Result> target) {
        this(fragment, target, null, null);
    }

    /**
     * Constructor for Fragments with specific cache policies
     * @param fragment the fragment
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     */
    protected ContextAwareAPIDelegate(Fragment fragment, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy) {
        this(fragment, target, cacheLoadPolicy, null);
    }

    /**
     * Constructor for Fragments with specific cache policies
     * @param fragment the fragment
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     * @param cacheStoragePolicy the cache storage policy
     */
    protected ContextAwareAPIDelegate(Fragment fragment, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        init(target, cacheLoadPolicy, cacheStoragePolicy);
        setContextProvider(FragmentContextProvider.get(fragment));
    }

    /**
     * Constructor for ContextProvider with default cache policies
     * @param contextProvider the context provider
     * @param target the expected response type
     */
    protected ContextAwareAPIDelegate(ContextProvider contextProvider, Class<Result> target) {
        this(contextProvider, target, null, null);
    }

    /**
     * Constructor for ContextProvider with specific cache policies
     * @param contextProvider the context provider
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     */
    protected ContextAwareAPIDelegate(ContextProvider contextProvider, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy) {
        this(contextProvider, target, cacheLoadPolicy, null);
    }

    /**
     * Constructor for ContextProvider with specific cache policies
     * @param contextProvider the context provider
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     * @param cacheStoragePolicy the cache storage policy
     */
    protected ContextAwareAPIDelegate(ContextProvider contextProvider, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        init(target, cacheLoadPolicy, cacheStoragePolicy);
        setContextProvider(contextProvider);
    }

    /**
     * Private helper that that all constructors delegate to and that initializes the delegate
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     * @param cacheStoragePolicy the cache storage policy
     */
    private void init(Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        if (cacheLoadPolicy != null) {
            this.cacheLoadPolicy = cacheLoadPolicy;
        }
        if (cacheStoragePolicy != null) {
            this.cacheStoragePolicy = cacheStoragePolicy;
        }
        this.expectedResponseType = target;
        this.operationId = defaultOperationId.incrementAndGet();
    }

    /**
     * @see APIDelegate#getOperationId()
     */
    @Override
    public int getOperationId() {
        return this.operationId;
    }

    /**
     * @see APIDelegate#onRequest(beldan.restrung.marshalling.request.RequestOperation)
     */
    @Override
    public void onRequest(RequestOperation operation) {
    }

    /**
     * @see APIDelegate#onResponse(beldan.restrung.marshalling.response.ResponseOperation)
     */
    @Override
    public void onResponse(ResponseOperation operation) {
    }

    /**
     * @see beldan.restrung.client.APIDelegate#getContextProvider()
     */
    @Override
    public ContextProvider getContextProvider() {
        return this.contextProvider;
    }

    /**
     * @see beldan.restrung.client.APIDelegate#setContextProvider(ContextProvider)
     */
    @Override
    public void setContextProvider(ContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    /**
     * @see APIDelegate#getCacheLoadPolicy()
     */
    public RequestCache.LoadPolicy getCacheLoadPolicy() {
        return cacheLoadPolicy;
    }

    /**
     * @see APIDelegate#setCacheLoadPolicy(beldan.restrung.cache.RequestCache.LoadPolicy)
     */
    public void setCacheLoadPolicy(RequestCache.LoadPolicy cacheLoadPolicy) {
        this.cacheLoadPolicy = cacheLoadPolicy;
    }

    /**
     * @see beldan.restrung.client.APIDelegate#getCacheStoragePolicy()
     */
    public RequestCache.StoragePolicy getCacheStoragePolicy() {
        return cacheStoragePolicy;
    }

    /**
     * @see beldan.restrung.client.APIDelegate#setCacheStoragePolicy(beldan.restrung.cache.RequestCache.StoragePolicy)
     */
    public void setCacheStoragePolicy(RequestCache.StoragePolicy cacheStoragePolicy) {
        this.cacheStoragePolicy = cacheStoragePolicy;
    }

    /**
     * gets the cache info
     * @return the cache info
     */
    public CacheInfo getCacheInfo() {
        return cacheInfo;
    }

    /**
     * Set the cache info object
     * @param cacheInfo the cache info object
     */
    public void setCacheInfo(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }

    /**
     * Gets the expected response type
     * @return the expected response type
     */
    public Class<Result> getExpectedResponseType() {
        return expectedResponseType;
    }

    /**
     * Sets the expected response type
     * @param expectedResponseType the expected response type
     */
    public void setExpectedResponseType(Class<Result> expectedResponseType) {
        this.expectedResponseType = expectedResponseType;
    }

    @Override
    public <T extends JSONResponse> T newInstance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (InstantiationException e) {
            throw new SerializationException(e);
        } catch (IllegalAccessException e) {
            throw new SerializationException(e);
        }
    }
}
