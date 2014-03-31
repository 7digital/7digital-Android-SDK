package uk.co.sevendigital.android.sdk.model;

import uk.co.sevendigital.android.sdk.model.base.Track;

public interface SCMDeviceTrack extends Track {

	/**
	 * Get the id of the device where the track originated.
	 *
	 * @return Return the id of the device where the track originated.
	 */
	public abstract String getDeviceId();

	/**
	 * Get the id of the track as stored in the media store of the original device.
	 *
	 * @return Return the id of the track as stored in the media store of the original device.
	 */
	public abstract long getMediaStoreId();

}
