package com.mscg.virgilio.programs;

import java.io.Serializable;
import java.util.Date;

public class ProgramDetails implements Serializable {

	private static final long serialVersionUID = -307399456122858704L;

	public static final int GREEN = 0;
	public static final int YELLOW = 1;
	public static final int RED = 2;

	private String strId;
	private Date lastUpdate;
	private String title;
	private String description;
	private int level;
	private String url;

	public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
