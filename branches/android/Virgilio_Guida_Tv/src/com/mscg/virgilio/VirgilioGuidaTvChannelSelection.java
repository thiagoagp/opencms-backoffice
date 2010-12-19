package com.mscg.virgilio;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mscg.virgilio.adapters.ChannelListItemAdapter;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.listener.ChannelSelectionClickListener;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.util.CacheManager;
import com.mscg.virgilio.util.Util;

public class VirgilioGuidaTvChannelSelection extends GenericActivity {

	private TextView channelsListIntro;
	private ListView channelsList;
	private ArrayAdapter<Channel> channelsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.channels_selection);

		channelsListIntro = (TextView) findViewById(R.id.channelsListIntro);
		channelsList = (ListView) findViewById(R.id.channelsList);
		channelsList.setOnItemClickListener(new ChannelSelectionClickListener(this));

	}

	@Override
	protected void onStart() {
		super.onStart();

		Intent intent = getIntent();
		Date day = null;
		try {
			day = (Date) intent.getSerializableExtra(DownloadProgressHandler.PROGRAMS);
		} catch(Exception e) {
			Log.e(VirgilioGuidaTvChannelSelection.class.getCanonicalName(),
				"Cannot get programs date from intent", e);
		}

		if(day != null) {
			Programs programs = CacheManager.getInstance().getProgramsForDay(day);

			if(programs != null) {
				day = programs.getDate();

				channelsListAdapter = new ChannelListItemAdapter(this, R.layout.channel_list_layout, programs.getChannels());
				channelsList.setAdapter(channelsListAdapter);

				SimpleDateFormat dayFormat = new SimpleDateFormat("EEEEE");
				SimpleDateFormat dateFormat = new SimpleDateFormat(", dd/MM");
				String intro = getString(R.string.select_channel_intro);
				intro = intro.replace("${dayName}", Util.capitalize(dayFormat.format(day)) + dateFormat.format(day));
				channelsListIntro.setText(intro);
			}
		}
		else {
			channelsListIntro.setText(R.string.select_channel_fail);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
