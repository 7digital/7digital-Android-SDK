package uk.co.sevendigital.android.sdk.sample.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoverHelper {

	private static final Pattern SIZED_IMAGE_URL_PATTERN = Pattern.compile("(http.*_)\\d+\\.(jpe?g|png)");

	private static final int[] productImageSizes = new int[] { 33, 50, 52, 75, 100, 175, 180, 182, 200, 350, 500, 800 };

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * swap sized image url size
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Swap the size of a sized image url, returning the new url.
	 * 
	 * Sized image urls encode the image (jpg, jpeg or png) with the image size at the suffix of the url.
	 * 
	 * For example: http://www.example.com/image_50.jpg With replacement 100 would return: http://www.example.com/image_100.jpg
	 * 
	 * @param url The sized image url.
	 * @param size The new size of the image
	 */
	public static String swapSizedImageUrlSize(String url, int size) {
		if (url == null) throw new IllegalArgumentException("url cannot be null");
		Matcher matcher = SIZED_IMAGE_URL_PATTERN.matcher(url);
		if (!matcher.matches()) return url;
		return matcher.group(1) + size + "." + matcher.group(2);
	}

}
