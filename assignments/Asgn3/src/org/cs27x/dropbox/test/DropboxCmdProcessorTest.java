package org.cs27x.dropbox.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.cs27x.dropbox.test.TestData.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.dropbox.DropboxCmdProcessor;
import org.cs27x.dropbox.FileManager;
import org.cs27x.filewatcher.FileState;
import org.cs27x.filewatcher.FileStates;
import org.junit.Before;
import org.junit.Test;

public class DropboxCmdProcessorTest {

	private DropboxCmdProcessor processor_;
	private FileManager fileManagerMock_;
	private FileStates fileStates_;

	@Before
	public void setUp() {
		fileManagerMock_ = mock(FileManager.class);
		fileStates_ = new FileStates();
		when(fileManagerMock_.resolve(any(String.class))).thenReturn(TEST_FILE);

		processor_ = new DropboxCmdProcessor(fileStates_, fileManagerMock_);
	}

	public DropboxCmd createCmdWithData(OpCode code, Path pathtodata)
			throws IOException {
		DropboxCmd cmd = new DropboxCmd();
		cmd.setOpCode(code);
		byte[] data = Files.readAllBytes(pathtodata);
		cmd.setData(data);
		cmd.setPath(pathtodata.getFileName().toString());
		return cmd;
	}

	@Test
	public void testAdd() throws Exception {

		DropboxCmd cmd = createCmdWithData(OpCode.ADD, TEST_FILE);
		processor_.cmdReceived(cmd);

		verify(fileManagerMock_).write(TEST_FILE, cmd.getData(), false);

		FileState state = fileStates_.getState(TEST_FILE);
		assertEquals(Files.size(TEST_FILE), state.getSize());
		assertEquals(Files.getLastModifiedTime(TEST_FILE),
				state.getLastModificationDate());
	}

	@Test
	public void testUpdate() throws Exception {
		DropboxCmd cmd = createCmdWithData(OpCode.UPDATE, TEST_FILE);
		processor_.cmdReceived(cmd);

		verify(fileManagerMock_).write(TEST_FILE, cmd.getData(), true);
		
		FileState state = fileStates_.getState(TEST_FILE);
		assertEquals(Files.size(TEST_FILE), state.getSize());
		assertEquals(Files.getLastModifiedTime(TEST_FILE),
				state.getLastModificationDate());
	}

	@Test
	public void testRemove() throws Exception {
		DropboxCmd cmd = new DropboxCmd();
		cmd.setOpCode(OpCode.REMOVE);
		cmd.setPath(TEST_FILE.getFileName().toString());
		processor_.cmdReceived(cmd);

		verify(fileManagerMock_).delete(TEST_FILE);
		
		FileState state = fileStates_.getState(TEST_FILE);
		assertTrue(state == null || -1 == state.getSize());
	}
}
