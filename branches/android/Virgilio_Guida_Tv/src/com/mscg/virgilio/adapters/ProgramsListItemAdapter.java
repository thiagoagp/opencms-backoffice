package com.mscg.virgilio.adapters;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.programs.TVProgram;

public class ProgramsListItemAdapter extends GenericListItemAdapter<TVProgram> {

	private SimpleDateFormat hourFormatter;

	public ProgramsListItemAdapter(Context context, int textViewResourceId, TVProgram[] objects) {
		this(context, textViewResourceId, Arrays.asList(objects));
	}

	public ProgramsListItemAdapter(Context context, int textViewResourceId, List<TVProgram> objects) {
		super(context, textViewResourceId, objects);

		hourFormatter = new SimpleDateFormat(context.getString(R.string.hours_format));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = null;

		if(convertView == null) {
			itemView = vi.inflate(textViewResourceId, null, true);
		}
		else {
			itemView = convertView;
		}

		TVProgram program = getItem(position);

		TextView startHour = (TextView)itemView.findViewById(R.id.startHour);
		TextView endHour = (TextView)itemView.findViewById(R.id.endHour);
		TextView programName = (TextView)itemView.findViewById(R.id.programName);
		TextView programCategory = (TextView)itemView.findViewById(R.id.programCategory);

		startHour.setText(hourFormatter.format(program.getStartTime()));
		endHour.setText(hourFormatter.format(program.getEndTime()));
		programName.setText(program.getName());
		programCategory.setText(program.getCategory());

		return itemView;
	}

}
