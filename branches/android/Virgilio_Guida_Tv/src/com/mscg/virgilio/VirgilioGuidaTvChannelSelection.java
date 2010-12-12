package com.mscg.virgilio;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.util.CacheManager;

public class VirgilioGuidaTvChannelSelection extends Activity {

	private ListView channelsList;
	private ArrayAdapter<String> channelsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.channels_selection);

		channelsList = (ListView) findViewById(R.id.channelsList);

		Intent intent = getIntent();
		Date day = null;
		try {
			day = (Date) intent.getSerializableExtra(DownloadProgressHandler.PROGRAMS);
		} catch(Exception e) {
			Log.e(VirgilioGuidaTvChannelSelection.class.getCanonicalName(),
				"Cannot get programs date from intent", e);
		}

		Programs programs = CacheManager.getInstance().getProgramsForDay(day);

		if(programs != null) {
			String channels[] = new String[programs.getChannels().size()];
			int i = 0;
			for(Channel channel : programs.getChannels()) {
				channels[i++] = channel.getName();
			}
			channelsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, channels);
			channelsList.setAdapter(channelsListAdapter);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
