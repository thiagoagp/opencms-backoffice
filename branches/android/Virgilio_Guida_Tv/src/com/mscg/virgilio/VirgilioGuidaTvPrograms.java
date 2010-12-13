package com.mscg.virgilio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mscg.virgilio.adapters.ProgramsListItemAdapter;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.TVProgram;

public class VirgilioGuidaTvPrograms extends Activity {

	public static final String CHANNEL = VirgilioGuidaTvPrograms.class.getCanonicalName() + ".channel";

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
				channel = (Channel) intent.getSerializableExtra(CHANNEL);
			} catch(Exception e) {
				Log.e(VirgilioGuidaTvPrograms.class.getCanonicalName(),
					"Cannot get channel from intent", e);
			}

			if(channel != null) {
				String title = getString(R.string.programs_list_title);
				title = title.replace("${channelName}", channel.getName());
				setTitle(title);

				programsAdapter = new ProgramsListItemAdapter(this, R.layout.program_list_layout, channel.getTVPrograms());
				programsListView.setAdapter(programsAdapter);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
