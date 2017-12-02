package com.github.akurilov.commons.io.util;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;

public interface BufferedByteChannel
extends Channel {

	/**
	 * @return the wrapped byte buffer which is used for bytes transfer
	 */
	ByteBuffer buffer();
}
