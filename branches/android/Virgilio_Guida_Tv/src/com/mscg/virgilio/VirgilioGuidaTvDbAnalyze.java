package com.mscg.virgilio;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import com.mscg.virgilio.adapters.DBAnalyzeListItemAdapter;
import com.mscg.virgilio.database.ProgramsDB;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.listener.AnalyzeDBClickListener;
import com.mscg.virgilio.util.CacheManager;

public class VirgilioGuidaTvDbAnalyze extends GenericActivity implements OnCheckedChangeListener {

	private Handler guiHandler;

	private CheckBox selectAll;
	private ListView programsList;

	private Map<Integer, Boolean> checkedElems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.db_analyze_layout);

		guiHandler = new DownloadProgressHandler(this);

		selectAll = (CheckBox)findViewById(R.id.analyze_db_select_all);
		programsList = (ListView)findViewById(R.id.analyze_db_list);

		checkedElems = new HashMap<Integer, Boolean>();
		Cursor cur = CacheManager.getInstance().getPrograms();
		startManagingCursor(cur);

		while(cur.moveToNext()) {
			int id = cur.getInt(ProgramsDB.PROGRAMS_CONSTS.ID_COL_INDEX);
			checkedElems.put(id, Boolean.FALSE);
		}

		cur.requery();

		selectAll.setOnCheckedChangeListener(this);
		programsList.setAdapter(new DBAnalyzeListItemAdapter(this, R.layout.db_analyze_element_layout, cur, false));
		programsList.setOnItemClickListener(new AnalyzeDBClickListener(this, guiHandler));
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public Handler getGuiHandler() {
		return guiHandler;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		boolean selected = false;

		for(Boolean checked : checkedElems.values()) {
			if(checked) {
				selected = checked;
				break;
			}
		}

		cache.setVisible(false);
		delete.setVisible(true);
		delete.setEnabled(selected);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = super.onOptionsItemSelected(item);
		if(!ret) {
			switch(item.getItemId()) {
			case MENU_DELETE:
				for(Map.Entry<Integer, Boolean> entry : checkedElems.entrySet()) {
					if(entry.getValue()) {
						// the element must be deleted from the DB

						// TODO implement deletion
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		for(Integer id : checkedElems.keySet()) {
			checkedElems.put(id, isChecked);
		}
		for(int i = 0, l = programsList.getChildCount(); i < l; i++) {
			CheckBox checkbox = (CheckBox) programsList.getChildAt(i).findViewById(R.id.analyze_db_elem_select);
			checkbox.setChecked(isChecked);
		}
	}

	public void setElementChecked(Integer id, Boolean checked) {
		checkedElems.put(id, checked);
	}

	public boolean isElementChecked(Integer id) {
		Boolean ret = checkedElems.get(id);
		return (ret != null && ret);
	}

}
