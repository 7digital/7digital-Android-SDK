package uk.co.sevendigital.android.sdk.api.request.charts;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistChartsRequest;
import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.api.object.SDIChartItem;
import uk.co.sevendigital.android.sdk.test.BuildConfig;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIGetArtistChartsJob extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testGetArtistCharts() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		SDIGetArtistChartsRequest.Result result = getArtistCharts(getContext(), 1, 20, "week", null);

		assertEquals(SDIGetArtistChartsRequest.ResultCode.SUCCESS, result.getResultCode());

		assertNotNull(result.getChart());

		assertNotNull(result.getChart().getItems());
		assertTrue(result.getChart().getItems().size() > 0);

		SDIChartItem<SDIArtist> artistChartItem = (SDIChartItem<SDIArtist>) result.getChart().getItems().get(0);
		assertNotNull(artistChartItem.getItem());
		assertNotNull(artistChartItem.getItem().getName());
		assertNotNull(artistChartItem.getItem().getExternalId());

		assertEquals(SDIGetArtistChartsRequest.ResultCode.SUCCESS, result.getResultCode());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetArtistChartsRequest.Result getArtistCharts(Context context, int page, int pageSize, String period, String toDate)
			throws SignatureException, InterruptedException, ExecutionException, IOException {

		//setup the api
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.artist().getCharts(page, pageSize, period, toDate);
	}

}
