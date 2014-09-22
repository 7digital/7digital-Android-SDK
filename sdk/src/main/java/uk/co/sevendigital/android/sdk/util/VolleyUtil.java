
package uk.co.sevendigital.android.sdk.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.impl.cookie.DateUtils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.RequestFuture;

public class VolleyUtil {

    /**
     * Execute a server request that returns a string response using the given
     * request parameters. Execute the request inline (blocking) such that the
     * resulting string response is returned immediately.
     * 
     * @return Return the parsed string response from the server request.
     */
    public static CacheEntryResponse<String> executeStringRequest(CacheEntryRequestParams params)
            throws InterruptedException,
            ExecutionException {

        if (params == null)
            throw new IllegalArgumentException("params cannot be null");

        RequestFuture<CacheEntryResponse<String>> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(params, future, future);
        params.getRequestQueue().add(request);
        return future.get();
    }

    /**
     * Get the expiry timestamp (based on the time to live) at which point the
     * response cached (on disk) will have expired.<br/>
     * <br/>
     * Return -1 if the request has no entry cached.<br/>
     * <br/>
     * Note: The returned value can be compared to the current time {@link
     * System.#currentTimeMillis()} to see if the cached content requires to be
     * refreshed from the server.
     * 
     * @return Return the timestamp at which point the response cached (on disk)
     *         will have expired, or -1 if no response is cached.
     */
    public static long getCacheEntryExpiry(CacheEntryRequest<?> request) {
        if (request == null)
            throw new IllegalArgumentException("request cannot be null");

        Cache.Entry entry = request.getRequestParams().getRequestQueue().getCache()
                .get(request.getCacheKey());
        return entry != null ? entry.ttl : -1;
    }

    /**
     * The {@link StringRequest} class is an extension of Volley's
     * {@link com.android.volley.Request} class and is used to automatically unbox returned string
     * responses from the server.<br/>
     * <br/>
     * The {@link StringRequest} class extends from {@link CacheEntryRequest} so
     * that the cache entry associated with the response can be retrieved with
     * the returned string.
     */
    public static class StringRequest extends CacheEntryRequest<String> {

        public StringRequest(CacheEntryRequestParams params,
                Listener<CacheEntryResponse<String>> listener, ErrorListener error) {
            super(params, listener, error);
        }

        @Override
        public CacheEntryRequestParams getRequestParams() {
            return (CacheEntryRequestParams) super.getRequestParams();
        }

        @Override
        protected String parseNetworkResponseObject(NetworkResponse nr) throws VolleyError {
            try {
                return new String(nr.data, HttpHeaderParser.parseCharset(nr.headers));
            } catch (UnsupportedEncodingException e) {
                return new String(nr.data);
            }
        }
    }

    /**
     * The {@link CacheEntryRequest} class is an extension of Volley's
     * {@link com.android.volley.Request} class and is used to return a response that contains both
     * the information returned from the server and the cache entry associated
     * with the returned response.<br/>
     * <br/>
     * The cache entry is used by Volley to determine if a previously retrieved
     * response (cached on the disk) can be returned immediately without
     * contacting the server. The cache entry ttl (time to live) is determined
     * by the header values on the returned response. The time to live value of
     * response (stored within the cache entry) can be used determine if a
     * returned network response requires action or if it can be ignored because
     * the request has been processed before.<br/>
     * <br/>
     * For example: If a network request is to be made to update entries stored
     * in the database, the request can be sent to Volley for processing. If the
     * ttl (time to live) of the returned cache entry matches the ttl value at
     * which the database was most recently updated, the update can be ignored.<br/>
     * <br/>
     * By default, Volley reconstructs the cache entry from the cached network
     * response when a response is returned from disk. This results in a
     * different ttl value (in the cache entry) than previously returned (for
     * the same response). This class works around that limitation and returns a
     * cache entry with identical ttl.<br/>
     * <br/>
     * Note: Volley only uses the "expires" and "max-age" headers in the
     * response to decide on how long a piece of content should be cached. To
     * implement a custom caching mechanism, override the
     * {@link #parseCacheHeaders(com.android.volley.NetworkResponse)} method and set custom values
     * for the ttl (time to live) and soft ttl (soft time to live - the point at
     * which content should be prefetched).<br/>
     * <br/>
     * Note: The returned {@link CacheEntryResponse} is not guaranteed to have a
     * non-null cache entry. If the request should not explicitly cache the
     * result, the returned cache entry will be null (indicating the result was
     * not cached).
     *
     * @param <T> The type of the response returned from the server.
     */

    public static abstract class CacheEntryRequest<T> extends Request<CacheEntryResponse<T>> {
        private final RequestQueue mRequestQueue;
        private final CacheEntryRequestParams mRequestParams;
        private final Listener<CacheEntryResponse<T>> mListener;

        public CacheEntryRequest(CacheEntryRequestParams params,
                Listener<CacheEntryResponse<T>> listener, ErrorListener errorListener) {
            super(params.getMethod(), params.getUrl(), errorListener);
            mRequestQueue = params.getRequestQueue();
            mRequestParams = params;
            mListener = listener;

            setShouldCache(params.isUseCache());
            RetryPolicy retryPolicy = params.getRetryPolicy();
            if (retryPolicy != null)
                setRetryPolicy(retryPolicy);
        }

        public CacheEntryRequestParams getRequestParams() {
            return mRequestParams;
        }

        @Override
        public String getCacheKey() {
            String cacheKey = mRequestParams.getCacheKey();
            return cacheKey != null ? cacheKey : super.getCacheKey();
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = mRequestParams.getHeaders();
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = mRequestParams.getParams();
            return params != null ? params : super.getParams();
        }

        @Override
        public String getBodyContentType() {
            String contentType = mRequestParams.getBodyContentType();
            return contentType != null ? contentType : super.getBodyContentType();
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            byte[] body = mRequestParams.getBody();
            return body != null ? body : super.getBody();
        }

        @Override
        protected final Response<CacheEntryResponse<T>> parseNetworkResponse(NetworkResponse nr) {
            try {
                CacheEntryResponse<T> response = parseNetworkResponseCacheEntry(nr);
                return Response.success(response, response.getCacheEntry());
            } catch (VolleyError e) {
                return Response.error(e);
            }
        }

        protected final CacheEntryResponse<T> parseNetworkResponseCacheEntry(NetworkResponse nr)
                throws VolleyError {

            // cache the cache entry before the request is processed.
            // because volley batches requests with the same cache key, this
            // value is guaranteed to be identical to the cache entry as
            // processed in this individual request.

            Entry preCacheEntry = mRequestQueue.getCache().get(getCacheKey());

            // parse the network response to retrieve the response object
            T response = parseNetworkResponseObject(nr);

            // return a null cache entry response if the response should not be
            // cached
            if (!shouldCache())
                return new CacheEntryResponse<T>(nr, response, null);

            // parse the network response headers to construct the cache entry
            // if there was no existing entry cached
            if (preCacheEntry == null)
                return new CacheEntryResponse<T>(nr, response, parseCacheHeaders(nr));

            // return the previous cache entry if the entry was not expired
            // (hence retrieved from cache) or the server returned not modified
            if (!preCacheEntry.isExpired() || nr.notModified)
                return new CacheEntryResponse<T>(nr, response, preCacheEntry);

            // return a cache entry parsed from the network response (cache
            // entry updated)
            return new CacheEntryResponse<T>(nr, response, parseCacheHeaders(nr));
        }

        protected abstract T parseNetworkResponseObject(NetworkResponse nr) throws VolleyError;

        protected Cache.Entry parseCacheHeaders(NetworkResponse nr) {
            return HttpHeaderParser.parseCacheHeaders(nr);
        }

        @Override
        protected void deliverResponse(CacheEntryResponse<T> response) {
            mListener.onResponse(response);
        }
    }

    /**
     * The {@link CacheEntryResponse} is the object returned from a
     * {@link CacheEntryRequest}.<br/>
     * <br/>
     * The {@link CacheEntryResponse} includes both the information returned
     * from the server and the cache entry associated with the returned
     * response.<br/>
     * <br/>
     * Note: The returned {@link CacheEntryResponse} is not guaranteed to have a
     * non-null cache entry. If the request should not explicitly cache the
     * result, the returned cache entry will be null (indicating the result was
     * not cached).
     *
     * @param <T> The type of the object returned from the server.
     */

    public static class CacheEntryResponse<T> {
        private final NetworkResponse mNetworkResponse;
        private final Cache.Entry mCacheEntry;
        private final T mResponse;

        public CacheEntryResponse(NetworkResponse networkResponse, T response,
                Cache.Entry cacheEntry) {

            mNetworkResponse = networkResponse;
            mCacheEntry = cacheEntry;
            mResponse = response;
        }

        public NetworkResponse getNetworkResponse() {
            return mNetworkResponse;
        }

        public Cache.Entry getCacheEntry() {
            return mCacheEntry;
        }

        public T getResponse() {
            return mResponse;
        }
    }

    public static class CacheEntryRequestParams extends RequestParams {
        private final RequestQueue mQueue;

        public CacheEntryRequestParams(RequestQueue queue, int method, String url) {
            super(method, url);
            mQueue = queue;
        }

        public RequestQueue getRequestQueue() {
            return mQueue;
        }
    }

    /**
     * The {@link RequestParams} is a class used to pass in to Volley requests
     * that is used set provided values against the request. The following
     * values are required for request parameters:
     *
     * <pre>
     * 1. Method type (eg. GET, POST).
     * 2. Url
     * </pre>
     *
     * The following values can optionally be set on the request (through the
     * parameters):
     *
     * <pre>
     * 1. Headers - headers to include in the request.
     * 2. Parameters - parameters to include in the body of PUT or POST requests.
     * 3. Cache key - unique entry used to compare cached responses (by default the url of the request is used).
     * 4. Retry policy - policy used to retry the request in the event of network failure.
     * 5. Body content type - the type of content contained in the body for PUT or POST requests.
     * 6. Body - the body for PUT or POST requests (will override Parameters if set).
     * </pre>
     *
     * Note: The cache key should be overridden if the request url is not
     * appropriate for a unique key. For example, if the request includes a
     * timestamp (or nonce) that does not affect the response, the url would be
     * different for repeat requests, hence any cached value will be ignored.
     * For example, the following url could use the following as a cache key:
     *
     * <pre>
     * url:			http://www.example.com/details?page=1&count=10&timestamp=123456789&user=1234
     * cache key:	http://www.example.com/details?count=10&page=1&user=1234						// parameters ordered alphabetically
     * </pre>
     */

    public static class RequestParams {
        private final int mMethod;
        private final String mUrl;

        private boolean mUseCache = true;
        private RetryPolicy mRetryPolicy;

        private Map<String, String> mHeaders;
        private Map<String, String> mParams;
        private String mCacheKey;

        private String mBodyContentType;
        private byte[] mBody;

        public RequestParams(int method, String url) {
            mMethod = method;
            mUrl = url;
        }

        public int getMethod() {
            return mMethod;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setRetryPolicy(RetryPolicy policy) {
            mRetryPolicy = policy;
        }

        public RetryPolicy getRetryPolicy() {
            return mRetryPolicy;
        }

        public void setUseCache(boolean cache) {
            mUseCache = cache;
        }

        public boolean isUseCache() {
            return mUseCache;
        }

        public void addHeader(String key, String value) {
            if (mHeaders == null)
                mHeaders = new HashMap<String, String>();
            mHeaders.put(key, value);
        }

        public void setHeaders(Map<String, String> headers) {
            mHeaders = headers;
        }

        public Map<String, String> getHeaders() {
            return mHeaders;
        }

        public void addParam(String key, String value) {
            if (mParams == null)
                mParams = new HashMap<String, String>();
            mParams.put(key, value);
        }

        public void setParams(Map<String, String> params) {
            mParams = params;
        }

        public Map<String, String> getParams() {
            return mParams;
        }

        public void setCacheKey(String key) {
            mCacheKey = key;
        }

        public String getCacheKey() {
            return mCacheKey;
        }

        public String getBodyContentType() {
            return mBodyContentType;
        }

        public void setBodyContentType(String contentType) {
            mBodyContentType = contentType;
        }

        public byte[] getBody() {
            return mBody;
        }

        public void setBody(byte[] body) {
            mBody = body;
        }
    }

    /**
     * The {@link BitmapLruCache} is an implementation of {@link com.android.volley.toolbox.ImageLoader.ImageCache}
     * that stores bitmaps (in memory) to a maximum number of bytes.
     */

    public static class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {
        public BitmapLruCache(int maxBytes) {
            super(maxBytes);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }

    /**
     * The {@link SimpleImageListener} is an implementation of
     * {@link com.android.volley.toolbox.ImageLoader.ImageListener} with all methods implemented to default.
     */

    public static class SimpleImageListener implements ImageListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            // do nothing
        }

        @Override
        public void onResponse(ImageContainer response, boolean isImmediate) {
            // do nothing
        }
    }

    /**
     * Return a set of headers that can be attached to a {@link com.android.volley.NetworkResponse}
     * that denotes the response should be cached for the given length of time.
     * 
     * @param cacheDurationMillis The duration (in milliseconds) that the
     *            response should be cached for.
     * @return Return a set of headers that denotes a response with a known
     *         cache lifetime.
     */
    public static Map<String, String> getNetworkResponseHeaders(long cacheDurationMillis) {
        Map<String, String> map = new HashMap<String, String>();

        Calendar calendar = Calendar.getInstance();
        map.put("Date", DateUtils.formatDate(calendar.getTime()));
        calendar.add(Calendar.MINUTE, (int) (cacheDurationMillis / 60000));
        map.put("Expires", DateUtils.formatDate(calendar.getTime()));

        return map;
    }

}
