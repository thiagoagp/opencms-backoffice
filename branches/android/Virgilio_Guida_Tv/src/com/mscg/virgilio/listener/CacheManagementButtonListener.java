package com.mscg.virgilio.listener;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mscg.virgilio.VirgilioGuidaTvCacheManagement;
import com.mscg.virgilio.handlers.CacheManagementHandler;
import com.mscg.virgilio.util.CacheManager;

public class CacheManagementButtonListener implements OnClickListener {

	private VirgilioGuidaTvCacheManagement context;
	private Button emptyCacheButton;
	private Button emptyOlderDBButton;
	private Button emptyDBButton;

	public CacheManagementButtonListener(VirgilioGuidaTvCacheManagement context,
			Button emptyCacheButton, Button emptyOlderDBButton, Button emptyDBButton) {

		super();
		this.context = context;
		this.emptyCacheButton = emptyCacheButton;
		this.emptyOlderDBButton = emptyOlderDBButton;
		this.emptyDBButton = emptyDBButton;

		this.emptyCacheButton.setOnClickListener(this);
		this.emptyOlderDBButton.setOnClickListener(this);
		this.emptyDBButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final CacheManager cache = CacheManager.getInstance();
		if(v == emptyCacheButton) {
			cache.clearChannelsCache();
			cache.clearProgramsCache();
			context.updateCounts();
		}
		else if(v == emptyOlderDBButton) {
			new Thread() {

				@Override
				public void run() {
					Message m = null;
					Bundle b = null;
					try {
						m = context.getGuiHandler().obtainMessage();
						b = m.getData();
						b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_START);
						context.getGuiHandler().sendMessage(m);

						cache.removeOlderPrograms(7);

						m = context.getGuiHandler().obtainMessage();
						b = m.getData();
						b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.UPDATE_COUNTS);
						context.getGuiHandler().sendMessage(m);
					} catch(Exception e) {
						Log.e(CacheManagementButtonListener.class.getCanonicalName(),
								"Cannot delete elements", e);
					} finally {
						m = context.getGuiHandler().obtainMessage();
						b = m.getData();
						b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_END);
						context.getGuiHandler().sendMessage(m);
					}
				}

			}.start();
		}
		else if(v == emptyDBButton) {
			new Thread() {

				@Override
				public void run() {
					Message m = null;
					Bundle b = null;
					try {
						m = context.getGuiHandler().obtainMessage();
						b = m.getData();
						b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_START);
						context.getGuiHandler().sendMessage(m);

						cache.removeAllPrograms();

						m = context.getGuiHandler().obtainMessage();
						b = m.getData();
						b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.UPDATE_COUNTS);
						context.getGuiHandler().sendMessage(m);
					} catch(Exception e) {
						Log.e(CacheManagementButtonListener.class.getCanonicalName(),
								"Cannot delete elements", e);
					} finally {
						m = context.getGuiHandler().obtainMessage();
						b = m.getData();
						b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_END);
						b.putInt(CacheManagementHandler.OPERATION, CacheManagementHandler.GOTO_HOME);
						context.getGuiHandler().sendMessage(m);
					}
				}

			}.start();
		}
	}

}
