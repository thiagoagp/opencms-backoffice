package com.mscg.emule;

import org.apache.http.client.methods.HttpGet;

import android.os.Bundle;
import android.util.Log;

import com.mscg.emule.handler.TransfersHandler;
import com.mscg.emule.net.handler.TransfersNetHandler;
import com.mscg.emule.util.Preferences;
import com.mscg.emule.util.Util;
import com.mscg.net.HttpClientManager;

public class DownloadList extends GenericSpeedInfoActivity {

	private int updateTime;

	private UpdateThread updateThread;

	@Override
	public int getActivityLayout() {
		return R.layout.downloadlist;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handler = new TransfersHandler(this);

		updateThread = new UpdateThread();
		updateTime = Preferences.getInstance().getInt(Preferences.UPDATE_TIME, 30000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			updateThread.interrupt();
		} catch(Exception e){}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateThread.start();
	}

	@Override
	public boolean update() {
		updateThread.setUpdate(true);
		updateThread.interrupt();
		return true;
	}

	private class UpdateThread extends Thread {

		private boolean update;
		private Thread httpThread;

		public synchronized boolean isUpdate() {
			return update;
		}

		@Override
		public void run() {
			do {
				setUpdate(false);
				try {
					while(!interrupted()) {
						Preferences prefs = Preferences.getInstance();
						String url = prefs.getString(Preferences.WEBSERVER_URL, "") +
							"?ses=" + prefs.getInt(Preferences.SESSION_ID, -1) + "&w=transfer";
						HttpGet get = new HttpGet(url);
						httpThread = HttpClientManager.executeAsynchMethod(get,
							new TransfersNetHandler(handler, Util.getHtmlCleanerStandardProperties()));
						httpThread.join();
						sleep(updateTime);
					}
				} catch(InterruptedException e) {
					Log.i(this.getClass().getCanonicalName(), "Thread is interrupted: " + e.getMessage());
					try {
						httpThread.interrupt();
					} catch(Exception e2){}
					httpThread = null;
				}
			} while(isUpdate());
		}

		public synchronized void setUpdate(boolean update) {
			this.update = update;
		}

	}
}
