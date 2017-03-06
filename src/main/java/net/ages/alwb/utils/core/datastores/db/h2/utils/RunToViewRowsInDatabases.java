package net.ages.alwb.utils.core.datastores.db.h2.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;

public class RunToViewRowsInDatabases {

	public static void main(String[] args) {

		boolean delete = false;
		String dbName = "ioc-liturgical-db";
		PreparedStatement statement;

    	H2ConnectionManager manager = new H2ConnectionManager(dbName,delete);
        try {
        	for (String table : manager.getTableNames()) {
            	System.out.println("List of Rows from " + table);
                String SelectQuery = "select * from " + table;
    			statement = manager.getConnection().prepareStatement(SelectQuery);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                	System.out.println(rs.getString("value"));
                }
                statement.close();
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }

}
