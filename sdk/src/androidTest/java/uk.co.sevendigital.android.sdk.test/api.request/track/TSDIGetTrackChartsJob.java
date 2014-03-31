package uk.co.sevendigital.android.sdk.api.request.charts;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.BuildConfig;
import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackChartsRequest;
import uk.co.sevendigital.android.sdk.api.object.SDIChartItem;
import uk.co.sevendigital.android.sdk.api.object.SDITrack;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIGetTrackChartsJob extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testGetTrackCharts() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		SDIGetTrackChartsRequest.Result result = getTrackCharts(getContext(), 1, 20, "week", null, false, null, "GB", null);

		assertEquals(SDIGetTrackChartsRequest.ResultCode.SUCCESS, result.getResultCode());

		assertNotNull(result.getChart());

		assertNotNull(result.getChart().getItems());
		assertTrue(result.getChart().getItems().size() > 0);

		SDIChartItem<SDITrack> chartTrack = (SDIChartItem<SDITrack>) result.getChart().getItems().get(-0);

		assertNotNull(chartTrack.getItem().getTitle());
		assertNotNull(chartTrack.getItem().getArtist());
		assertNotNull(chartTrack.getItem().getArtist().getName());
		assertNotNull(chartTrack.getItem().getArtistName());

		assertEquals(SDIGetTrackChartsRequest.ResultCode.SUCCESS, result.getResultCode());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetTrackChartsRequest.Result getTrackCharts(Context context, int page, int pageSize, String period, String toDate,
			Boolean streamable, Integer licensorId, String country, Integer imageSize) throws SignatureException, InterruptedException,
			ExecutionException, IOException {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.track().getCharts(1, 20, "week", null, false, null, "GB", null);
	}

}
