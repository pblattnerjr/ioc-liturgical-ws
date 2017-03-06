package net.ages.alwb.utils.core.datastores.db.h2.utils;


import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;


public class RunToListAllTableNames {

	public static void main(String[] args) {

		boolean delete = false;

    	H2ConnectionManager manager = new H2ConnectionManager(delete);
        	System.out.println("List of all User Tables...");
        	for (String table : manager.getTableNames()) {
        		System.out.println(table);
        	}
   }

}
