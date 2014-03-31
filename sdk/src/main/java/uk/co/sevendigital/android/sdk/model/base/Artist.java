package uk.co.sevendigital.android.sdk.model.base;

import android.os.Parcelable;

import java.io.Serializable;

public interface Artist extends Parcelable, Serializable {


	/**
	 * Get the name of the mArtist.
	 * 
	 * @return Return the name of the mArtist.
	 */
	public abstract String getName();

}
