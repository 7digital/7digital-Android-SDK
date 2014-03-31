package uk.co.sevendigital.android.sdk.model;

import uk.co.sevendigital.android.sdk.model.base.Track;

public interface SCMCloudTrack extends Track {

	/**
	 * Get the external (sdi) id of the track as stored on the server.
	 *
	 * @return Return the external (sdi) id of the track as stored on the server.
	 */
	public abstract long getSdiId();

	public abstract String getVersion();

	public abstract long getReleaseSdiId();

	public abstract String getReleaseTitle();

	public abstract String getReleaseArtist();

	public abstract String getReleaseVersion();

	public abstract String getSource();

	public abstract String getAudioUrl();

	public abstract String getUser();

	public abstract String getImageUrl();
}
