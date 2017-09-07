package com.red5pro.example;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.service.ServiceUtils;
import org.red5.server.api.stream.IBroadcastStream;

import com.red5pro.plugin.Red5ProPlugin;

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
		Red5ProPlugin.getCluster().setHandler("publishingHandler", this);
		
		return super.appStart(app);
	}
	
	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamPublishStart(stream);
		
		// send a message over the clustered nodes
		// the last two arguments which are false and true represent that we do not want to send messages upstream, 
		// and that we want to send them downstream
		// public void sendMessage(String service,Object[] message, boolean upstream,boolean downstream);
		Red5ProPlugin.getCluster().sendMessage("publishingHandler.onPublishStart", new Object[]{ stream.getPublishedName() }, false, true);
	}
	
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamBroadcastStart(stream);
	}

	/**
	 * Clustered Event Handler
	 * 
	 * @param streamName
	 */
	public void onPublishStart(String streamName) {
		IConnection connectionLocal = Red5.getConnectionLocal();
		
		// send a message to all connected clients on the current scope. The method on teh client will be called "publishStarted" 
		// and will have the streamName as an argument
		ServiceUtils.invokeOnAllScopeConnections(connectionLocal.getScope(), "publishStarted", new Object[]{ streamName }, null);
	}

}
