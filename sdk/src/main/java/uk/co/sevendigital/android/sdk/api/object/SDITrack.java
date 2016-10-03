package uk.co.sevendigital.android.sdk.api.object;

import android.os.Parcel;

import java.util.Date;

import uk.co.sevendigital.android.sdk.model.base.Track;

public class SDITrack implements Track, android.os.Parcelable {
	private static final long serialVersionUID = 3946173619294217397L;

	public SDITrack() {}

	private String mExternalId;
	private Date mStreamingReleaseDate;
	private int mDuration;
	private String mTitle;
	private SDIRelease.SDIPrice mPrice;
	private boolean mExplicitContent;
	private int mTrackNumber;
	private SDIArtist mArtist;
	private int mDiscNumber;
	private SDIRelease mRelease;
	private String mUrl;
	private String mVersion;

	@Override public String getTitle() {
		return mTitle;
	}

	@Override public SDIArtist getArtist() {
		return mArtist;
	}

	@Override public String getArtistName() {
		return mArtist.getName();
	}

	public String getExternalId() {
		return mExternalId;
	}

	public Date getStreamingReleaseDate() {
		return mStreamingReleaseDate;
	}

	public int getDuration() {
		return mDuration;
	}

	public SDIRelease.SDIPrice getPrice() {
		return mPrice;
	}

	public boolean isExplicitContent() {
		return mExplicitContent;
	}

	public int getTrackNumber() {
		return mTrackNumber;
	}

	public int getDiscNumber() {
		return mDiscNumber;
	}

	public SDIRelease getRelease() {
		return mRelease;
	}

	public String getUrl() {
		return mUrl;
	}

	public String getVersion() {
		return mVersion;
	}

	public SDITrack(Composition composition) {
		if (composition == null) throw new NullPointerException("composition cannot be null");
		mTitle = composition.getTitle();
		mArtist = composition.getArtist();
		mExternalId = composition.getExternalId();
		mStreamingReleaseDate = composition.getStreamingRelease();
		mDuration = composition.getDuration();
		mPrice = composition.getPrice();
		mExplicitContent = composition.hasExplicitContent();
		mTrackNumber = composition.getTrackNumber();
		mDiscNumber = composition.getDiscNumber();
		mRelease = composition.getRelease();
		mUrl = composition.getUrl();
		mVersion = composition.getVersion();
	}

	/** An mArtist is composed of the following methods */
	public static interface Composition {

		public abstract String getExternalId();

		public abstract String getTitle();

		public abstract int getDuration();

		public abstract boolean hasExplicitContent();

		public abstract int getTrackNumber();

		public abstract SDIArtist getArtist();

		public abstract SDIRelease.SDIPrice getPrice();

		public abstract int getDiscNumber();

		public abstract SDIRelease getRelease();

		public abstract String getUrl();

		public abstract String getVersion();

		public abstract Date getStreamingRelease();

	}

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mExternalId);
		dest.writeLong(mStreamingReleaseDate != null ? mStreamingReleaseDate.getTime() : -1);
		dest.writeInt(this.mDuration);
		dest.writeString(this.mTitle);
		dest.writeParcelable(this.mPrice, flags);
		dest.writeByte(mExplicitContent ? (byte) 1 : (byte) 0);
		dest.writeInt(this.mTrackNumber);
		dest.writeParcelable(this.mArtist, flags);
		dest.writeInt(this.mDiscNumber);
		dest.writeParcelable(this.mRelease, flags);
		dest.writeString(this.mUrl);
		dest.writeString(this.mVersion);
	}

	private SDITrack(Parcel in) {
		this.mExternalId = in.readString();
		long tmpMStreamingReleaseDate = in.readLong();
		this.mStreamingReleaseDate = tmpMStreamingReleaseDate == -1 ? null : new Date(tmpMStreamingReleaseDate);
		this.mDuration = in.readInt();
		this.mTitle = in.readString();
		this.mPrice = in.readParcelable(SDIRelease.SDIPrice.class.getClassLoader());
		this.mExplicitContent = in.readByte() != 0;
		this.mTrackNumber = in.readInt();
		this.mArtist = in.readParcelable(SDIArtist.class.getClassLoader());
		this.mDiscNumber = in.readInt();
		this.mRelease = in.readParcelable(Object.class.getClassLoader());
		this.mUrl = in.readString();
		this.mVersion = in.readString();
	}

	public static Creator<SDITrack> CREATOR = new Creator<SDITrack>() {
		public SDITrack createFromParcel(Parcel source) {
			return new SDITrack(source);
		}

		public SDITrack[] newArray(int size) {
			return new SDITrack[size];
		}
	};

	@Override public String toString() {
		return "SDITrack{" + "mExternalId='" + mExternalId + '\'' + ", mStreamingReleaseDate=" + mStreamingReleaseDate + ", mDuration="
				+ mDuration + ", mTitle='" + mTitle + '\'' + ", mPrice=" + mPrice + ", mExplicitContent=" + mExplicitContent
				+ ", mTrackNumber=" + mTrackNumber + ", mArtist=" + mArtist + ", mDiscNumber=" + mDiscNumber + ", mRelease=" + mRelease
				+ ", mUrl='" + mUrl + '\'' + ", mVersion='" + mVersion + '\'' + '}';
	}
}
