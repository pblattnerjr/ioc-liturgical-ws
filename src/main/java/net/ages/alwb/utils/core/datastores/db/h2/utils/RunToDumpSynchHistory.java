package net.ages.alwb.utils.core.datastores.db.h2.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.db.factory.DbConnectionFactory;
import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;


public class RunToDumpSynchHistory {

	public static void main(String[] args) {

        try {
    		boolean delete = false;
    		PreparedStatement statement;

        	H2ConnectionManager manager =   DbConnectionFactory.getH2Manager(
    				Constants.APP_DATA_PATH + Constants.DB_NAME + ".json"
    				, "JSON_MANAGER"
    				, false
    				, false
    				);
        	String table = "JSON_MANAGER";// Constants.TABLE_SYNCH_HISTORY;
            	System.out.println("List of Rows from " + table);
                String SelectQuery = "select * from " + table;
//                String SelectQuery = "select * from " + table;
    			statement = manager.getConnection().prepareStatement(SelectQuery);
                ResultSet rs = statement.executeQuery();
                int count = 0;
                while (rs.next()) {
                	count = count + 1;
                	System.out.println(rs.getString("value"));
                }
                statement.close();
                System.out.println(count + " records.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}