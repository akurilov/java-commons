module com.github.akurilov.commons {

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
}
