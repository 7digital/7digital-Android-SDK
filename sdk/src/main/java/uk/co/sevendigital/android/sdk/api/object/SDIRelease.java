package uk.co.sevendigital.android.sdk.api.object;

import android.os.Parcel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import uk.co.sevendigital.android.sdk.model.base.Release;

public class SDIRelease implements Release {

	public SDIRelease() {}

	private String mExternalId;
	private String mTitle;
	private String mVersion;
	private String mType;
	private String mBarcode;
	private int mYear;
	private boolean mExplicitContent;
	private SDIArtist mArtist;
	private String mUrl;
	private String mImage;
	private Date mReleaseDate;
	private SDIPrice mPrice;
	private List<Object> mFormats;
	private Date mStreamingReleaseDate;
	private int mDuration;
	private int mTrackCount;

	@Override public String getTitle() {
		return mTitle;
	}

	@Override public SDIArtist getArtist() {
		return mArtist;
	}

	@Override public String getArtistName() {
		return mArtist.getName();
	}

	public SDIRelease(Composition composition) {
		if (composition == null) throw new NullPointerException("composition cannot be null");
		mTitle = composition.getTitle();
		mArtist = composition.getArtist();
		mYear = composition.getYear();
		mReleaseDate = composition.getReleaseDate();
		mType = composition.getType();
		mExternalId = composition.getExternalId();
		mImage = composition.getImage();
		mPrice = composition.getPrice();
		//		mFormats = composition.getFormats();
		mStreamingReleaseDate = composition.getStreamingReleaseDate();
		mUrl = composition.getUrl();
		mVersion = composition.getVersion();
		mBarcode = composition.getBarcode();
	}

	/** An mArtist is composed of the following methods */
	public static interface Composition {

		public String getExternalId();

		public String getTitle();

		public SDIArtist getArtist();

		public String getVersion();

		public String getType();

		public String getBarcode();

		public int getYear();

		public boolean hasExplicitContent();

		public String getUrl();

		public String getImage();

		public Date getReleaseDate();

		public SDIPrice getPrice();

		public Object getFormats();

		public Date getStreamingReleaseDate();

		public int getDuration();

		public int getTrackCount();

	}

	public String getExternalId() {
		return mExternalId;
	}

	public String getVersion() {
		return mVersion;
	}

	@Override public String getType() {
		return mType;
	}

	public String getBarcode() {
		return mBarcode;
	}

	@Override public int getYear() {
		return mYear;
	}

	public boolean isExplicitContent() {
		return mExplicitContent;
	}

	public String getUrl() {
		return mUrl;
	}

	public String getImageUrl() {
		return mImage;
	}

	@Override public Date getReleaseDate() {
		return mReleaseDate;
	}

	public SDIPrice getPrice() {
		return mPrice;
	}

	public Object getFormats() {
		return mFormats;
	}

	public Date getStreamingReleaseDate() {
		return mStreamingReleaseDate;
	}

	public int getDuration() {
		return mDuration;
	}

	@Override public int getTrackCount() {
		return mTrackCount;
	}

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mExternalId);
		dest.writeString(this.mTitle);
		dest.writeString(this.mVersion);
		dest.writeString(this.mType);
		dest.writeString(this.mBarcode);
		dest.writeInt(this.mYear);
		dest.writeByte(mExplicitContent ? (byte) 1 : (byte) 0);
		dest.writeParcelable(this.mArtist, flags);
		dest.writeString(this.mUrl);
		dest.writeString(this.mImage);
		dest.writeLong(mReleaseDate != null ? mReleaseDate.getTime() : -1);
		dest.writeParcelable(this.mPrice, flags);
		//		dest.writeList(this.mFormats);
		dest.writeLong(mStreamingReleaseDate != null ? mStreamingReleaseDate.getTime() : -1);
		dest.writeInt(this.mDuration);
		dest.writeInt(this.mTrackCount);
	}

	private SDIRelease(Parcel in) {
		this.mExternalId = in.readString();
		this.mTitle = in.readString();
		this.mVersion = in.readString();
		this.mType = in.readString();
		this.mBarcode = in.readString();
		this.mYear = in.readInt();
		this.mExplicitContent = in.readByte() != 0;
		this.mArtist = in.readParcelable(SDIArtist.class.getClassLoader());
		this.mUrl = in.readString();
		this.mImage = in.readString();
		long tmpMReleaseDate = in.readLong();
		this.mReleaseDate = tmpMReleaseDate == -1 ? null : new Date(tmpMReleaseDate);
		this.mPrice = in.readParcelable(SDIPrice.class.getClassLoader());
		//todo:
		//		this.mFormats = new ArrayList<List<Object>>();
		//		in.readList(this.mFormats, List<Object>.class.getClassLoader());
		long tmpMStreamingReleaseDate = in.readLong();
		this.mStreamingReleaseDate = tmpMStreamingReleaseDate == -1 ? null : new Date(tmpMStreamingReleaseDate);
		this.mDuration = in.readInt();
		this.mTrackCount = in.readInt();
	}

	public static Creator<SDIRelease> CREATOR = new Creator<SDIRelease>() {
		public SDIRelease createFromParcel(Parcel source) {
			return new SDIRelease(source);
		}

		public SDIRelease[] newArray(int size) {
			return new SDIRelease[size];
		}
	};

	public static class SDIPrice implements android.os.Parcelable, Serializable {
		private String mCurrencyCode;
		private String mCurrencySymbol;
		private String mValue;
		private String mFormattedPrice;
		private String mRrp;
		private String mFormattedRrp;
		private Boolean mOnSale;

		public SDIPrice(Composition composition) {
			mCurrencyCode = composition.getCurrencyCode();
			mCurrencySymbol = composition.getCurrencySymbol();
			mValue = composition.getValue();
			mFormattedPrice = composition.getFormattedPrice();
			mFormattedRrp = composition.getFormattedRrp();
			mRrp = composition.getRrp();
			mOnSale = composition.isOnSale();
		}

		public String getCurrencyCode() {
			return mCurrencyCode;
		}

		public String getCurrencySymbol() {
			return mCurrencySymbol;
		}

		public String getValue() {
			return mValue;
		}

		public String getFormattedPrice() {
			return mFormattedPrice;
		}

		public String getRrp() {
			return mRrp;
		}

		public String getFormattedRrp() {
			return mFormattedRrp;
		}

		public Boolean getOnSale() {
			return mOnSale;
		}

		/** An mArtist is composed of the following methods */
		public static interface Composition {

			public String getCurrencyCode();

			public String getCurrencySymbol();

			public String getValue();

			public String getFormattedPrice();

			public String getRrp();

			public String getFormattedRrp();

			public boolean isOnSale();
		}

		@Override public int describeContents() {
			return 0;
		}

		@Override public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.mCurrencyCode);
			dest.writeString(this.mCurrencySymbol);
			dest.writeString(this.mValue);
			dest.writeString(this.mFormattedPrice);
			dest.writeString(this.mRrp);
			dest.writeString(this.mFormattedRrp);
			dest.writeValue(this.mOnSale);
		}

		private SDIPrice(Parcel in) {
			this.mCurrencyCode = in.readString();
			this.mCurrencySymbol = in.readString();
			this.mValue = in.readString();
			this.mFormattedPrice = in.readString();
			this.mRrp = in.readString();
			this.mFormattedRrp = in.readString();
			this.mOnSale = (Boolean) in.readValue(Boolean.class.getClassLoader());
		}

		public static Creator<SDIPrice> CREATOR = new Creator<SDIPrice>() {
			public SDIPrice createFromParcel(Parcel source) {
				return new SDIPrice(source);
			}

			public SDIPrice[] newArray(int size) {
				return new SDIPrice[size];
			}
		};
	}


	@Override public String toString() {
		return "SDIRelease{" +
				"mExternalId='" + mExternalId + '\'' +
				", mTitle='" + mTitle + '\'' +
				", mVersion='" + mVersion + '\'' +
				", mType='" + mType + '\'' +
				", mBarcode='" + mBarcode + '\'' +
				", mYear=" + mYear +
				", mExplicitContent=" + mExplicitContent +
				", mArtist=" + mArtist +
				", mUrl='" + mUrl + '\'' +
				", mImage='" + mImage + '\'' +
				", mReleaseDate=" + mReleaseDate +
				", mPrice=" + mPrice +
				", mFormats=" + mFormats +
				", mStreamingReleaseDate=" + mStreamingReleaseDate +
				", mDuration=" + mDuration +
				", mTrackCount=" + mTrackCount +
				'}';


	}
}
