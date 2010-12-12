package com.mscg.virgilio.handlers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvChannelSelection;
import com.mscg.virgilio.VirgilioGuidaTvDaySelection;
import com.mscg.virgilio.programs.Programs;

public class DownloadProgressHandler extends Handler {

	public static final String TYPE = DownloadProgressHandler.class.getCanonicalName() + ".type";
	public static final String PROGRAMS = DownloadProgressHandler.class.getCanonicalName() + ".programs";
	public static final String MESSAGE = DownloadProgressHandler.class.getCanonicalName() + ".message";

	public static final int START_DOWNLOAD = 0;
	public static final int UPDATE_PROGRESS = 1;
	public static final int END_DOWNLOAD = 2;
	public static final int END_PARSE = 3;
	public static final int START_LOAD = 4;
	public static final int END_SAVE = 5;
	public static final int ERROR = 6;

	private VirgilioGuidaTvDaySelection context;

	public DownloadProgressHandler(VirgilioGuidaTvDaySelection context) {
		this.context = context;
	}

	@Override
	public void handleMessage(Message msg) {
		Bundle b = msg.getData();
		int caseValue = b.getInt(TYPE);
		switch(caseValue) {
		case UPDATE_PROGRESS:
			if(context.getProgressDialog().isIndeterminate()) {
				if(context.getProgressDialog() != null)
					context.getProgressDialog().dismiss();
				context.showDownloadDialog(UPDATE_PROGRESS);
				context.getProgressDialog().setProgress(0);
			}
			int position = b.getInt("progress");
			context.getProgressDialog().setProgress(position);
			break;
		case END_DOWNLOAD:
			break;
		case START_DOWNLOAD:
		case END_PARSE:
			if(context.getProgressDialog() != null)
				context.getProgressDialog().dismiss();
			context.showDownloadDialog(caseValue);
			break;
		case END_SAVE:
			if(context.getProgressDialog() != null)
				context.getProgressDialog().dismiss();
			Intent intent = new Intent(context, VirgilioGuidaTvChannelSelection.class);
			intent.putExtra(PROGRAMS, ((Programs)b.getSerializable(PROGRAMS)).getDate());
			context.startActivity(intent);
			break;
		case ERROR:
			if(context.getProgressDialog() != null)
				context.getProgressDialog().dismiss();
			AlertDialog.Builder ad = new AlertDialog.Builder(context);
			ad.setTitle(context.getString(R.string.error_title));
			ad.setMessage(context.getString(R.string.error_text) + b.getString(MESSAGE));
			ad.setCancelable(true);
			ad.show();
			break;
		}
	}

}
