package com.mscg.emule.handler;

import java.util.List;

import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.mscg.emule.DownloadList;
import com.mscg.emule.R;
import com.mscg.emule.bean.CategoryBean;
import com.mscg.emule.util.Constants;

public class TransfersHandler extends GenericSpeedInfoHandler {

	public TransfersHandler(DownloadList context) {
		super(context);
	}

	@Override
	public void handleMessage(Message msg) {
		Spinner category = null;
		SpinnerAdapter categoryAdapter = null;

		switch(msg.what) {
		case Constants.Messages.Transfers.UPDATE_CATEGORIES:
			categoryAdapter = new ArrayAdapter<CategoryBean>(
				context,
				R.layout.category_dropdown,
				(List<CategoryBean>)msg.obj);
			((ArrayAdapter)categoryAdapter).setDropDownViewResource(
					android.R.layout.simple_spinner_dropdown_item);
			category = ((DownloadList)context).getCategoriesSpinner();
			category.setAdapter(categoryAdapter);
			break;
		default:
			super.handleMessage(msg);
		}
	}

}
