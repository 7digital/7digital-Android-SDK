package uk.co.sevendigital.android.sdk.api.request.abs;

import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

public abstract class SDIAbsRequest<T> {

	protected static void addUserAgent(VolleyUtil.CacheEntryRequestParams params) {
		params.addHeader("User-Agent", SDIConstants.USER_AGENT);
	}
	//	protected static void addUserAgent(HttpClient client){
	//		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, SDIConstants.USER_AGENT);
	//	}

}
