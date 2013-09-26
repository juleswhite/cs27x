package org.cs27x.dropbox;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Singleton;
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

@Singleton
public class HazelcastTransport implements DropboxTransport,
		MessageListener<DropboxCmd>, MembershipListener {

	private static final String DEFAULT_TOPIC = "default";

	private ITopic<DropboxCmd> topic_;

	private boolean connected_ = false;

	private List<DropboxTransportListener> listeners_ = new LinkedList<>();

	private List<DropboxCmdListener> cmdListeners_ = new LinkedList<>();

	@SuppressWarnings("deprecation")
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

	@SuppressWarnings("deprecation")
	public void disconnect() {
		Hazelcast.shutdown();
	}

	@SuppressWarnings("deprecation")
	private void checkConnected() {
		Cluster cluster = Hazelcast.getCluster();
		if (cluster.getMembers().size() > 1 && !connected_) {
			connected();
		} else if (connected_) {
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

	public synchronized void connected() {
		if (!connected_) {
			connected_ = true;
			for (DropboxTransportListener l : listeners_) {
				l.connected(this);
			}
			notifyAll();
		}
	}

	public synchronized void disconnected() {
		if (connected_) {
			connected_ = false;
			for (DropboxTransportListener l : listeners_) {
				l.disconnected(this);
			}
			notifyAll();
		}
	}

	@Override
	public void onMessage(Message<DropboxCmd> cmd) {
		for (DropboxCmdListener l : cmdListeners_) {
			l.handleCmd(cmd.getMessageObject());
		}
	}

	@Override
	public void publish(DropboxCmd cmd) {
		topic_.publish(cmd);
	}

	public void addTransportListener(DropboxTransportListener hdlr) {
		listeners_.add(hdlr);
	}

	public void removeTransportListener(DropboxTransportListener l) {
		listeners_.remove(l);
	}

	public void addCmdListener(DropboxCmdListener l) {
		cmdListeners_.add(l);
	}

	public void removeCmdListener(DropboxCmdListener l) {
		cmdListeners_.remove(l);
	}

	public boolean isConnected() {
		return connected_;
	}

}
