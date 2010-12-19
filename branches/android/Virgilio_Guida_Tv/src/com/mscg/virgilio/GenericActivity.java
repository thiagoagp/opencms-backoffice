package com.mscg.virgilio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class GenericActivity extends Activity {

	public static final String EXIT_PARAM = GenericActivity.class.getCanonicalName() + ".exit";

	public static final int MENU_GROUP = 0;
	public static final int MENU_CACHE = Menu.FIRST;
	public static final int MENU_EXIT = Menu.FIRST + 1;

	protected MenuItem cache;
	protected MenuItem exit;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		cache = menu.add(MENU_GROUP, MENU_CACHE, Menu.NONE, R.string.cache_settings);
		cache.setIcon(android.R.drawable.ic_menu_save);
		cache.setShortcut('0', 'c');
		exit = menu.add(MENU_GROUP, MENU_EXIT, Menu.NONE, R.string.exit);
		exit.setIcon(android.R.drawable.ic_lock_power_off);
		exit.setShortcut('1', 'e');

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		cache.setEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case MENU_CACHE:
			Intent cacheIntent = new Intent(this, VirgilioGuidaTvCacheManagement.class);
			startActivity(cacheIntent);
			return true;
		case MENU_EXIT:
			Intent closeIntent = getHomeIntent(this);
			closeIntent.putExtra(EXIT_PARAM, true);
			startActivity(closeIntent);
			return true;
		}
		return false;
	}

	public static Intent getHomeIntent(Context context) {
		Intent homeIntent = new Intent(context, VirgilioGuidaTvDaySelection.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return homeIntent;
	}

}
