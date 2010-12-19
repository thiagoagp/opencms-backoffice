package com.mscg.virgilio.listener;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mscg.virgilio.CacheManagement;
import com.mscg.virgilio.util.CacheManager;

public class CacheManagementButtonListener implements OnClickListener {

	private CacheManagement context;
	private Button emptyCacheButton;
	private Button emptyOlderDBButton;
	private Button emptyDBButton;

	public CacheManagementButtonListener(CacheManagement context,
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
		boolean update = false;
		CacheManager cache = CacheManager.getInstance();
		if(v == emptyCacheButton) {
			cache.clearChannelsCache();
			cache.clearProgramsCache();
			update = true;
		}
		else if(v == emptyOlderDBButton) {
			update = true;
		}
		else if(v == emptyDBButton) {
			update = true;
		}
		if(update)
			context.updateCounts();
	}

}
