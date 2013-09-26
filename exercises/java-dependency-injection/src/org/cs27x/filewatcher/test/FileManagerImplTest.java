package org.cs27x.filewatcher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cs27x.dropbox.FileManagerImpl;
import org.junit.Before;
import org.junit.Test;


public class FileManagerImplTest {
	
	private static final String TEST_DATA = "test-data";
	private static final Path TEST_FILE_RELATIVE = Paths.get("invariant/vandy.png");
	private static final Path TEST_FILE = Paths.get("test-data/invariant/vandy.png");
	private static final Path TEST_FILE_COPY = Paths.get("test-data/vandy2.png");
	
	private FileManagerImpl fileManager_;
	
	@Before
	public void setUp(){
		fileManager_ = new FileManagerImpl(TEST_DATA);
	}
	
	@Test
	public void testExists() {
		assertTrue(fileManager_.exists(TEST_FILE));
	}
	
	@Test
	public void testResolve() {
		Path resolved = fileManager_.resolve(TEST_FILE_RELATIVE.toString()).toAbsolutePath();
		assertEquals(TEST_FILE.toAbsolutePath(), resolved);
	}
	
	@Test
	public void testEnsureRelative() {
		Path relative = fileManager_.ensureRelative(TEST_FILE);
		assertEquals(TEST_FILE_RELATIVE,relative);
	}
	
	@Test
	public void testWrite() throws Exception {
		try{
			fileManager_.write(TEST_FILE_COPY, Files.readAllBytes(TEST_FILE), true);
			assertTrue(Files.exists(TEST_FILE_COPY));
		} finally {
			if(Files.exists(TEST_FILE_COPY)){
				Files.delete(TEST_FILE_COPY);
			}
		}
	}
	
	@Test
	public void testDelete() throws Exception {
		try{
			Files.copy(TEST_FILE, TEST_FILE_COPY);
			fileManager_.delete(TEST_FILE_COPY);
			assertTrue(!Files.exists(TEST_FILE_COPY));
		} finally {
			if(Files.exists(TEST_FILE_COPY)){
				Files.delete(TEST_FILE_COPY);
			}
		}
	}

}
