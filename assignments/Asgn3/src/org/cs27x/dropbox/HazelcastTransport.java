package org.cs27x.dropbox;

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.config.Config;
import com.hazelcast.config.Join;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

public class HazelcastTransport implements DropboxTransport,
		MessageListener<DropboxCmd>, MembershipListener {

	private static final String DEFAULT_TOPIC = "default";

	private ITopic<DropboxCmd> topic_;
	
	private boolean connected_ = false;

	private List<DropboxTransportListener> listeners_ = new ArrayList<>(1);

	@Override
	public void connect(String initialPeer) {
		if (initialPeer != null) {
			Config cfg = new Config();
			cfg.setPort(5701);
			cfg.setPortAutoIncrement(true);

			NetworkConfig network = cfg.getNetworkConfig();
			Join join = network.getJoin();
			join.getMulticastConfig().setEnabled(false);

			join.getTcpIpConfig().setRequiredMember(initialPeer)
					.setEnabled(true);

			Hazelcast.init(cfg);
		}

		Cluster cluster = Hazelcast.getCluster();
		cluster.addMembershipListener(this);
		topic_ = Hazelcast.getTopic(DEFAULT_TOPIC);
		topic_.addMessageListener(this);
	
		checkConnected();
	}
	
	public void disconnect(){
		Hazelcast.shutdown();
	}

	private void checkConnected(){
		Cluster cluster = Hazelcast.getCluster();
		if(cluster.getMembers().size() > 1 && !connected_){
			connected();
		} else if(connected_){
			disconnected();
		}
	}
	
	@Override
	public void memberAdded(MembershipEvent arg0) {
		checkConnected();
	}

	@Override
	public void memberRemoved(MembershipEvent arg0) {
		checkConnected();
	}
	
	public synchronized void connected(){
		connected_ = true;
		for(DropboxTransportListener l : listeners_){
			l.connected(this);
		}
		notifyAll();
	}
	
	public synchronized void disconnected(){
		connected_ = false;
		for(DropboxTransportListener l : listeners_){
			l.disconnected(this);
		}
		notifyAll();
	}

	@Override
	public void onMessage(Message<DropboxCmd> cmd) {
		for (DropboxTransportListener l : listeners_) {
			l.cmdReceived(cmd.getMessageObject());
		}
	}

	@Override
	public void publish(DropboxCmd cmd) {
		topic_.publish(cmd);
	}

	public void addListener(DropboxTransportListener hdlr) {
		listeners_.add(hdlr);
	}

	public boolean isConnected() {
		return connected_;
	}
	
	public synchronized void awaitConnect(long timeout) throws InterruptedException {
		if(!connected_){
			if(timeout > 0){
				wait(timeout);
			}else {
				wait();
			}
		}
	}
}
