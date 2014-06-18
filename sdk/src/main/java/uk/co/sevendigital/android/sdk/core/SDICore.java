package uk.co.sevendigital.android.sdk.core;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import uk.co.sevendigital.android.sdk.BuildConfig;
import uk.co.sevendigital.android.sdk.util.OkHttpStack;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;

public class SDICore {

	private static RequestQueue queue = null;
	private static ObjectMapper mapper = null;
	private static SDIServerUtil.OauthConsumer consumer = null;
	private static Serializer serializer = null;

	protected SDICore() {}

	public static synchronized RequestQueue getQueue(Context ctx) {

		if (queue == null) {
			OkHttpClient okClient = new OkHttpClient();
			//disable checking of ssl certs when debugging, so we can proxy the requests
			//todo: always disable
			if (BuildConfig.DEBUG)okClient.setSslSocketFactory(badSslSocketFactory());
			queue = Volley.newRequestQueue(ctx, new OkHttpStack(okClient));
		}
		return queue;
	}

	public static synchronized ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return mapper;
	}

	public static synchronized Serializer getSerializer() {
		if (serializer == null) {
			//use a custom date format not supported by simplexml
			DateFormat format = new SimpleDateFormat("yyy-mm-dd'T'HH:mm:ss", Locale.US);
			RegistryMatcher m = new RegistryMatcher();
			m.bind(Date.class, new DateFormatTransformer(format));
			serializer =  new Persister(m);
		}
		return serializer;
	}

	/**
	 * Returns an SSL socket factory that doesn't validate SSL certs. This should only be used for development.
	 */
	private static SSLSocketFactory badSslSocketFactory() {
		try {
			// Construct SSLSocketFactory that accepts any cert.
			SSLContext context = SSLContext.getInstance("TLS");
			TrustManager permissive = new X509TrustManager() {
				@Override public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

				@Override public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

				@Override public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			context.init(null, new TrustManager[] { permissive }, null);
			return context.getSocketFactory();
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}


	/* Custom Data Transformer for simple xml, to deserialize the dates */
	public static class DateFormatTransformer implements Transform<Date>
	{
		private DateFormat dateFormat;

		public DateFormatTransformer(DateFormat dateFormat)
		{
			this.dateFormat = dateFormat;
		}

		@Override
		public Date read(String value) throws Exception
		{
			value = value.replaceAll("Z$","");
			return dateFormat.parse(value);
		}

		@Override
		public String write(Date value) throws Exception
		{
			return dateFormat.format(value);
		}

	}

}
