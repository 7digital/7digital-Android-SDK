package uk.co.sevendigital.android.sdk.model.base;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

import uk.co.sevendigital.android.sdk.api.object.SDIArtist;

public interface Release extends Parcelable, Serializable {


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
	public abstract SDIArtist getArtist();


	public abstract String getArtistName();

	/**
	 * Get the date of this release
	 * 
	 * @return Return release date.
	 */
	public abstract Date getReleaseDate();

	/**
	 * Get the number of tracks in this release
	 * 
	 * @return Return track count.
	 */
	public abstract int getTrackCount();

	/**
	 * Get the type of this release.
	 * 
	 * @return Return release type
	 */
	public abstract String getType();

	/**
	 * Get the year of this release.
	 * 
	 * @return Return release year
	 */
	public abstract int getYear();

}
