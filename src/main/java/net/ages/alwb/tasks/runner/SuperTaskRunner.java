package net.ages.alwb.tasks.runner;

import com.google.gson.Gson;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class SuperTaskRunner {
	
	private InternalDbManager internalManager;
	private ExternalDbManager externalManager;

	private String uid = "";
	private String pwd = "";
	private String dbHost = "localhost";
	private Gson gson = new Gson();
	private boolean simulate = false;
	private boolean disableSynch = false;

	public SuperTaskRunner(
			String uid
			, String pwd
			, String dbHost
			, boolean disableSynch
			, boolean simulate
			) {
		this.uid = uid;
		this.pwd = pwd;
		this.dbHost = dbHost;
		this.disableSynch = disableSynch;
		this.simulate = simulate;
		
		if (this.simulate) {
			System.out.println("Simulating DB updates");
		} else {
			internalManager = new InternalDbManager(
					"datastore/ioc-liturgical-db"  // store name
					, "json" // tablename
					, false // delete old database
					, false // truncate tables (if you don't delete the old db)
					, false // create test users
					, this.uid // the username
					, this.pwd // the password
					);

			externalManager = new ExternalDbManager(
					this.dbHost
					, ""
					, this.disableSynch
					, this.disableSynch
					, uid
					, pwd
					, false // do not build domainMap
					, false // not read only
					, this.internalManager
					);
			if (this.disableSynch) {
				ExternalDbManager.neo4jManager.setSynchOn(false);
			}
		}
	}
	
	public void run() {
		
	}

	public InternalDbManager getInternalManager() {
		return internalManager;
	}

	public void setInternalManager(InternalDbManager internalManager) {
		this.internalManager = internalManager;
	}

	public ExternalDbManager getExternalManager() {
		return externalManager;
	}

	public void setExternalManager(ExternalDbManager externalManager) {
		this.externalManager = externalManager;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public boolean isSimulate() {
		return simulate;
	}

	public void setSimulate(boolean simulate) {
		this.simulate = simulate;
	}

	public boolean isDisableSynch() {
		return disableSynch;
	}

	public void setDisableSynch(boolean disableSynch) {
		this.disableSynch = disableSynch;
	}
}
