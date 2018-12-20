package com.red5pro.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IBroadcastStream;

/**
 * @author daccattato
 *
 */
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
	public boolean appStart(IScope app) {
		log.info("appStart");
		
        WebSocketScopeManager manager = ((WebSocketPlugin) PluginRegistry.getPlugin(WebSocketPlugin.NAME)).getManager(scope);
        manager.setApplication(app);
        
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = () -> log.debug("Scheduling: " + System.nanoTime());

		int initialDelay = 0;
		int period = 1;
		executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
		
		return super.appStart(app);
	}
    
    @Override
    public void appStop(IScope scope) {
        log.info("appStop");
        
        WebSocketScopeManager manager = ((WebSocketPlugin) PluginRegistry.getPlugin(WebSocketPlugin.NAME)).getManager(scope);
        manager.stop();

        super.appStop(scope);
    }


}
