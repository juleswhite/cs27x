package org.cs27x.filewatcher;

import java.util.HashMap;
import java.util.Map;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;

public class FileSystemStateImpl implements FileSystemState {

	private static final int UNININITIALIZED = -2;
	
	private Map<String, FileState> states_ = new HashMap<>();
	
	private final HashFunction fileHashFunction_;
	
	/**
	 * Initializes the class with MD5 hashing to
	 * check for file changes.
	 * 
	 */
	public FileSystemStateImpl(){
		this(Hashing.md5());
	}

	public FileSystemStateImpl(HashFunction fileHashFunction) {
		super();
		fileHashFunction_ = fileHashFunction;
	}

	private synchronized FileState getOrCreateState(String key) {
		
		FileState state = states_.get(key);
		if (state == null) {
			state = new FileState(UNININITIALIZED);
			states_.put(key, state);
		}
		return state;
	}

	private boolean hashesMatch(HashCode h1, HashCode h2){
		return h1 == h2 || (h1 != null && h1.equals(h2));
	}
	
	@Override
	public synchronized boolean updateState(FileChangeEvent evt) {
		final String p = evt.getPath();
		final FileState state = getOrCreateState(p);
		return updateState(state, evt);
	}
	
	private boolean updateState(FileState state, final FileChangeEvent fd){
		final byte[] data = (fd.getData() != null)? fd.getData() : new byte[0];
		final long size = data.length;
		final HashCode incomingHash = fileHashFunction_.newHasher().putBytes(data).hash();
			
		final boolean update = size != state.getSize() || !hashesMatch(incomingHash, state.getFileHash());
			
		state.setSize(size);
		state.setFileHash(incomingHash);
			
		return update;
	}

	
}
