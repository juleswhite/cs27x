package org.cs27x.dropbox;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.Map;

import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileEventListener;
import org.cs27x.filewatcher.FileEventSource;
import org.cs27x.filewatcher.FileSystemState;
import org.cs27x.filewatcher.FileSystemStateImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The DropboxClient is responsible for translating incoming DropboxCmds into
 * changes on the local file system. The DropboxClient is also responsible for
 * translating FileEvents into DropboxCmds and propagating them to other clients
 * via the DropboxTransport.
 * 
 * 
 * @author jules
 * 
 */
@Singleton
public class DropboxClient implements FileEventListener, DropboxCmdListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DropboxClient.class);

	private static final Map<Kind<Path>, OpCode> EVENT_TYPE_TO_OPCODES = new ImmutableMap.Builder<WatchEvent.Kind<Path>, DropboxCmd.OpCode>()
			.put(StandardWatchEventKinds.ENTRY_CREATE, OpCode.ADD)
			.put(StandardWatchEventKinds.ENTRY_MODIFY, OpCode.UPDATE)
			.put(StandardWatchEventKinds.ENTRY_DELETE, OpCode.REMOVE).build();

	public interface CmdHandler {
		public void handleCmd(DropboxCmd cmd) throws IOException;
	}

	private CmdHandler ADD_UPDATE_HANDLER = new CmdHandler() {
		@Override
		public void handleCmd(DropboxCmd cmd) throws IOException {
			Path p = fileManager_.resolve(cmd.getPath());
			fileManager_.write(p, cmd.getData(), true);
		}
	};

	private CmdHandler REMOVE_HANDLER = new CmdHandler() {
		@Override
		public void handleCmd(DropboxCmd cmd) throws IOException {
			Path p = fileManager_.resolve(cmd.getPath());
			fileManager_.delete(p);
		}
	};

	private final Map<OpCode, CmdHandler> OPCODE_TO_HANDLER = new ImmutableMap.Builder<OpCode, CmdHandler>()
			.put(OpCode.ADD, ADD_UPDATE_HANDLER)
			.put(OpCode.UPDATE, ADD_UPDATE_HANDLER)
			.put(OpCode.REMOVE, REMOVE_HANDLER).build();

	private final FileSystemState fileSystemState_;
	private final FileManager fileManager_;
	private final DropboxTransport transport_;
	private final FileEventSource fileEvents_;

	public DropboxClient(FileManager fileManager, DropboxTransport transport) {
		this(new FileSystemStateImpl(), null, fileManager, transport);
	}

	public DropboxClient(FileSystemState fileSystemState,
			FileEventSource fileEvents, FileManager fileManager,
			DropboxTransport transport) {
		super();
		fileEvents_ = fileEvents;
		fileSystemState_ = fileSystemState;
		fileManager_ = fileManager;
		transport_ = transport;

		if (transport_ != null) {
			transport_.addCmdListener(this);
		}
		if (fileEvents_ != null) {
			fileEvents_.addListener(this);
		}

	}

	/**
	 * This method should translate FileEvents into DropboxCmds and publish them
	 * to the DropboxTransport as follows:
	 * 
	 * 1. ENTRY_CREATE --> ADD 2. ENTRY_MODIFY --> UPDATE 3. ENTRY_DELETE -->
	 * REMOVE
	 * 
	 * Create and modify cmds should be created with the data contained in the
	 * FileEvent.
	 * 
	 * DropboxCmds should only be created if the FileSystemState indicates that
	 * they should be propagated.
	 * 
	 */
	@Override
	public void handleEvent(FileEvent rawEvt) {
		if (EVENT_TYPE_TO_OPCODES.containsKey(rawEvt.getEventType())) {
			final Path sharedPath = fileManager_.ensureRelative(rawEvt
					.getFile());
			final FileEvent evt = new FileEvent(rawEvt.getEventType(),
					sharedPath, rawEvt.getData());

			if (fileSystemState_.updateState(evt)) {
				try {
					final OpCode code = EVENT_TYPE_TO_OPCODES.get(evt
							.getEventType());
					final byte[] data = (evt.getEventType() != StandardWatchEventKinds.ENTRY_DELETE) ? fileManager_
							.read(Paths.get(rawEvt.getPath())) : null;
					final DropboxCmd cmd = new DropboxCmd(code, evt.getPath()
							.toString(), data);

					transport_.publish(cmd);
				} catch (Exception e) {
					LOG.error("Error dispatching file event.");
					LOG.error("Exception:",e);
				}
			}
		}
	}

	/**
	 * This method should add, update, and delete files in response to incoming
	 * DropboxCmds.
	 * 
	 * Add/Update cmds should trigger a write on the FileManager with the data
	 * in the cmd.
	 * 
	 * Remove cmds should trigger a delete on the FileManager
	 * 
	 * All Paths should be resolved using the FileManager before invoking any
	 * file management operations.
	 * 
	 * DropboxCmds should only be executed if the FileSystemState indicates that
	 * they should be propagated.
	 */
	@Override
	public void handleCmd(DropboxCmd cmd) {
		if (fileSystemState_.updateState(cmd)) {
			try {
				CmdHandler hdlr = OPCODE_TO_HANDLER.get(cmd.getOpCode());
				hdlr.handleCmd(cmd);
			} catch (IOException e) {
				LOG.error("Unexpected exception processing cmd: [{}]", cmd);
				LOG.error("Error:", e);
			}
		}
	}

}
