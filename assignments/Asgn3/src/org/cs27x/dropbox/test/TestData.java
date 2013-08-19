package org.cs27x.dropbox.test;

import static org.cs27x.dropbox.test.TestData.CLIENT_DIR;
import static org.cs27x.dropbox.test.TestData.SERVER_DIR;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestData {

	public static final String TEST_DATA_DIR = "test-data";
	public static final String TEST_INVARIANT_DIR = TEST_DATA_DIR + "/"
			+ "invariant";
	public static final String TEST_WORKING_DIR = TEST_DATA_DIR + "/"
			+ "working-dir";

	public static final Path TEST_FILE = Paths.get(TEST_INVARIANT_DIR
			+ "/vandy.png");

	public static final Path TEST_FILE2 = Paths.get(TEST_INVARIANT_DIR
			+ "/cornelius.jpg");

	public static final Path SERVER_DIR = Paths
			.get(System.getProperty("java.io.tmpdir"), "test-data/integration/server");
	public static final Path CLIENT_DIR = Paths
			.get(System.getProperty("java.io.tmpdir"), "test-data/integration/client");
	
	public static void cleanClientServerTestDirs() throws Exception {
		List<Path> todelete = new ArrayList<>();
		for (Path child : Files.newDirectoryStream(CLIENT_DIR)) {
			todelete.add(child);
		}
		for (Path child : Files.newDirectoryStream(SERVER_DIR)) {
			todelete.add(child);
		}
		for (Path child : todelete) {
			Files.delete(child);
		}
	}
}
