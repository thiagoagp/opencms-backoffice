package com.mscg.virgilio.adapters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.util.DayLinearLayout;
import com.mscg.virgilio.util.Util;

public class DayListItemAdapter extends ArrayAdapter<Integer> {

	private int resource;
	private LayoutInflater vi;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dayFormat;

	public DayListItemAdapter(Context context, int resource, Integer[] objects) {
		super(context, resource, objects);
		this.resource = resource;
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		vi = (LayoutInflater) getContext().getSystemService(inflater);
		dateFormat = new SimpleDateFormat(getContext().getString(R.string.select_day_format));
		dayFormat = new SimpleDateFormat("EEEEE");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DayLinearLayout dayView = null;
		Integer dayOffset = getItem(position);
		if(convertView == null) {
			dayView = new DayLinearLayout(getContext());
			vi.inflate(resource, dayView, true);
		}
		else {
			dayView = (DayLinearLayout)convertView;
		}

		Calendar now = new GregorianCalendar();
		now.add(Calendar.DAY_OF_MONTH, dayOffset);

		Calendar dayCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		dayCal.setTimeInMillis(now.getTimeInMillis());
		dayView.setDay(dayCal);

		if(position == 0)
			dayView.setBackgroundColor(getContext().getResources().getColor(R.color.actual_day));
		else
			dayView.setBackgroundColor(getContext().getResources().getColor(R.color.default_day));

		((TextView)dayView.findViewById(R.id.dayName)).setText(
			Util.capitalize(dayFormat.format(now.getTime()))
		);
		((TextView)dayView.findViewById(R.id.dayNumber)).setText(dateFormat.format(now.getTime()));

		return dayView;
	}

}
