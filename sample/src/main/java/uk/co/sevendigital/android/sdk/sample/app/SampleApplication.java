package uk.co.sevendigital.android.sdk.sample.app;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.sample.BuildConfig;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class SampleApplication extends Application {

	private static RequestQueue sRequestQueue;
	private static ImageLoader sImageLoader;
	private static SDIApi sSDIApi;
	private static SDIServerUtil.OauthConsumer sOauthConsumer;

	@Override public void onCreate() {
		super.onCreate();

		/* setup the volley request queue and image loader */
		sRequestQueue = Volley.newRequestQueue(this);
		int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = 1024 * 1024 * memClass / 8; // use 1/8th of the available memory for this memory cache
		sImageLoader = new ImageLoader(sRequestQueue, new VolleyUtil.BitmapLruCache(cacheSize));

		/* setup the api, with our oauth consumer key and secret*/
		sOauthConsumer = new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY, BuildConfig.TEST_OAUTH_CONSUMER_SECRET);
		sSDIApi = new SDIApi(this, sRequestQueue, sOauthConsumer);
	}

	/**
	 * Get the static Volley request queue
	 * @return {@link com.android.volley.RequestQueue}
	 */
	public static RequestQueue getRequestQueue() {
		return sRequestQueue;
	}

	/**
	 * Get the static Volley image loader
	 * @return {@link com.android.volley.toolbox.ImageLoader}
	 */
	public static ImageLoader getImageLoader() {
		return sImageLoader;
	}

	/**
	 * Get the Seven Digital Api
	 * @return {@link uk.co.sevendigital.android.sdk.api.SDIApi}
	 */
	public static SDIApi getApi() {
		return sSDIApi;
	}
}
