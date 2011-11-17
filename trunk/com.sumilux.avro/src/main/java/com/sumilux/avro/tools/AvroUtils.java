package com.sumilux.avro.tools;

import java.io.File;
import java.io.IOException;
import org.apache.avro.Protocol;

public class AvroUtils {

	public static Protocol getProtocol() {
		Protocol protocol = null;
		try {
			System.out.println("init sysout .......");
			protocol = Protocol.parse(new File("src/main/java/com/sumilux/avro/user.avpr"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return protocol;
	}

}