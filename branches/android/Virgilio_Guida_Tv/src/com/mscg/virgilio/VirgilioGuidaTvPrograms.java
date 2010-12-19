package com.mscg.virgilio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mscg.virgilio.adapters.ProgramsListItemAdapter;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.CacheManager;

public class VirgilioGuidaTvPrograms extends GenericActivity {

	public static final String CHANNEL = VirgilioGuidaTvPrograms.class.getCanonicalName() + ".channel";
	public static final String PROGRAMS = VirgilioGuidaTvPrograms.class.getCanonicalName() + ".programs";

	private ArrayAdapter<TVProgram> programsAdapter;
	private ListView programsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.program_selection);

		programsListView = (ListView)findViewById(R.id.programsList);
	}

	@Override
	protected void onStart() {
		super.onStart();

		Intent intent = getIntent();

		if(intent != null) {
			Channel channel = null;
			try {
				long programsID = intent.getLongExtra(PROGRAMS, -1);
				long channelID = intent.getLongExtra(CHANNEL, -1);
				if(programsID >= 0 && channelID >= 0) {
					channel = CacheManager.getInstance().getChannel(programsID, channelID);
					if(channel == null) {
						throw new NullPointerException("Cannot find channel info");
					}
				}
				else
					throw new Exception("Invalid intent paramenters");
			} catch(Exception e) {
				Log.e(VirgilioGuidaTvPrograms.class.getCanonicalName(),
					"Cannot get channel from intent", e);
			}

			if(channel != null) {
				String title = getString(R.string.programs_list_title);
				title = title.replace("${channelName}", channel.getName());
				setTitle(title);

				programsAdapter = new ProgramsListItemAdapter(this,
					R.layout.program_list_layout,
					channel.getTVPrograms());
				programsListView.setAdapter(programsAdapter);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
