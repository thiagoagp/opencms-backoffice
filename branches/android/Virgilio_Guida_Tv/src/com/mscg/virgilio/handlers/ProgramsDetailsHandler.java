package com.mscg.virgilio.handlers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvPrograms;
import com.mscg.virgilio.programs.ProgramDetails;

public class ProgramsDetailsHandler extends Handler {

	private VirgilioGuidaTvPrograms context;
	private ProgramDetails programDetails;
	private ProgressDialog progressDialog;

	private String contactingServer;
	private String analyzingData;
	private String errorTitle;
	private String errorText;
	private String closeText;

	public ProgramsDetailsHandler(VirgilioGuidaTvPrograms context) {
		super();
		this.context = context;

		contactingServer = context.getString(R.string.contacting_server);
		analyzingData = context.getString(R.string.analyzing_data);
		errorTitle = context.getString(R.string.error_title);
		errorText = context.getString(R.string.error_text);
		closeText = context.getString(R.string.close);
	}

	@Override
	public void handleMessage(Message msg) {
		AlertDialog.Builder ad = null;
		Bundle b = msg.getData();
		int caseValue = b.getInt(DownloadProgressHandler.TYPE);

		switch(caseValue) {
		case DownloadProgressHandler.START_DOWNLOAD:
			if(progressDialog != null)
				progressDialog.dismiss();
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(contactingServer);
			progressDialog.show();
			break;
		case DownloadProgressHandler.UPDATE_PROGRESS:
			if(progressDialog == null || progressDialog.isIndeterminate()) {
				if(progressDialog != null)
					progressDialog.dismiss();
				progressDialog = new ProgressDialog(context);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setTitle(analyzingData);
				progressDialog.setMax(10000);
				progressDialog.setMax(10000);
				progressDialog.setProgress(0);
			}
			int position = b.getInt("progress");
			progressDialog.setProgress(position);
			break;
		case DownloadProgressHandler.END_DOWNLOAD:
			break;
		case DownloadProgressHandler.END_PARSE:
			if(progressDialog != null)
				progressDialog.dismiss();
			ad = new AlertDialog.Builder(context);
			ad.setTitle(programDetails.getTitle());
			ad.setMessage(programDetails.getDescription());
			ad.setNegativeButton(closeText, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.setCancelable(true);
			ad.show();
			break;
		case DownloadProgressHandler.ERROR:
			ad = new AlertDialog.Builder(context);
			ad.setTitle(errorTitle);
			ad.setMessage(errorText + " " + b.getString(DownloadProgressHandler.MESSAGE));
			ad.setCancelable(true);
			ad.show();
			break;
		}
	}

	public synchronized ProgramDetails getProgramDetails() {
		return programDetails;
	}

	public synchronized void setProgramDetails(ProgramDetails programDetails) {
		this.programDetails = programDetails;
	}

}
