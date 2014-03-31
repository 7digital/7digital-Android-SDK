package uk.co.sevendigital.android.sdk.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import uk.co.sevendigital.android.sdk.core.SDIConstants;

public class SDIStringUtil {

	/**
	 * Convert the given input stream into a string with the given encoding. Close the input stream on completion.
	 *
	 * @param in The input stream to convert to string.
	 *
	 * @return Return the string representation of the given input stream (UTF-8 encoded).
	 */
	public static String convertInputStreamToString(InputStream in) throws UnsupportedEncodingException, IOException {

		// in order to preserve backwards compatibility, this method returns an empty string if a null input stream is provided.
		// the correct behaviour (as in the method below) is to throw an exception. do not rely on this behaviour, this workaround will be 
		// removed one day.

		if (in == null) {
			Log.w(SDIConstants.TAG,"null input stream, returning empty string. do not rely on this functionality. subject to change.");
			return "";
		}

		return convertInputStreamToString(in, "UTF-8", true);

	}

	/**
	 * Convert the given input stream into a string with the given encoding. Optionally close the input stream on completion.
	 *
	 * @param in The input stream to convert to string.
	 * @param encoding The encoding used to read the stream bytes to string.
	 * @param close Whether or not to close the input stream on completion of the read.
	 *
	 * @return Return the string representation of the given input stream.
	 */
	public static String convertInputStreamToString(InputStream in, String encoding, boolean close) throws UnsupportedEncodingException,
			IOException {

		if (in == null) throw new IllegalArgumentException();
		if (encoding == null) throw new IllegalArgumentException();

		final Writer writer = new StringWriter();
		final char[] buffer = new char[1024];
		int n;

		try {

			Reader reader = new BufferedReader(new InputStreamReader(in, encoding));
			while ((n = reader.read(buffer)) != -1)
				writer.write(buffer, 0, n);
			return writer.toString();

		} finally {
			if (close) in.close();
		}
	}
}
