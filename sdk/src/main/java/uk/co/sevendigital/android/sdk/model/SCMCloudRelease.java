package uk.co.sevendigital.android.sdk.model;

import uk.co.sevendigital.android.sdk.model.base.Track;

public interface SCMCloudRelease extends Track {

	/**
	 * Get the external (sdi) id of the track as stored on the server.
	 *
	 * @return Return the external (sdi) id of the track as stored on the server.
	 */
	public abstract long getSdiId();

	public abstract String getVersion();

	public abstract String getImageUrl();
}
