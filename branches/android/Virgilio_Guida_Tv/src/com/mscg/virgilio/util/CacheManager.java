package com.mscg.virgilio.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.database.SQLException;

import com.mscg.virgilio.database.ChannelsManagement;
import com.mscg.virgilio.database.ProgramsDB;
import com.mscg.virgilio.database.ProgramsManagement;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.Programs;

public class CacheManager implements Closeable, ProgramsManagement, ChannelsManagement {

	private static CacheManager instance;

	private ProgramsDB db;
	private LRUMap programsCache;
	private LRUMap channelsCache;

	public synchronized static CacheManager getInstance() {
		return instance;
	}

	public synchronized static void init(Context context) {
		instance = new CacheManager(context);
	}

	protected CacheManager(Context context) {
		db = new ProgramsDB(context, true);
		programsCache = new LRUMap(4);
		channelsCache = new LRUMap(80);
	}

	@Override
	public synchronized void close() throws IOException {
		IOUtils.closeQuietly(db);
		db = null;
		programsCache.clear();
		programsCache = null;
		channelsCache.clear();
		channelsCache = null;
		instance = null;
	}

	@Override
	public synchronized boolean containsProgramsForDay(Date day) throws SQLException {
		// search in cache
		boolean ret = programsCache.containsKey(Util.adaptDate(day));
		if(!ret) {
			// search in the DB
			ret = db.containsProgramsForDay(day);
		}
		return ret;
	}

	@Override
	public synchronized Programs getProgramsForDay(Date day) throws SQLException {
		// search in cache
		Programs ret = (Programs)programsCache.get(Util.adaptDate(day));
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
		programsCache.put(Util.adaptDate(programs.getDate()), programs);
	}

	@Override
	public synchronized Channel getChannel(long programsID, long channelID) {
		return getChannel(programsID, channelID, false);
	}

	@Override
	public Channel getChannel(long programsID, long channelID, boolean removeElement) {
		String key = Long.toString(programsID) + channelID;
		Channel ret = null;
		if(channelsCache.containsKey(key)) {
			ret = (Channel)channelsCache.get(key);
			if(removeElement)
				removeChannel(programsID, channelID);
		}
		return ret;
	}

	@Override
	public synchronized void saveChannel(long programsID, long channelID, Channel channel) {
		channelsCache.put(Long.toString(programsID) + channelID, channel);
	}

	@Override
	public synchronized boolean removeChannel(long programsID, long channelID) {
		String key = Long.toString(programsID) + channelID;
		return (channelsCache.remove(key) != null);
	}

}
