package org.cs27x.dropbox;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import org.cs27x.filewatcher.FileChangeEvent;

/**
 * A DropboxCmd represents a change to the shared file
 * system that needs to be executed on each client.
 * 
 * 
 * @author jules
 *
 */
public class DropboxCmd implements Serializable, FileChangeEvent {

	private static final long serialVersionUID = 1L;

	public enum OpCode {
		ADD, REMOVE, UPDATE, SYNC, GET
	}

	private final String path_;
	private final byte[] data_;
	private final OpCode opCode_;

	public DropboxCmd(OpCode opCode, String path, byte[] data) {
		super();
		path_ = path;
		data_ = data;
		opCode_ = opCode;
	}
	
	public DropboxCmd(OpCode opCode, String path) {
		this(opCode,path,null);
	}
	
	public String getPath() {
		return path_;
	}

	/* (non-Javadoc)
	 * @see org.cs27x.dropbox.FileData#getData()
	 */
	@Override
	public byte[] getData() {
		return data_;
	}

	public OpCode getOpCode() {
		return opCode_;
	}

	@Override
	public int hashCode() {
		return Objects.hash(opCode_, path_, data_);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DropboxCmd && equals((DropboxCmd)obj);
	}
	
	public boolean equals(DropboxCmd cmd){
		return Objects.equals(opCode_, cmd.getOpCode()) 
			&& Objects.equals(path_, cmd.getPath())
			&& Arrays.equals(data_, cmd.getData());
	}

	
}
