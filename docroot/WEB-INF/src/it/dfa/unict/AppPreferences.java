package it.dfa.unict;

import it.dfa.unict.util.Constants;

public class AppPreferences {
	private boolean productionEnviroment;
	private String gridOperationDesc = "";
	private String portletVersion = "";
	private String gridOperationId = "-1";
	private String numInfrastructures = "";
	private String sciGwyUserTrackingDB_Hostname = "";
	private String sciGwyUserTrackingDB_Username = "";
	private String sciGwyUserTrackingDB_Password = "";
	private String sciGwyUserTrackingDB_Database = "";
	private String jobRequirements = "";
	private String pilotScript = "";

	public boolean isProductionEnviroment() {
		return productionEnviroment;
	}

	public void setProductionEnviroment(boolean productionEnviroment) {
		this.productionEnviroment = productionEnviroment;
	}

	public String getGridOperationDesc() {
		return gridOperationDesc;
	}

	public void setGridOperationDesc(String gridOperationDesc) {
		this.gridOperationDesc = gridOperationDesc;
	}

	public String getPortletVersion() {
		return portletVersion;
	}

	public void setPortletVersion(String portletVersion) {
		this.portletVersion = portletVersion;
	}

	public String getGridOperationId() {
		return gridOperationId;
	}

	public void setGridOperationId(String gridOperationId) {
		this.gridOperationId = gridOperationId;
	}

	public String getNumInfrastructures() {
		return numInfrastructures;
	}

	public void setNumInfrastructures(String numInfrastructures) {
		this.numInfrastructures = numInfrastructures;
	}

	public String getSciGwyUserTrackingDB_Hostname() {
		return sciGwyUserTrackingDB_Hostname;
	}

	public void setSciGwyUserTrackingDB_Hostname(
			String sciGwyUserTrackingDB_Hostname) {
		this.sciGwyUserTrackingDB_Hostname = sciGwyUserTrackingDB_Hostname;
	}

	public String getSciGwyUserTrackingDB_Username() {
		return sciGwyUserTrackingDB_Username;
	}

	public void setSciGwyUserTrackingDB_Username(
			String sciGwyUserTrackingDB_Username) {
		this.sciGwyUserTrackingDB_Username = sciGwyUserTrackingDB_Username;
	}

	public String getSciGwyUserTrackingDB_Password() {
		return sciGwyUserTrackingDB_Password;
	}

	public void setSciGwyUserTrackingDB_Password(
			String sciGwyUserTrackingDB_Password) {
		this.sciGwyUserTrackingDB_Password = sciGwyUserTrackingDB_Password;
	}

	public String getSciGwyUserTrackingDB_Database() {
		return sciGwyUserTrackingDB_Database;
	}

	public void setSciGwyUserTrackingDB_Database(
			String sciGwyUserTrackingDB_Database) {
		this.sciGwyUserTrackingDB_Database = sciGwyUserTrackingDB_Database;
	}

	public String getJobRequirements() {
		return jobRequirements;
	}

	public void setJobRequirements(String jobRequirements) {
		this.jobRequirements = jobRequirements;
	}

	public String getPilotScript() {
		return pilotScript;
	}

	public void setPilotScript(String pilotScript) {
		this.pilotScript = pilotScript;
	}

	/**
	 * Returns a text string containing the AppPreferences values dump
	 * 
	 * @return String with HTML statements inside a 'table' block
	 * 
	 * @see AppPreferences
	 */
	public String dump() {
		String dump = Constants.NEW_LINE + "Preference values:"
				+ Constants.NEW_LINE + "-----------------" + Constants.NEW_LINE
				+ "gridOperationId              : '" + gridOperationId + "'"
				+ Constants.NEW_LINE + "gridOperationDesc            : '"
				+ gridOperationDesc + "'" + Constants.NEW_LINE
				+ "numInfrastructures           : '" + numInfrastructures + "'"
				+ Constants.NEW_LINE + "jobRequirements              : '"
				+ jobRequirements + "'" + Constants.NEW_LINE
				+ "pilotScript                  : '" + pilotScript + "'"
				+ Constants.NEW_LINE + "sciGwyUserTrackingDB_Hostname: '"
				+ sciGwyUserTrackingDB_Hostname + "'" + Constants.NEW_LINE
				+ "sciGwyUserTrackingDB_Username: '"
				+ sciGwyUserTrackingDB_Username + "'" + Constants.NEW_LINE
				+ "sciGwyUserTrackingDB_Password: '"
				+ sciGwyUserTrackingDB_Password + "'" + Constants.NEW_LINE
				+ "sciGwyUserTrackingDB_Database: '"
				+ sciGwyUserTrackingDB_Database + "'" + Constants.NEW_LINE;
		return dump;
	} // dumpPreferences

}
