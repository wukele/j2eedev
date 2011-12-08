package com.googlecode.doce.utils;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityIdHelper {
	static final Logger LOGGER = LoggerFactory.getLogger(EntityIdHelper.class);

	private static AtomicInteger _nextInc = new AtomicInteger(
			(new java.util.Random()).nextInt());

	private EntityIdHelper() {
	}

	public static String getEntityId() {
		int _time = (int) (System.currentTimeMillis() / 1000);
		int _machine = _genmachine;
		int _inc = _nextInc.getAndIncrement();

		byte b[] = new byte[12];
		ByteBuffer bb = ByteBuffer.wrap(b);
		// by default BB is big endian like we need
		bb.putInt(_time);
		bb.putInt(_machine);
		bb.putInt(_inc);

		StringBuilder buf = new StringBuilder(24);

		for (int i = 0; i < b.length; i++) {
			int x = b[i] & 0xFF;
			String s = Integer.toHexString(x);
			if (s.length() == 1)
				buf.append("0");
			buf.append(s);
		}

		return buf.toString();
	}

	private static final int _genmachine;
	static {

		try {
			// build a 2-byte machine piece based on NICs info
			final int machinePiece;
			{
				StringBuilder sb = new StringBuilder();
				Enumeration<NetworkInterface> e = NetworkInterface
						.getNetworkInterfaces();
				while (e.hasMoreElements()) {
					NetworkInterface ni = e.nextElement();
					sb.append(ni.toString());
				}
				machinePiece = sb.toString().hashCode() << 16;
				LOGGER.info("machine piece post: "
						+ Integer.toHexString(machinePiece));
			}

			// add a 2 byte process piece. It must represent not only the JVM
			// but the class loader.
			// Since static var belong to class loader there could be collisions
			// otherwise
			final int processPiece;
			{
				int processId = new java.util.Random().nextInt();
				try {
					processId = java.lang.management.ManagementFactory
							.getRuntimeMXBean().getName().hashCode();
				} catch (Throwable t) {
				}

				ClassLoader loader = EntityIdHelper.class.getClassLoader();
				int loaderId = loader != null ? System.identityHashCode(loader)
						: 0;

				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toHexString(processId));
				sb.append(Integer.toHexString(loaderId));
				processPiece = sb.toString().hashCode() & 0xFFFF;
				LOGGER.info("process piece: "
						+ Integer.toHexString(processPiece));
			}

			_genmachine = machinePiece | processPiece;
			LOGGER.info("machine : " + Integer.toHexString(_genmachine));
		} catch (java.io.IOException ioe) {
			throw new RuntimeException(ioe);
		}

	}
}
