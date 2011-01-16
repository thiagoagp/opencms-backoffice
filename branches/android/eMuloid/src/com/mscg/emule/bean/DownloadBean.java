package com.mscg.emule.bean;

import android.util.Log;

import com.mscg.emule.util.Util;

public class DownloadBean {

	private DownloadState state;
	private String title;
	private Integer typeResource;

	public DownloadBean() {
		this(null, null, null);
	}

	public DownloadBean(DownloadState state, String title, Integer typeResource) {
		setState(state);
		setTitle(title);
	}

	public DownloadState getState() {
		return state;
	}

	public void setState(DownloadState state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTypeResource() {
		return typeResource;
	}

	public void setTypeResource(Integer typeResource) {
		this.typeResource = typeResource;
	}

	public void setTypeResourceFromName(String typeResourceName) {
		try {
			setTypeResource((Integer)Util.getDrawableIDByName(typeResourceName));
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Cannot get resource value", e);
		}
	}

}
