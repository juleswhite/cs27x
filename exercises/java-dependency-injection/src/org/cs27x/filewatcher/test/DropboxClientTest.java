package org.cs27x.filewatcher.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;

import org.cs27x.dropbox.DropboxClient;
import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.dropbox.DropboxTransport;
import org.cs27x.dropbox.FileManager;
import org.cs27x.filewatcher.FileChangeEvent;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileEventSource;
import org.cs27x.filewatcher.FileSystemState;
import org.junit.Before;
import org.junit.Test;

public class DropboxClientTest {

	private static final String TEST_FILE = "test_file";
	private static final Path TEST_FILE_PATH = Paths.get(TEST_FILE);
	private static final Path TEST_FILE_RESOLVED_PATH = Paths.get("/foo/bar/"+TEST_FILE);

	private DropboxClient client_;
	private DropboxTransport transport_;
	private FileManager fileManager_;
	private FileSystemState fileSystemState_;
	
	@Before
	public void setUp(){
		FileEventSource es = mock(FileEventSource.class);
		
		fileSystemState_ = mock(FileSystemState.class);
		when(fileSystemState_.updateState(any(FileChangeEvent.class))).thenReturn(true);
		
		fileManager_ = mock(FileManager.class);
		when(fileManager_.ensureRelative(TEST_FILE_PATH)).thenReturn(TEST_FILE_PATH);
		when(fileManager_.resolve(any(String.class))).thenReturn(TEST_FILE_RESOLVED_PATH);
		
		transport_ = mock(DropboxTransport.class);
		client_ = new DropboxClient(fileSystemState_, es, fileManager_, transport_);
	}
	
	@Test
	public void testFileEventToCmd() {

		DropboxCmd expected = new DropboxCmd(OpCode.ADD, TEST_FILE, new byte[1]);
		FileEvent evt = new FileEvent(StandardWatchEventKinds.ENTRY_CREATE, TEST_FILE_PATH, new byte[1]);
		client_.handleEvent(evt);
		verify(transport_).publish(eq(expected));
		
		DropboxCmd expected2 = new DropboxCmd(OpCode.UPDATE, TEST_FILE, new byte[1]);
		FileEvent evt2 = new FileEvent(StandardWatchEventKinds.ENTRY_MODIFY, TEST_FILE_PATH, new byte[1]);
		client_.handleEvent(evt2);
		verify(transport_).publish(eq(expected2));
		
		DropboxCmd expected3 = new DropboxCmd(OpCode.REMOVE, TEST_FILE, new byte[1]);
		FileEvent evt3 = new FileEvent(StandardWatchEventKinds.ENTRY_DELETE, TEST_FILE_PATH, new byte[1]);
		client_.handleEvent(evt3);
		verify(transport_).publish(eq(expected3));
	}
	
	@Test
	public void testFileEventAndCmdNonPropagation() throws IOException {
		when(fileSystemState_.updateState(any(FileChangeEvent.class))).thenReturn(false);
		
		FileEvent evt = new FileEvent(StandardWatchEventKinds.ENTRY_CREATE, TEST_FILE_PATH, new byte[1]);
		client_.handleEvent(evt);
		verify(transport_, never()).publish(any(DropboxCmd.class));
		
		
		DropboxCmd update = new DropboxCmd(OpCode.UPDATE, TEST_FILE, new byte[1]);
		client_.handleCmd(update);
		verify(fileManager_, never()).write(any(Path.class), any(byte[].class), any(boolean.class));
	}
	
	@Test
	public void testCmdToFileChanges() throws Exception {
		byte[] data = new byte[3];
		data[1] = 1;
		
		DropboxCmd add = new DropboxCmd(OpCode.ADD, TEST_FILE, data);
		client_.handleCmd(add);
		verify(fileManager_).write(TEST_FILE_RESOLVED_PATH, data, true);
		
		DropboxCmd update = new DropboxCmd(OpCode.UPDATE, TEST_FILE, data);
		client_.handleCmd(update);
		verify(fileManager_, times(2)).write(TEST_FILE_RESOLVED_PATH, data, true);
		
		DropboxCmd delete = new DropboxCmd(OpCode.REMOVE, TEST_FILE, data);
		client_.handleCmd(delete);
		verify(fileManager_).delete(TEST_FILE_RESOLVED_PATH);
	}

}
