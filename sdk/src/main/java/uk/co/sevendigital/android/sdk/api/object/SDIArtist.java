package uk.co.sevendigital.android.sdk.api.object;

import android.os.Parcel;

import uk.co.sevendigital.android.sdk.model.base.Artist;

public class SDIArtist implements Artist, android.os.Parcelable {
	private static final long serialVersionUID = 6636541149543121580L;

	public SDIArtist() {}

	private String mExternalId;
	private String mName;
	private String mAppearsAs;
	private String mUrl;
	private String mImageUrl;
	private String mBio;

	public SDIArtist(Composition composition) {
		mExternalId = composition.getExternalId();
		mName = composition.getName();
		mAppearsAs = composition.getName();
		mUrl = composition.getUrl();
		mImageUrl = composition.getImageUrl();
		mBio = composition.getBio();
	}

	/** An mArtist is composed of the following methods */
	public static interface Composition {

		public abstract String getExternalId();

		public abstract String getName();

		public abstract String getUrl();

		public abstract String getImageUrl();

		public abstract String getBio();
	}

	public String getExternalId() {
		return mExternalId;
	}

	public String getName() {
		return mName;
	}

	public String getBio() {
		return mBio;
	}

	public String getAppearsAs() {
		return mAppearsAs;
	}

	public String getUrl() {
		return mUrl;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mExternalId);
		dest.writeString(this.mName);
		dest.writeString(this.mAppearsAs);
		dest.writeString(this.mUrl);
		dest.writeString(this.mImageUrl);
		dest.writeString(this.mBio);
	}

	private SDIArtist(Parcel in) {
		this.mExternalId = in.readString();
		this.mName = in.readString();
		this.mAppearsAs = in.readString();
		this.mUrl = in.readString();
		this.mImageUrl = in.readString();
		this.mBio = in.readString();
	}

	public static Creator<SDIArtist> CREATOR = new Creator<SDIArtist>() {
		public SDIArtist createFromParcel(Parcel source) {
			return new SDIArtist(source);
		}

		public SDIArtist[] newArray(int size) {
			return new SDIArtist[size];
		}
	};

	@Override public String toString() {
		return "SDIArtist{" + "mExternalId='" + mExternalId + '\'' + ", mName='" + mName + '\'' + ", mAppearsAs='" + mAppearsAs + '\''
				+ ", mUrl='" + mUrl + '\'' + ", mBio='" + mBio + '\'' + '}';
	}
}
