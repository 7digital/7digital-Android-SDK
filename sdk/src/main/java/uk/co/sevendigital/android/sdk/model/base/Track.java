package uk.co.sevendigital.android.sdk.model.base;

import android.os.Parcelable;

import java.io.Serializable;

import uk.co.sevendigital.android.sdk.api.object.SDIArtist;

public interface Track extends Parcelable, Serializable {

	/**
	 * Get the title of the track.
	 *
	 * @return Return the title of the track.
	 */
	public abstract String getTitle();

	/**
	 * Get the mArtist of the track.
	 *
	 * @return Return the mArtist of the track.
	 */
	public abstract String getArtistName();

	public abstract SDIArtist getArtist();

}
