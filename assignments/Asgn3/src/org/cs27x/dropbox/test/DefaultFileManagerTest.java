package org.cs27x.dropbox.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.cs27x.dropbox.test.TestData.*;

import org.cs27x.dropbox.DefaultFileManager;
import org.junit.Test;

public class DefaultFileManagerTest {

	@Test
	public void testDelete() throws Exception {
		
		Path target = null;
		DefaultFileManager mgr = new DefaultFileManager(Paths.get(TEST_WORKING_DIR));
		
		try {
			target = Paths.get(TEST_WORKING_DIR + "/"
					+ UUID.randomUUID().toString() + ".png");
			Files.copy(TEST_FILE, target);

			assertTrue(Files.exists(target));//sanity check
			
			mgr.delete(target);
			
			assertTrue(!Files.exists(target));
			
		} finally {
			if (target != null && Files.exists(target)) {
				Files.delete(target);
			}
		}
	}
	
	@Test
	public void testExists() throws Exception {
		
		Path target = null;
		DefaultFileManager mgr = new DefaultFileManager(Paths.get(TEST_WORKING_DIR));
		
		try {
			target = Paths.get(TEST_WORKING_DIR + "/"
					+ UUID.randomUUID().toString() + ".png");
			Files.copy(TEST_FILE, target);

			assertTrue(Files.exists(target));//sanity check
			assertTrue(mgr.exists(target));
			
			Files.delete(target);
			
			assertTrue(!Files.exists(target));//sanity check
			assertTrue(!mgr.exists(target));
			
		} finally {
			if (target != null && Files.exists(target)) {
				Files.delete(target);
			}
		}
	}

	@Test
	public void testWrite() throws Exception {

		DefaultFileManager mgr = new DefaultFileManager(Paths.get(TEST_WORKING_DIR));

		byte[] data = Files.readAllBytes(TEST_FILE);

		Path target = null;

		try {
			target = Paths.get(TEST_WORKING_DIR + "/"
					+ UUID.randomUUID().toString() + ".png");
			mgr.write(target, data, false);

			assertTrue(verifySameFile(TEST_FILE, target));

			data = Files.readAllBytes(TEST_FILE2);
			mgr.write(target, data, false);

			assertTrue(verifySameFile(TEST_FILE, target));

			mgr.write(target, data, true);

			assertTrue(verifySameFile(TEST_FILE2, target));

		} finally {
			if (target != null && Files.exists(target)) {
				Files.delete(target);
			}
		}
	}

	public boolean verifySameFile(Path p1, Path p2) throws IOException {
		long size = Files.size(p2);
		long orig = Files.size(p1);

		boolean same = orig == size;
		if (same) {

			byte[] data = Files.readAllBytes(p1);
			byte[] written = Files.readAllBytes(p2);

			for (int i = 0; i < data.length; i++) {
				same = data[i] == written[i];
				if (!same) {
					break;
				}
			}
		}

		return same;
	}

}
