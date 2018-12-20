package com.red5pro.example;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;

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

	@Override
    public void appStart(IScope app) {
        log.info("appStart");

        WebSocketScopeManager manager = ((WebSocketPlugin) PluginRegistry.getPlugin(WebSocketPlugin.NAME)).getManager(scope);
        manager.setApplication(app);

        super.appStart(app);
    }

    @Override
    public void appStop(IScope scope) {
        log.info("appStop");
        
        WebSocketScopeManager manager = ((WebSocketPlugin) PluginRegistry.getPlugin(WebSocketPlugin.NAME)).getManager(scope);
        manager.stop();

        super.appStop(scope);
    }

}
