package com.starit.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class ServerPasswordCallback implements CallbackHandler {

	private static String password = "000000";

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

		if (pc.getIdentifer().equalsIgnoreCase("melin")) {
			if (!pc.getPassword().equals(password)) {
				throw new SecurityException("wrong password");
			}
		}
		else {
			throw new SecurityException("the user does not exits");
		}

	}

}