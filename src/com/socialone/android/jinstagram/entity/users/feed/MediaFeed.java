package com.socialone.android.jinstagram.entity.users.feed;

import com.google.gson.annotations.SerializedName;
import com.socialone.android.jinstagram.InstagramObject;
import com.socialone.android.jinstagram.entity.common.Meta;
import com.socialone.android.jinstagram.entity.common.Pagination;

import java.util.List;

public class MediaFeed extends InstagramObject {
	@SerializedName("data")
	private List<MediaFeedData> data;

	@SerializedName("meta")
	private Meta meta;

	@SerializedName("pagination")
	private Pagination pagination;

	/**
	 * @return the pagination
	 */
	public Pagination getPagination() {
		return pagination;
	}

	/**
	 * @param pagination the pagination to set
	 */
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return the meta
	 */
	public Meta getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	/**
	 * @return the data
	 */
	public List<MediaFeedData> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<MediaFeedData> data) {
		this.data = data;
	}

    @Override
    public String toString() {
        return String.format("MediaFeed [data=%s, meta=%s, pagination=%s]", data, meta, pagination);
    }
}
