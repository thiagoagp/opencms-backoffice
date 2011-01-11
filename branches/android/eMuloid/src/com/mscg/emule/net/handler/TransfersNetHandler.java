package com.mscg.emule.net.handler;

import org.htmlcleaner.CleanerProperties;
import org.w3c.dom.Document;

import android.os.Handler;
import android.os.Message;

import com.mscg.emule.util.Constants;
import com.mscg.io.InputStreamDataReadListener;

public class TransfersNetHandler extends GenericSpeedInfoNetHandler {

	public TransfersNetHandler(Handler handler, boolean localCache) {
		super(handler, localCache);
	}

	public TransfersNetHandler(Handler handler, CleanerProperties props, boolean localCache, InputStreamDataReadListener dataListener) {
		super(handler, props, localCache, dataListener);
	}

	public TransfersNetHandler(Handler handler, CleanerProperties props) {
		super(handler, props);
	}

	public TransfersNetHandler(Handler handler) {
		super(handler);
	}

	@Override
	public void handleDocument(Document document) throws Exception {
		Message m = handler.obtainMessage(Constants.Messages.UPDATE_TERMINATED);
		handler.sendMessage(m);
	}

}
