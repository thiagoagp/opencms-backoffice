package com.mscg.virgilio.database;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.Util;

public class ProgramsDB implements Closeable, ProgramsManagement {

	public static class CHANNEL_CONSTS {
		public static final String TABLE_NAME = "channel";

		public static final String ID_COL = "id";
		public static final int ID_COL_INDEX = 0;

		public static final String STR_ID_COL = "str_id";
		public static final int STR_ID_COL_INDEX = ID_COL_INDEX + 1;

		public static final String NAME_COL = "name";
		public static final int NAME_COL_INDEX = ID_COL_INDEX + 2;

		public static final String TYPES_COL = "types";
		public static final int TYPES_COL_INDEX = ID_COL_INDEX + 3;

		public static final int COLUMN_COUNT = 4;

		public static final String CREATE_QUERY =
			"CREATE TABLE " + TABLE_NAME + "(" +
				ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				STR_ID_COL + " TEXT NOT NULL, " +
				NAME_COL + " TEXT NOT NULL, " +
				TYPES_COL + " TEXT " +
			")";
		public static final String DROP_QUERY =
			"DROP TABLE " + TABLE_NAME;
	}
	public static class CHANNELS_PROGRAMS_CONSTS {
		public static final String TABLE_NAME = "channel_to_programs";

		public static final String PROGRAMS_COL = "programs_id";
		public static final int PROGRAMS_COL_INDEX = 0;

		public static final String CHANNEL_COL = "channel_id";
		public static final int CHANNEL_COL_INDEX = PROGRAMS_COL_INDEX + 1;

		public static final int COLUMN_COUNT = 2;

		public static final String CREATE_QUERY =
			"CREATE TABLE " + TABLE_NAME + "(" +
				PROGRAMS_COL + " INTEGER NOT NULL, " +
				CHANNEL_COL + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + PROGRAMS_COL + ") REFERENCES " + PROGRAMS_CONSTS.TABLE_NAME + "(" + PROGRAMS_CONSTS.ID_COL + ") " +
					"ON DELETE CASCADE ON UPDATE CASCADE, " +
				"FOREIGN KEY (" + CHANNEL_COL + ") REFERENCES " + CHANNEL_CONSTS.TABLE_NAME + "(" + CHANNEL_CONSTS.ID_COL + ") " +
					"ON DELETE CASCADE ON UPDATE CASCADE" +
			")";
		public static final String DROP_QUERY =
			"DROP TABLE " + TABLE_NAME;
	}

	public static class PROGRAMS_CONSTS {
		public static final String TABLE_NAME = "programs";

		public static final String ID_COL = "id";
		public static final int ID_COL_INDEX = 0;

		public static final String DATE_COL = "date";
		public static final int DATE_COL_INDEX = ID_COL_INDEX + 1;

		public static final String LAST_UPDATE_COL = "lastupdate";
		public static final int LAST_UPDATE_COL_INDEX = ID_COL_INDEX + 2;

		public static final int COLUMN_COUNT = 3;

		public static final String CREATE_QUERY =
			"CREATE TABLE " + TABLE_NAME + "(" +
				ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				DATE_COL + " LONG, " +
				LAST_UPDATE_COL + " LONG" +
			")";
		public static final String DROP_QUERY =
			"DROP TABLE " + TABLE_NAME;
	}
	private static class ProgramsDBHelper extends SQLiteOpenHelper {

		public ProgramsDBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.beginTransaction();
			try {
				db.execSQL(PROGRAMS_CONSTS.CREATE_QUERY);
				db.execSQL(CHANNEL_CONSTS.CREATE_QUERY);
				db.execSQL(CHANNELS_PROGRAMS_CONSTS.CREATE_QUERY);
				db.execSQL(TV_PROGRAM_CONSTS.CREATE_QUERY);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(ProgramsDBHelper.class.getCanonicalName(),
				"Upgrading DB from version " + oldVersion + " to version " + newVersion + ". " +
				"Old data will be lost.");

			db.beginTransaction();
			try {
				db.execSQL(TV_PROGRAM_CONSTS.DROP_QUERY);
				db.execSQL(CHANNELS_PROGRAMS_CONSTS.DROP_QUERY);
				db.execSQL(CHANNEL_CONSTS.DROP_QUERY);
				db.execSQL(PROGRAMS_CONSTS.DROP_QUERY);

				db.setTransactionSuccessful();
			} catch(Exception e) {
				db.endTransaction();
			}

			onCreate(db);
		}

	}
	public static class TV_PROGRAM_CONSTS {
		public static final String TABLE_NAME = "tv_program";

		public static final String ID_COL = "id";
		public static final int ID_COL_INDEX = 0;

		public static final String STR_ID_COL = "str_id";
		public static final int STR_ID_COL_INDEX = ID_COL_INDEX + 1;

		public static final String NAME_COL = "name";
		public static final int NAME_COL_INDEX = ID_COL_INDEX + 2;

		public static final String STARTTIME_COL = "start_time";
		public static final int STARTTIME_COL_INDEX = ID_COL_INDEX + 3;

		public static final String ENDTIME_COL = "end_time";
		public static final int ENDTIME_COL_INDEX = ID_COL_INDEX + 4;

		public static final String CATEGORY_COL = "category";
		public static final int CATEGORY_COL_INDEX = ID_COL_INDEX + 5;

		public static final String CHANNEL_COL = "channel_id";
		public static final int CHANNEL_COL_INDEX = ID_COL_INDEX + 6;

		public static final int COLUMN_COUNT = 7;

		public static final String CREATE_QUERY =
			"CREATE TABLE " + TABLE_NAME + "(" +
				ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				STR_ID_COL + " TEXT NOT NULL, " +
				NAME_COL + " TEXT NOT NULL, " +
				STARTTIME_COL + " LONG, " +
				ENDTIME_COL + " LONG, " +
				CATEGORY_COL + " TEXT, " +
				CHANNEL_COL + " INTEGER NOT NULL, " +
				"FOREIGN KEY (" + CHANNEL_COL + ") REFERENCES " + CHANNEL_CONSTS.TABLE_NAME + "(" + CHANNEL_CONSTS.ID_COL + ") " +
					"ON DELETE CASCADE ON UPDATE CASCADE" +
			")";
		public static final String DROP_QUERY =
			"DROP TABLE " + TABLE_NAME;
	}

	private static final String DATABASE_NAME = "tv_programs.db";;

	private static final int DATABASE_VERSION = 1;

	private final Context context;

	private SQLiteDatabase db;

	private ProgramsDBHelper dbHelper;

	public ProgramsDB(Context context) {
		this(context, false);
	}

	public ProgramsDB(Context context, boolean autoOpen) {
		this.context = context;
		dbHelper = new ProgramsDBHelper(this.context, DATABASE_NAME, null, DATABASE_VERSION);
		if(autoOpen)
			open();
	}

	@Override
	public void close() throws IOException {
		if(!isClosed()) {
			db.close();
			db = null;
		}
	}

	@Override
	public boolean containsProgramsForDay(Date day) throws SQLException {
		boolean ret = false;
		Cursor tmp = null;
		try {
			tmp = getProgramsCursorForDay(day);
			ret = tmp.getCount() > 0 && tmp.moveToFirst();
		} finally {
			try {
				tmp.close();
			} catch(Exception e){}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.mscg.virgilio.database.ProgramsManagement#getChannelByStringID(java.lang.String)
	 */
	public Cursor getChannelByStringID(String id) throws SQLException {
		if(db == null)
			throw new SQLException("The DB is not opened.");

		Cursor ret = null;
		ret = db.query(
			CHANNEL_CONSTS.TABLE_NAME,
			null,
			CHANNEL_CONSTS.STR_ID_COL + " = ?",
			new String[]{id},
			null, null, null);

		return ret;
	}

	/* (non-Javadoc)
	 * @see com.mscg.virgilio.database.ProgramsManagement#getProgramsCursorForDay(java.util.Date)
	 */
	public Cursor getProgramsCursorForDay(Date day) throws SQLException {
		if(db == null)
			throw new SQLException("The DB is not opened.");

		Date cal = Util.adaptDate(day);

		Cursor programCur = db.query(
				PROGRAMS_CONSTS.TABLE_NAME,
				null,
				PROGRAMS_CONSTS.DATE_COL + " = ?",
				new String[]{Long.toString(cal.getTime())},
				null, null, null);

		return programCur;
	}

	/* (non-Javadoc)
	 * @see com.mscg.virgilio.database.ProgramsManagement#getProgramsForDay(java.util.Date)
	 */
	public Programs getProgramsForDay(Date day) throws SQLException {
		Programs ret = null;
		Cursor programCur = getProgramsCursorForDay(day);

		Cursor listCur = null;

		try {
			if(programCur.getCount() != 0 && programCur.moveToFirst()) {
				ret = new Programs(
					programCur.getLong(programCur.getColumnIndexOrThrow(PROGRAMS_CONSTS.ID_COL)));
				ret.setDate(new Date(
					programCur.getLong(programCur.getColumnIndexOrThrow(PROGRAMS_CONSTS.DATE_COL))));
				ret.setLastUpdate(new Date(
					programCur.getLong(programCur.getColumnIndexOrThrow(PROGRAMS_CONSTS.LAST_UPDATE_COL))));

				listCur = db.rawQuery(
					"SELECT c.*, t.* " +
					"FROM " + CHANNELS_PROGRAMS_CONSTS.TABLE_NAME + " AS cp, " +
						CHANNEL_CONSTS.TABLE_NAME + " AS c, " + TV_PROGRAM_CONSTS.TABLE_NAME + " AS t " +
					"WHERE cp." + CHANNELS_PROGRAMS_CONSTS.PROGRAMS_COL + " = ? " +
					"  AND cp." + CHANNELS_PROGRAMS_CONSTS.CHANNEL_COL + " = c." + CHANNEL_CONSTS.ID_COL + " " +
					"  AND t." + TV_PROGRAM_CONSTS.CHANNEL_COL + " = c." + CHANNEL_CONSTS.ID_COL,
					new String[]{"" + ret.getId()});

				Channel curChann = null;
				if(listCur.getCount() != 0 && listCur.moveToFirst()) {
					do {
						long channelID = listCur.getLong(CHANNEL_CONSTS.ID_COL_INDEX);
						if(curChann == null || curChann.getId() != channelID) {
							curChann = new Channel(channelID,
								listCur.getString(CHANNEL_CONSTS.STR_ID_COL_INDEX),
								listCur.getString(CHANNEL_CONSTS.NAME_COL_INDEX));
							String types = listCur.getString(CHANNEL_CONSTS.TYPES_COL_INDEX);
							curChann.setTypes(Arrays.asList(types.split("\\|")));
							ret.addChannel(curChann);
						}
						TVProgram program = new TVProgram(
							listCur.getLong(CHANNEL_CONSTS.COLUMN_COUNT + TV_PROGRAM_CONSTS.ID_COL_INDEX),
							listCur.getString(CHANNEL_CONSTS.COLUMN_COUNT + TV_PROGRAM_CONSTS.STR_ID_COL_INDEX),
							listCur.getString(CHANNEL_CONSTS.COLUMN_COUNT + TV_PROGRAM_CONSTS.NAME_COL_INDEX),
							new Date(listCur.getLong(CHANNEL_CONSTS.COLUMN_COUNT + TV_PROGRAM_CONSTS.STARTTIME_COL_INDEX)),
							new Date(listCur.getLong(CHANNEL_CONSTS.COLUMN_COUNT + TV_PROGRAM_CONSTS.ENDTIME_COL_INDEX)),
							listCur.getString(CHANNEL_CONSTS.COLUMN_COUNT + TV_PROGRAM_CONSTS.CATEGORY_COL_INDEX));
						curChann.addTVProgram(program);
					} while(listCur.moveToNext());
				}
			}
		} finally {
			try {
				programCur.close();
			} catch(Exception e){ }

			try {
				listCur.close();
			} catch(Exception e){ }
		}

		return ret;
	}

	public boolean isClosed() {
		return db == null;
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch(SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
	}

	/* (non-Javadoc)
	 * @see com.mscg.virgilio.database.ProgramsManagement#savePrograms(com.mscg.virgilio.programs.Programs)
	 */
	public Programs savePrograms(Programs programs) throws SQLException {
		if(db == null)
			throw new SQLException("The DB is not opened.");

		db.beginTransaction();
		try {
			// Save the programs information on the DB
			ContentValues progsValues = new ContentValues();
			progsValues.put(PROGRAMS_CONSTS.DATE_COL, programs.getDate().getTime());
			progsValues.put(PROGRAMS_CONSTS.LAST_UPDATE_COL, programs.getLastUpdate().getTime());
			long id = db.insert(PROGRAMS_CONSTS.TABLE_NAME, null, progsValues);
			if(id < 0)
				throw new SQLException("Cannot save programs in the DB.");
			programs.setId(id);

			for(Channel channel : programs.getChannels()) {
				Cursor channelCur = getChannelByStringID(channel.getStrId());
				long channelID = -1;
				try {
					if(channelCur.getCount() != 0 && channelCur.moveToFirst()) {
						// The channel already exists in the DB
						channelID = channelCur.getLong(channelCur.getColumnIndexOrThrow(CHANNEL_CONSTS.ID_COL));
					}
					else {
						// Save new channel info on DB
						ContentValues chanValues = new ContentValues();
						chanValues.put(CHANNEL_CONSTS.STR_ID_COL, channel.getStrId());
						chanValues.put(CHANNEL_CONSTS.NAME_COL, channel.getName());
						StringBuffer types = new StringBuffer();
						boolean first = true;
						for(String type : channel.getTypes()) {
							types.append((first ? "" : "|") + type.trim());
							first = false;
						}
						chanValues.put(CHANNEL_CONSTS.TYPES_COL, types.toString());
						channelID = db.insert(CHANNEL_CONSTS.TABLE_NAME, null, chanValues);
						if(channelID < 0)
							throw new SQLException("Cannot save channel informations in the DB.");
					}
				} finally {
					try {
						channelCur.close();
					} catch(Exception e){}
				}

				if(channel.getId() < 0) {
					channel.setId(channelID);

					// Join channel info and programs info
					ContentValues joinValues = new ContentValues();
					joinValues.put(CHANNELS_PROGRAMS_CONSTS.PROGRAMS_COL, programs.getId());
					joinValues.put(CHANNELS_PROGRAMS_CONSTS.CHANNEL_COL, channel.getId());
					db.insert(CHANNELS_PROGRAMS_CONSTS.TABLE_NAME, null, joinValues);
				}

				for(TVProgram tvprogram : channel.getTVPrograms()) {
					// Save program info on DB
					ContentValues progValues = new ContentValues();
					progValues.put(TV_PROGRAM_CONSTS.STR_ID_COL, tvprogram.getStrId());
					progValues.put(TV_PROGRAM_CONSTS.NAME_COL, tvprogram.getName());
					progValues.put(TV_PROGRAM_CONSTS.STARTTIME_COL, tvprogram.getStartTime().getTime());
					progValues.put(TV_PROGRAM_CONSTS.ENDTIME_COL, tvprogram.getEndTime().getTime());
					progValues.put(TV_PROGRAM_CONSTS.CATEGORY_COL, tvprogram.getCategory());
					progValues.put(TV_PROGRAM_CONSTS.CHANNEL_COL, channel.getId());

					long progID = db.insert(TV_PROGRAM_CONSTS.TABLE_NAME, null, progValues);
					if(progID < 0)
						throw new SQLException("Cannot save programs informations in the DB.");

					tvprogram.setId(progID);
				}
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return programs;
	}

}
