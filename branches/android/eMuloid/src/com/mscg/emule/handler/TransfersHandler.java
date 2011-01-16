package com.mscg.emule.handler;

import java.util.List;

import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.mscg.emule.DownloadList;
import com.mscg.emule.R;
import com.mscg.emule.bean.CategoryBean;
import com.mscg.emule.listener.PreventingUpdateItemSelectionListener;
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
			category = ((DownloadList)context).getCategoriesSpinner();

			PreventingUpdateItemSelectionListener selListener = (PreventingUpdateItemSelectionListener)category.getOnItemSelectedListener();
			if(selListener != null)
				selListener.setUpdatesToIgnore(category.getAdapter() != null ? 2 : 1);

			categoryAdapter = new ArrayAdapter<CategoryBean>(
				context,
				R.layout.category_dropdown,
				(List<CategoryBean>)msg.obj);
			((ArrayAdapter)categoryAdapter).setDropDownViewResource(
					android.R.layout.simple_spinner_dropdown_item);
			category.setAdapter(categoryAdapter);
			int index = 0;
			for(CategoryBean categoryBean : (List<CategoryBean>)msg.obj) {
				if(categoryBean.isChecked())
					break;
				index++;
			}
			category.setSelection(index);
			((ArrayAdapter)categoryAdapter).notifyDataSetChanged();

			break;
		default:
			super.handleMessage(msg);
		}
	}

}
