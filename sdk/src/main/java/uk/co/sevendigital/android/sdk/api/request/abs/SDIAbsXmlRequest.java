package uk.co.sevendigital.android.sdk.api.request.abs;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.Date;

import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.api.object.SDIRelease;
import uk.co.sevendigital.android.sdk.api.object.SDITrack;

public abstract class SDIAbsXmlRequest<T> extends SDIAbsRequest<T> {

	@Root(strict = false) public static class Error {
		@Attribute(name = "code", required = false) public String mErrorCode;
		@Element(name = "errorMessage", required = false) public String mErrorMessage;
	}

	@Root(strict = false) public static class XMLArtist implements SDIArtist.Composition {
		@Attribute(name = "id") public String mExternalId;
		@Element(name = "name") public String mName;
		@Element(name = "sortName", required = false) public String mSortName;
		@Element(name = "url") public String mUrl;
		@Element(name = "image", required = false) public String mImage;
		@Element(name = "bio", required = false) public XMLBio mBio;

		@Override public String getExternalId() {
			return mExternalId;
		}

		@Override public String getName() {
			return mName;
		}

		@Override public String getUrl() {
			return mUrl;
		}

		@Override public String getBio() {
			return null != mBio ? mBio.mText : null;
		}

		@Override public String getImageUrl() {
			return mImage;
		}
	}

	@Root(strict = false) public static class XMLBio {
		@Element(name = "text", required = false) public String mText;
	}

	@Root(strict = false) public static class XMLRelease implements SDIRelease.Composition {
		@Attribute(name = "id") public String mExternalId;
		@Element(name = "title") public String mTitle;
		@Element(name = "version", required = false) public String mVersion;
		@Element(name = "type") public String mType;
		@Element(name = "barcode",required = false) public String mBarcode;
		@Element(name = "year",required = false) public int mYear;
		@Element(name = "explicitContent",required = false) public boolean mExplicitContent;
		@Element(name = "artist") public XMLArtist mArtist;
		@Element(name = "url",required = false) public String mUrl;
		@Element(name = "image",required = false) public String mImage;
		@Element(name = "price",required = false) public XMLPrice mPrice;
		@Element(name = "releaseDate",required = false) public Date mReleaseDate;
		@Element(name = "addedDate",required = false) public Date mAddedDate;
		//todo: when api is fixed
		//		@ElementList(name = "formats") public List<Object> mFormats;
		@Element(name = "label",required = false) public XMLLabel mLabel;
		@Element(name = "licensor",required = false) public XMLLicensor mLicensor;
		@Element(name = "streamingReleaseDate",required = false) public Date mStreamingReleaseDate;
		@Element(name = "popularity",required = false) public Double mPopularity;
		@Element(name = "duration",required = false) public int mDuration;
		@Element(name = "trackCount",required = false) public int mTrackCount;

		@Override public String getExternalId() {
			return mExternalId;
		}

		@Override public String getTitle() {
			return mTitle;
		}

		@Override public SDIArtist getArtist() {
			return new SDIArtist(mArtist);
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
			return null!=mPrice?new SDIRelease.SDIPrice(mPrice):null;
		}

		@Override public Object getFormats() {
			return null;
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

	@Root(strict = false) public static class XMLLicensor {
		@Attribute(name = "id") public String mId;
		@Element(name = "name") public String mName;
	}

	@Root(strict = false) public static class XMLLabel {
		@Attribute(name = "id") public String mId;
		@Element(name = "name") public String mName;
	}

	@Root(strict = false) public static class XMLPrice implements SDIRelease.SDIPrice.Composition {
		@Attribute(name = "id", required = false) public String mId;
		@Element(name = "currency", required = false) public XMLCurrency mCurrency;
		//		@Element(name = "currency", required = false) public XMLCurrency mCurrency;
		@Element(name = "value") public String mValue;
		@Element(name = "formattedPrice") public String mFormattedPrice;
		@Element(name = "rrp") public String mRrp;
		@Element(name = "formattedRrp") public String mFormattedRrp;
		@Element(name = "onSale") public Boolean mOnSale;

		@Override public String getCurrencyCode() {
			return mCurrency.code;
		}

		@Override public String getCurrencySymbol() {
			return mCurrency.symbol;
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

	}

	@Root(strict = false) public static class XMLCurrency {

		@Attribute protected String code;

		@Text protected String symbol;
	}

	@Root(strict = false) public static class XMLTrack implements SDITrack.Composition {
		@Attribute(name = "id") public String mExternalId;
		@Element(name = "title") public String mTitle;
		@Element(name = "version", required = false) public String mVersion;
		@Element(name = "artist", required = false) public XMLArtist mArtist;
		@Element(name = "price", required = false) public XMLPrice mPrice;
		@Element(name = "trackNumber",required = false) public int mTrackNumber;
		@Element(name = "duration",required = false) public int mDuration;
		@Element(name = "release",required = false) public XMLRelease mRelease;
		@Element(name = "explicitContent",required = false) public boolean mExplicitContent;
		@Element(name = "isrc",required = false) public String mIsrc;
		@Element(name = "type",required = false) public String mType;
		@Element(name = "url",required = false) public String mUrl;
		@Element(name = "streamingReleaseDate",required = false) public Date mStreamingReleaseDate;

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
			return null != mPrice ? new SDIRelease.SDIPrice(mPrice) : null;
		}

		@Override public int getDiscNumber() {
			return mTrackNumber;
		}

		@Override public SDIRelease getRelease() {
			return null != mRelease ? new SDIRelease(mRelease) : null;
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
