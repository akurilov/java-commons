package com.github.akurilov.commons.net.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 Created by kurila on 12.05.16.
 */
public final class SslContext {

	private SslContext() {}

	private static SSLContext getInstance()
	throws NoSuchAlgorithmException, KeyManagementException {
		final var sslContext = SSLContext.getInstance("TLS");
		sslContext.init(
			null, new TrustManager[] { X509TrustAllManager.INSTANCE }, new SecureRandom()
		);
		return sslContext;
	}

	public static final SSLContext INSTANCE;
	static {
		try {
			INSTANCE = getInstance();
		} catch(final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
