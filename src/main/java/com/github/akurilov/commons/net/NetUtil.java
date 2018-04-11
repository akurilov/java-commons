package com.github.akurilov.commons.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 Created by kurila on 11.07.16.
 */
public interface NetUtil {

	/**
	 Tries to resolve 1st enabled external network interface IP address.
	 Tries to fall back to loopback interface if no valid external interface found.
	 @return IP address
	 @throws SocketException if failed to resolve an interface address
	 @throws IllegalStateException if no network interface found
	 */
	static InetAddress getHostAddr()
	throws SocketException {
		InetAddress addr = null;
		final var netIfaces = NetworkInterface.getNetworkInterfaces();
		NetworkInterface nextNetIface;
		while(netIfaces.hasMoreElements()) {
			nextNetIface = netIfaces.nextElement();
			if(!nextNetIface.isLoopback() && nextNetIface.isUp()) {
				final var addrs = nextNetIface.getInetAddresses();
				while(addrs.hasMoreElements()) {
					addr = addrs.nextElement();
					if(Inet4Address.class.isInstance(addr)) {
						// resolved the external interface address
						break;
					}
				}
			}
		}

		if(addr == null) {
			addr = InetAddress.getLoopbackAddress();
		}
		if(addr == null) {
			throw new IllegalStateException("No network interface found");
		}

		return addr;
	}


	static String getHostAddrString()
	throws SocketException, IllegalStateException {
		return getHostAddr().getHostAddress();
	}

	static long getHostAddrCode()
	throws SocketException, IllegalStateException {
		return getHostAddrString().hashCode();
	}

	static String addPortIfMissing(final String addr, final int defaultPort) {
		if(addr.contains(":")) {
			return addr;
		} else {
			return addr + ':' + Integer.toString(defaultPort);
		}
	}
}
