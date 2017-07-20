package com.red5pro.example;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;

public class ExampleApplication extends MultiThreadedApplicationAdapter {

	@Override
	public boolean appConnect(IConnection conn, Object[] params) {
		log.info("appConnect");
		return super.appConnect(conn, params);
	}

	@Override
	public void appDisconnect(IConnection conn) {
		log.info("appDisconnect");
		super.appDisconnect(conn);
	}

}
