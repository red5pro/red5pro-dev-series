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
public class ExampleApplication extends MultiThreadedApplicationAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
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
		
        configureApplicationScopeWebSocket(scope);
        
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
        
        WebSocketScopeManager manager = ((WebSocketPlugin) PluginRegistry.getPlugin("WebSocketPlugin")).getManager(scope);
        manager.removeApplication(scope);
        manager.stop();
        

        super.appStop(scope);
    }
    
    /**
     * Configures a websocket scope for a given application scope.
     * 
     * @param scope Server application scope
     */
    private void configureApplicationScopeWebSocket(IScope scope) {

        // first get the websocket plugin
        WebSocketPlugin wsPlugin = ((WebSocketPlugin) PluginRegistry.getPlugin(WebSocketPlugin.NAME));
        // get the websocket scope manager for the red5 scope
        WebSocketScopeManager manager = wsPlugin.getManager(scope);

        if (manager == null) {
            // get the application adapter
            MultiThreadedApplicationAdapter app = (MultiThreadedApplicationAdapter) scope.getHandler();
            log.debug("Creating WebSocketScopeManager for {}", app);
            // set the application in the plugin to create a websocket scope manager for it
            wsPlugin.setApplication(app);
            // get the new manager
            manager = wsPlugin.getManager(scope);
        }

        // the websocket scope
        WebSocketScope wsScope = (WebSocketScope) scope.getAttribute(WSConstants.WS_SCOPE);

        // check to see if its already configured
        if (wsScope == null) {
            log.debug("Configuring application scope: {}", scope);
            // create a websocket scope for the application
            wsScope = new WebSocketScope(scope);
            // register the ws scope
            wsScope.register();
        }
    }


}
