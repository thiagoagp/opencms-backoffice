package com.mscg.virgilio.adapters;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.util.ChannelLinearLayout;

public class ChannelListItemAdapter extends GenericListItemAdapter<Channel> {

	public ChannelListItemAdapter(Context context, int textViewResourceId, Channel[] objects) {
		this(context, textViewResourceId, Arrays.asList(objects));
	}

	public ChannelListItemAdapter(Context context, int textViewResourceId, List<Channel> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = null;
		Channel channel = getItem(position);
		if(convertView == null) {
			itemView = new ChannelLinearLayout(channel, getContext());
			vi.inflate(textViewResourceId, (ChannelLinearLayout)itemView, true);
		}
		else {
			itemView = convertView;
		}

		TextView channelText = (TextView)itemView.findViewById(R.id.channelName);
		channelText.setText(channel.getName());

		return itemView;
	}

}
