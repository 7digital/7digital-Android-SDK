package uk.co.sevendigital.android.sdk.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SDIChartItem<T> {

	public SDIChartItem() {
	}

	@JsonProperty("position") private int mPosition;
	@JsonProperty("change") private String mChange;
	@JsonProperty() public T mItem;

	public SDIChartItem(Composition composition) {
		if (composition == null) throw new NullPointerException("composition cannot be null");
		mPosition = composition.getPosition();
		mChange = composition.getChange();
		mItem = (T) composition.getItem();
	}

	public T getItem() {
		return mItem;
	}

	public int getPosition() {
		return mPosition;
	}

	public String getChange() {
		return mChange;
	}

	/** A chart item is composed of the following methods */
	public static interface Composition {

		public abstract Object getItem();

		public int getPosition();

		public String getChange();
	}

}
