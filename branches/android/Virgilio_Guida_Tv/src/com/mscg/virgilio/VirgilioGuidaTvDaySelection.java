package com.mscg.virgilio;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mscg.virgilio.adapters.DayListItemAdapter;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.listener.DaySelectionClickListener;
import com.mscg.virgilio.net.HttpClientManager;
import com.mscg.virgilio.util.CacheManager;

public class VirgilioGuidaTvDaySelection extends Activity {

	private Handler guiHandler;

	private ListView daysList;
	private ArrayAdapter<Integer> daysListAdapter;
	private Map<Integer, ProgressDialog> progressDialogs;
	private ProgressDialog progressDialog;

	private static final Integer days[] = {0, 1, 2, 3, 4, 5, 6};

    public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialogs = new HashMap<Integer, ProgressDialog>();

        CacheManager.init(this);
        HttpClientManager.open();
        guiHandler = new DownloadProgressHandler(this);

        setContentView(R.layout.days_selection);

        daysList = (ListView)findViewById(R.id.daysList);
        daysListAdapter = new DayListItemAdapter(
        	this,
        	R.layout.day_list_layout,
        	days);

        daysList.setAdapter(daysListAdapter);
        daysList.setOnItemClickListener(new DaySelectionClickListener(this, guiHandler));
    }

	@Override
	protected Dialog onCreateDialog(int id) {
    	ProgressDialog progressDialog = new ProgressDialog(this);
    	progressDialog.setCancelable(false);
		switch(id) {
		case DownloadProgressHandler.START_DOWNLOAD:
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(getString(R.string.contacting_server));
			break;
		case DownloadProgressHandler.UPDATE_PROGRESS:
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(10000);
			progressDialog.setMessage(getString(R.string.analyzing_data));
			break;
		case DownloadProgressHandler.START_LOAD:
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(getString(R.string.loading_data));
			break;
		case DownloadProgressHandler.END_PARSE:
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(getString(R.string.saving_data));
			break;
		default:
			return null;
		}
		progressDialogs.put(id, progressDialog);
		return progressDialog;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		HttpClientManager.close();

		IOUtils.closeQuietly(CacheManager.getInstance());

		finish();
	}

	public void showDownloadDialog(int id) {
		//showDialog(id);
		onCreateDialog(id);
		progressDialog = progressDialogs.get(id);
		progressDialog.show();
	}

}