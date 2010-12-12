package com.mscg.virgilio.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.database.SQLException;

import com.mscg.virgilio.database.ProgramsDB;
import com.mscg.virgilio.database.ProgramsManagement;
import com.mscg.virgilio.programs.Programs;

public class CacheManager implements Closeable, ProgramsManagement {

	private static CacheManager instance;

	private ProgramsDB db;
	private LRUMap cache;

	public synchronized static CacheManager getInstance() {
		return instance;
	}

	public synchronized static void init(Context context) {
		instance = new CacheManager(context);
	}

	protected CacheManager(Context context) {
		db = new ProgramsDB(context, true);
		cache = new LRUMap(4);
	}

	@Override
	public synchronized void close() throws IOException {
		IOUtils.closeQuietly(db);
		db = null;
		cache.clear();
		cache = null;
		instance = null;
	}

	@Override
	public synchronized boolean containsProgramsForDay(Date day) throws SQLException {
		// search in cache
		boolean ret = cache.containsKey(Util.adaptDate(day));
		if(!ret) {
			// search in the DB
			ret = db.containsProgramsForDay(day);
		}
		return ret;
	}

	@Override
	public synchronized Programs getProgramsForDay(Date day) throws SQLException {
		// search in cache
		Programs ret = (Programs)cache.get(Util.adaptDate(day));
		if(ret == null) {
			// search in the DB
			ret = db.getProgramsForDay(day);
			saveProgramsInCache(ret);
		}
		return ret;
	}

	@Override
	public synchronized Programs savePrograms(Programs programs) throws SQLException {
		Programs ret = db.savePrograms(programs);
		saveProgramsInCache(ret);
		return ret;
	}

	protected void saveProgramsInCache(Programs programs) {
		cache.put(Util.adaptDate(programs.getDate()), programs);
	}

}
