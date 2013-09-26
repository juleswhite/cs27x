package org.cs27x.dropbox;

import java.io.IOException;
import java.nio.file.Path;

public interface FileManager {

	public static final String ROOT_DIR = "rootDir";

	/**
	 * This method converts a path that is possibly specific to the host into a
	 * relative path to the shared file system.
	 * 
	 * @param relativePathName
	 * @return
	 */
	public Path ensureRelative(Path path);

	/**
	 * This method resolves a relative path from the shared file system to an
	 * absolute path tied to the host that the FileManager is operating on.
	 * 
	 * @param relativePathName
	 * @return
	 */
	public Path resolve(String relativePathName);

	/**
	 * This method returns true if the specified path exists on the file system
	 * that the FileManager is operating on.
	 * 
	 * @param p
	 * @return
	 */
	public boolean exists(Path p);
	
	/**
	 * Read the specified file into a byte[] array.
	 * 
	 * @param p
	 * @return
	 */
	public byte[] read(Path p) throws IOException;

	/**
	 * This method writes the specified data to the given path on the file
	 * system that the FileManager is operating on.
	 * 
	 * @param p
	 * @param data
	 * @param overwrite
	 * @throws IOException
	 */
	public void write(Path p, byte[] data, boolean overwrite)
			throws IOException;

	/**
	 * This method deletes the specified path on the file system that the
	 * FileManager is operating on.
	 * 
	 * @param p
	 * @param data
	 * @param overwrite
	 * @throws IOException
	 */
	public void delete(Path p) throws IOException;

}
