package com.mscg.emule.handler;

import android.os.Message;

import com.mscg.emule.DownloadList;

public class TransfersHandler extends GenericSpeedInfoHandler {

	public TransfersHandler(DownloadList context) {
		super(context);
	}

	@Override
	public void handleMessage(Message msg) {
		switch(msg.what) {
		default:
			super.handleMessage(msg);
		}
	}

}
