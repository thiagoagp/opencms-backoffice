package com.mscg.virgilio.database;

import java.util.Date;

import android.database.SQLException;

import com.mscg.virgilio.programs.Programs;

public interface ProgramsManagement {

	public boolean containsProgramsForDay(Date day) throws SQLException;

	public Programs getProgramsForDay(Date day) throws SQLException;

	public Programs savePrograms(Programs programs) throws SQLException;

}