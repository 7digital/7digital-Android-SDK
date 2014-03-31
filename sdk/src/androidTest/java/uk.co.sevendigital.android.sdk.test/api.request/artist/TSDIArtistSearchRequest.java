package uk.co.sevendigital.android.sdk.api.request.search;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIArtistSearchRequest;
import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.test.BuildConfig;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIArtistSearchRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testSearchArtist() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		SDIArtistSearchRequest.Result result = searchArtist(getContext(), "Mitch Murder");

		assertEquals(SDIArtistSearchRequest.ResultCode.SUCCESS, result.getResultCode());

		//make sure we have content
		assertNotNull(result.getSearchResult());
		assertNotNull(result.getSearchResult().getResults());
		assertNotNull(result.getSearchResult().getResults().size()>0);
		assertNotNull(result.getSearchResult().getResults().get(0).getType());
		assertNotNull(result.getSearchResult().getResults().get(0).getScore());
		assertNotNull(result.getSearchResult().getResults().get(0).getResult());
		assertTrue(result.getSearchResult().getResults().get(0).getResult() instanceof SDIArtist);
		assertNotNull(result.getSearchResult().getResults().get(0).getResult().getName());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIArtistSearchRequest.Result searchArtist(Context context, String query) throws SignatureException,
			InterruptedException, ExecutionException, IOException, Exception {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.artist().search(query);
	}

}
