package org.cs27x.dropbox;

import com.beust.jcommander.Parameter;

public class DropboxOptions {

	@Parameter(names = { "-dir" }, description = "The directory to share. [required]")
	private String rootDir_;

	@Parameter(names = { "-connectTo" }, description = "The initial peer to connect to. [optional]")
	private String host_;

	public String getRootDir() {
		return rootDir_;
	}

	public void setRootDir(String rootDir) {
		rootDir_ = rootDir;
	}

	public String getHost() {
		return host_;
	}

	public void setHost(String host) {
		host_ = host;
	}

}
