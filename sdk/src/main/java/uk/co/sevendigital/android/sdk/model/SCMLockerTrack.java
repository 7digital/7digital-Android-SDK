package uk.co.sevendigital.android.sdk.model;

import uk.co.sevendigital.android.sdk.model.base.Track;

public interface SCMLockerTrack extends Track {

	/**
	 * Get the id of the track as stored in the local locker (database).
	 * 
	 * @return Return id of the track as stored in the local locker (database).
	 */
	public abstract long getLockerId();

}
