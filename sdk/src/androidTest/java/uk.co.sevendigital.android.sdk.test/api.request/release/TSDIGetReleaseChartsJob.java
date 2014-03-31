package uk.co.sevendigital.android.sdk.api.request.charts;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.BuildConfig;
import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseChartsRequest;
import uk.co.sevendigital.android.sdk.api.object.SDIChartItem;
import uk.co.sevendigital.android.sdk.api.object.SDIRelease;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIGetReleaseChartsJob extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testGetReleaseCharts() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		SDIGetReleaseChartsRequest.Result result = getReleaseCharts(getContext(), 1, 20, "week", null, false, null, "GB", null);

		assertEquals(SDIGetReleaseChartsRequest.ResultCode.SUCCESS, result.getResultCode());

		assertNotNull(result.getChart());

		assertNotNull(result.getChart().getItems());
		assertTrue(result.getChart().getItems().size() > 0);

		SDIChartItem<SDIRelease> releaseChart = (SDIChartItem<SDIRelease>) result.getChart().getItems().get(0);
		assertNotNull(releaseChart.getItem());
		assertNotNull(releaseChart.getItem().getTitle());
		assertNotNull(releaseChart.getItem().getArtist());
		assertNotNull(releaseChart.getItem().getArtistName());
		assertNotNull(releaseChart.getItem().getExternalId());
		assertNotNull(releaseChart.getItem().getType());
		assertNotNull(releaseChart.getItem().getPrice());
		assertNotNull(releaseChart.getItem().getPrice().getCurrencySymbol());
		assertNotNull(releaseChart.getItem().getPrice().getCurrencyCode());

		assertEquals(SDIGetReleaseChartsRequest.ResultCode.SUCCESS, result.getResultCode());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetReleaseChartsRequest.Result getReleaseCharts(Context context, int page, int pageSize, String period,
			String toDate, Boolean streamable, Integer licensorId, String country, Integer imageSize) throws SignatureException,
			InterruptedException, ExecutionException, IOException {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.release().getCharts(1, 20, "week", null, false, null, "GB", null);
	}

}
