package com.github.akurilov.commons.concurrent;

import java.io.Closeable;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public interface AsyncRunnable
extends Closeable {

	enum State {
		INITIAL, STARTED, SHUTDOWN, STOPPED, CLOSED
	}

	/**
	 @return the current state, null if closed
	 */
	State state()
	throws RemoteException;

	/**
	 @return true if the state is "initial", false otherwise
	 */
	boolean isInitial()
	throws RemoteException;

	/**
	 @return true if the state is "started", false otherwise
	 */
	boolean isStarted()
	throws RemoteException;

	/**
	 @return true if the state is "shutdown", false otherwise
	 */
	boolean isShutdown()
	throws RemoteException;

	/**
	 @return true if the state is "stopped", false otherwise
	 */
	boolean isStopped()
	throws RemoteException;

	/**
	 @return true if there's no state, false otherwise
	 */
	boolean isClosed()
	throws RemoteException;

	/**
	 Start/resume the execution
	 @return the same instance with state changed to <i>STARTED</i> if call was successful.
	 */
	AsyncRunnable start()
	throws RemoteException;

	/**
	 Notify to stop to enqueue incoming requests. The await method should be used after this to make
	 sure that everything accepted before the shutdown is done.
	 */
	AsyncRunnable shutdown()
	throws RemoteException;

	/**
	 Stop (with further resumption capability)
	 @return the same instance with state changed to <i>STOPPED</i> if call was successful
	 */
	AsyncRunnable stop()
	throws RemoteException;

	/**
	 Wait while the state is <i>STARTED</i>
	 @return the same instance
	 @throws InterruptedException
	 */
	AsyncRunnable await()
	throws InterruptedException, RemoteException;

	boolean await(final long timeout, final TimeUnit timeUnit)
	throws InterruptedException, RemoteException;
}
