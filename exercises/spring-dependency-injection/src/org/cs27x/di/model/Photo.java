package org.cs27x.di.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Photo {

	private final String id_;

	private final byte[] data_;

	public Photo(String id, byte[] data) {
		super();
		data_ = data;
		id_ = id;
	}

	@JsonIgnore
	public byte[] getData() {
		return data_;
	}

	public long getSize() {
		return (data_ != null) ? data_.length : 0;
	}

	public String getId() {
		return id_;
	}

}
