package net.ages.alwb.utils.core.datastores.db.h2.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;

public class RunToGetSpecificHistoryRecord {

	public static void main(String[] args) {
		boolean delete = false;
		PreparedStatement statement;

    	H2ConnectionManager manager = new H2ConnectionManager(delete);
        try {
        	// First RunToListAllTableNames to get the name of the table you want.
        	// Then set its value below.  Also, the row id that you want.
        	String table = "HTTP1270015986_GR_GR_MAC";
        	String id = "actors|Candidate_Sponsor";
            String SelectQuery = "SELECT * FROM " + table + " WHERE _id='" + id + "'";
			statement = manager.getConnection().prepareStatement(SelectQuery);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
            	System.out.println(rs.getString("value"));
            }
            statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
