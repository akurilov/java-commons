module com.github.akurilov.commons {

	requires org.apache.commons.collections4;
	requires java.base;
	requires java.logging;
	requires java.rmi;

	exports com.github.akurilov.commons.collection;
	exports com.github.akurilov.commons.concurrent;
	exports com.github.akurilov.commons.concurrent.throttle;
	exports com.github.akurilov.commons.func;
	exports com.github.akurilov.commons.io;
	exports com.github.akurilov.commons.io.collection;
	exports com.github.akurilov.commons.io.file;
	exports com.github.akurilov.commons.io.util;
	exports com.github.akurilov.commons.math;
	exports com.github.akurilov.commons.net;
	exports com.github.akurilov.commons.net.ssl;
	exports com.github.akurilov.commons.reflection;
	exports com.github.akurilov.commons.system;

	opens it.unimi.dsi.fastutil;
	opens it.unimi.dsi.fastutil.bytes;
	opens it.unimi.dsi.fastutil.ints;
	opens it.unimi.dsi.fastutil.objects;
	opens it.unimi.dsi.fastutil.shorts;
}
