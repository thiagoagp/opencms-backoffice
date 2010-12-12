package com.mscg.virgilio.util.net;

import org.apache.http.client.ResponseHandler;

public interface SyncResponseHandler<T> extends ResponseHandler<T> {

	public void startDownload();

}
