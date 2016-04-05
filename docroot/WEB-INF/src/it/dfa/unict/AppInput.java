package it.dfa.unict;

import java.util.List;

/**
 *
 * @author mario
 */
public class AppInput {
	// Application inputs

	private String inputFileName; // Filename for application input file
	private String jobLabel; // User' given job identifier

	// Each inputSandobox file must be declared below
	// This variable contains the content of an uploaded file
	private String inputSandbox;

	// Some user level information
	// must be stored as well
	private String username;
	private String timestamp;
	private String collectionType;
	private int taskNumber;
	private String executable;
	private List<String> executables;
	private List<String> arguments;
	private String userEmail;
	private String finalJobExecutable;
	private String finalJobArgument;

	/**
	 * Standard constructor just initialize empty values
	 */
	public AppInput() {
		this.inputFileName = "";
		this.jobLabel = "";
		this.inputSandbox = "";
		this.username = "";
		this.timestamp = "";
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public String getJobLabel() {
		return jobLabel;
	}

	public void setJobLabel(String joblabel) {
		this.jobLabel = joblabel;
	}

	public String getInputSandbox() {
		return inputSandbox;
	}

	public void setInputSandbox(String inputFile) {
		this.inputSandbox += (this.inputSandbox.equals("") ? "" : ",")
				+ inputFile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public int getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getExecutable() {
		return executable;
	}

	public void setExecutable(String executable) {
		this.executable = executable;
	}

	public List<String> getExecutables() {
		return executables;
	}

	public void setExecutables(List<String> list) {
		this.executables = list;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> list) {
		this.arguments = list;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getFinalJobExecutable() {
		return finalJobExecutable;
	}

	public void setFinalJobExecutable(String finalJobExecutable) {
		this.finalJobExecutable = finalJobExecutable;
	}

	public String getFinalJobArgument() {
		return finalJobArgument;
	}

	public void setFinalJobArgument(String finalJobArgument) {
		this.finalJobArgument = finalJobArgument;
	}

	@Override
	public String toString() {
		return "AppInput [inputFileName=" + inputFileName + ", jobLabel="
				+ jobLabel + ", inputSandbox=" + inputSandbox + ", username="
				+ username + ", timestamp=" + timestamp + ", collectionType="
				+ collectionType + ", taskNumber=" + taskNumber
				+ ", executable=" + executable + ", executables=" + executables
				+ ", arguments=" + arguments + ", userEmail=" + userEmail
				+ ", finalJobExecutable=" + finalJobExecutable
				+ ", finalJobArgument=" + finalJobArgument + "]";
	}

}
