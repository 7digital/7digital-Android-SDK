package uk.co.sevendigital.android.sdk.api.request.abs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.api.object.SDIChart;
import uk.co.sevendigital.android.sdk.api.object.SDIRelease;
import uk.co.sevendigital.android.sdk.api.object.SDITrack;

public abstract class SDIAbsJsonRequest<T> extends SDIAbsRequest<T> {

	protected static final class JsonRelease implements SDIRelease.Composition {
		@JsonProperty("id") private String mExternalId;
		@JsonProperty("title") private String mTitle;
		@JsonProperty("version") private String mVersion;
		@JsonProperty("type") private String mType;
		@JsonProperty("barcode") private String mBarcode;
		@JsonProperty("year") private int mYear;
		@JsonProperty("explicitContent") private boolean mExplicitContent;
		@JsonProperty("artist") private JsonArtist mArtist;
		@JsonProperty("url") private String mUrl;
		@JsonProperty("image") private String mImage;
		@JsonProperty("releaseDate") private Date mReleaseDate;
		@JsonProperty("price") private JsonPrice mPrice;
		@JsonProperty("formats") private Object mFormats;
		@JsonProperty("streamingReleaseDate") private Date mStreamingReleaseDate;
		@JsonProperty("duration") private int mDuration;
		@JsonProperty("trackCount") private int mTrackCount;

		@Override public String getExternalId() {
			return mExternalId;
		}

		@Override public String getTitle() {
			return mTitle;
		}

		@Override public SDIArtist getArtist() {
			return null != mArtist ? new SDIArtist(mArtist) : null;
		}

		@Override public String getVersion() {
			return mVersion;
		}

		@Override public String getType() {
			return mType;
		}

		@Override public String getBarcode() {
			return mBarcode;
		}

		@Override public int getYear() {
			return mYear;
		}

		@Override public boolean hasExplicitContent() {
			return mExplicitContent;
		}

		@Override public String getUrl() {
			return mUrl;
		}

		@Override public String getImage() {
			return mImage;
		}

		@Override public Date getReleaseDate() {
			return mReleaseDate;
		}

		@Override public SDIRelease.SDIPrice getPrice() {
			return null != mPrice ? new SDIRelease.SDIPrice(mPrice) : null;
		}

		@Override public Object getFormats() {
			return mFormats;
		}

		@Override public Date getStreamingReleaseDate() {
			return mStreamingReleaseDate;
		}

		@Override public int getDuration() {
			return mDuration;
		}

		@Override public int getTrackCount() {
			return mTrackCount;
		}
	}

	protected static final class JsonChart<T> implements SDIChart.Composition {
		@JsonProperty("fromDate") private Date mFromDate;
		@JsonProperty("page") private int mPage;
		@JsonProperty("totalItems") private int mTotalItems;
		@JsonProperty("pageSize") private int mPageSize;
		@JsonProperty("toDate") private Date mToDate;
		@JsonProperty("type") private String mType;
		@JsonProperty("chartItem") public List<T> mChartItems;

		@Override public Date getFromDate() {
			return mFromDate;
		}

		@Override public int getPage() {
			return mPage;
		}

		@Override public int getTotalItems() {
			return mTotalItems;
		}

		@Override public int getPageSize() {
			return mPageSize;
		}

		@Override public Date getToDate() {
			return mToDate;
		}

		@Override public String getType() {
			return mType;
		}
	}

	protected static class JsonPrice implements SDIRelease.SDIPrice.Composition {

		public JsonPrice() {}

		@JsonProperty("value") private String mValue;
		@JsonProperty("formattedPrice") private String mFormattedPrice;
		@JsonProperty("currency") private Currency mCurrency;
		@JsonProperty("rrp") private String mRrp;
		@JsonProperty("formattedRrp") private String mFormattedRrp;
		@JsonProperty("onSale") private boolean mOnSale;

		@Override public String getCurrencyCode() {
			return null != mCurrency ? mCurrency.mCode : null;
		}

		@Override public String getCurrencySymbol() {
			return null != mCurrency ? mCurrency.mSymbol : null;
		}

		@Override public String getValue() {
			return mValue;
		}

		@Override public String getFormattedPrice() {
			return mFormattedPrice;
		}

		@Override public String getRrp() {
			return mRrp;
		}

		@Override public String getFormattedRrp() {
			return mFormattedRrp;
		}

		@Override public boolean isOnSale() {
			return mOnSale;
		}

		protected static class Currency {
			@JsonProperty("code") protected String mCode;
			@JsonProperty("symbol") protected String mSymbol;
		}

	}

	protected static class JsonArtist implements SDIArtist.Composition {
		private JsonArtist() {}

		@JsonProperty("name") private String mName;
		@JsonProperty("appearsAs") private String mAppearsAs;
		@JsonProperty("url") private String mUrl;
		@JsonProperty("id") private String mExternalId;

		@Override public String getName() {
			return mName;
		}

		@Override public String getExternalId() {
			return mExternalId;
		}

		@Override public String getUrl() {
			return mUrl;
		}

		@Override public String getBio() {
			return null;
		}

		@Override public String getImageUrl() {
			return null;/* not returned by api call */
		}
	}

	protected static final class JsonTrack implements SDITrack.Composition {

		private JsonTrack() {}

		@JsonProperty("id") private String mExternalId;
		@JsonProperty("streamingReleaseDate") private Date mStreamingReleaseDate;
		@JsonProperty("duration") private int mDuration;
		@JsonProperty("title") private String mTitle;
		@JsonProperty("price") private JsonPrice mPrice;
		@JsonProperty("explicitContent") private boolean mExplicitContent;
		@JsonProperty("trackNumber") private int mTrackNumber;
		@JsonProperty("artist") private JsonArtist mArtist;
		@JsonProperty("discNumber") private int mDiscNumber;
		@JsonProperty("release") private JsonRelease mRelease;
		@JsonProperty("url") private String mUrl;
		@JsonProperty("version") private String mVersion;

		@Override public String getExternalId() {
			return mExternalId;
		}

		@Override public String getTitle() {
			return mTitle;
		}

		@Override public int getDuration() {
			return mDuration;
		}

		@Override public boolean hasExplicitContent() {
			return mExplicitContent;
		}

		@Override public int getTrackNumber() {
			return mTrackNumber;
		}

		@Override public SDIArtist getArtist() {
			return new SDIArtist(mArtist);
		}

		@Override public SDIRelease.SDIPrice getPrice() {
			return new SDIRelease.SDIPrice(mPrice);
		}

		@Override public int getDiscNumber() {
			return mDiscNumber;
		}

		@Override public SDIRelease getRelease() {
			return new SDIRelease(mRelease);
		}

		@Override public String getUrl() {
			return mUrl;
		}

		@Override public String getVersion() {
			return mVersion;
		}

		@Override public Date getStreamingRelease() {
			return mStreamingReleaseDate;
		}
	}

}
