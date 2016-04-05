//
// preSubmit
//
function preSubmit() {

	var taskNumber = document.getElementById(taskNumberId);
	var jobIdentifier = document.getElementById(jobLabelId);
	var executable = "";
	var executables = new Array();
	var arguments = new Array();
	var finalJobExecutalble = "";
	var finalJobArgument = "";

	var state_executable = false;
	var state_argument = false;
	var state_executables = false;
	var state_taskNumber = false;
	var state_jobIdentifier = false;
	var state_finalJobExecutable = false;

	var index_missingExecutable = -1;
	var index_missingArgument = -1;

	if (taskNumber.value == "") {
		state_taskNumber = true;
	} else {
		var selectedCollectionType = document
				.getElementById(collectionTypeId);

		switch (selectedCollectionType.value) {
		case "JOB_COLLECTION":
			for (var i = 0; i < taskNumber.value; i++) {
				var tmp = document.getElementById('executables' + i);
				if (tmp.value == "") {
					index_missingExecutable = i;
					break;
				} else {
					var argument = document.getElementById('argument' + i);
					arguments[i] = argument.value;
					executables[i] = tmp.value;
				}
			}
			document.getElementById(executablesId).value = executables.join(";");
			document.getElementById(argumentsId).value = arguments.join(";");
			break;
		case "WORKFLOW_N1":
			for (var i = 0; i < taskNumber.value; i++) {
				var tmp = document.getElementById('executables' + i);
				if (tmp.value == "") {
					index_missingExecutable = i;
					break;
				} else {
					var argument = document.getElementById('argument' + i);
					arguments[i] = argument.value;
					executables[i] = tmp.value;
				}
			}

			document.getElementById(executablesId).value = executables.join(";");
			document.getElementById(argumentsId).value = arguments.join(";");

			finalJobArgument = document.getElementById('final_argumentId').value;
			finalJobExecutalble = document.getElementById('final_executableId').value;

			if (finalJobExecutalble == "")
				state_finalJobExecutable = true;
			break;
		case "JOB_PARAMETRIC":
			executable = document.getElementById('executable').value;
			if (executable == "") {
				state_executable = true;
			} else {
				for (var i = 0; i < taskNumber.value; i++) {

					var argument = document.getElementById('argument' + i);
					if (argument.value != "") {
						arguments[i] = argument.value;
					} else {
						index_missingArgument = i;
					}
					document.getElementById(argumentsId).value = arguments.join(";");
				}
			}

			if (index_missingArgument != -1) {
				state_argument = true;
			}
			break;
		}

		if (index_missingExecutable != -1) {
			state_executables = true;
		}
	}

	if (jobIdentifier.value == "")
		state_jobIdentifier = true;

	var missingFields = "";
	if (state_taskNumber) {
		missingFields += "  Task Number\n";
	}
	if (state_executable) {
		missingFields += "  Missing Executable\n";
	}
	if (state_executables) {
		missingFields += "  Executable " + index_missingExecutable + "\n";
	}
	if (state_argument) {
		missingFields += "  Missing Argument " + index_missingArgument + "\n";
	}
	if (state_jobIdentifier) {
		missingFields += "  Collection identifier\n";
	}
	if (state_finalJobExecutable) {
		missingFields += "  Final Job Executable\n"
	}
	if (missingFields == "") {
		var stringa = "";
		stringa += "\nTask Number: " + taskNumber.value + "\n";
		for (var i = 0; i < taskNumber.value; i++) {
			if (document.getElementById(collectionTypeId).value != "JOB_PARAMETRIC") {
				stringa += "Executables[" + i + "]: " + executables[i].value
						+ "\n";
			} else {
				stringa += "Executable: " + executable + "\n";
			}
			stringa += "Arguments[" + i + "]: " + arguments[i] + "\n";
		}
		stringa += "FinalJobExecutable: " + finalJobExecutalble + "\n";
		stringa += "FinalJobArgument: " + finalJobArgument + "\n";
		stringa += "Collection Identifier: " + jobIdentifier.value;

		document.forms[0].submit();
	} else {
		alert("You cannot send an inconsistent "
				+ document.getElementById(collectionTypeId).options[document
						.getElementById(collectionTypeId).selectedIndex].text
				+ "!\nMissing fields:\n" + missingFields);
	}
}

//
// addDemo
//
// This function is responsible to fill form with demo values
function addDemo() {
	var currentTime = new Date();
	var taskNumber = document.getElementById(taskNumberId);
	taskNumber.value = 3;
	var jobIdentifier = document.getElementById(jobLabelId);
	var selectedCollectionType = document.getElementById(collectionTypeId);

	updatePage();
	var executables = new Array();
	for (var i = 0; i < taskNumber.value; i++) {
		executables[i] = document.getElementById('executables' + i);
	}

	switch (selectedCollectionType.value) {
	case "JOB_COLLECTION":
		executables[0].value = 'hostname';
		executables[1].value = 'ls';
		executables[2].value = 'pwd'
		jobIdentifier.value = "Demo Collection: ";
		break;
	case "WORKFLOW_N1":
		executables[0].value = 'hostname';
		executables[1].value = 'ls';
		executables[2].value = 'pwd'
		document.getElementById('final_executableId').value = "ls";
		document.getElementById('final_argumentId').value = "-l";
		jobIdentifier.value = "Demo Workflow N1: ";
		break;
	case "JOB_PARAMETRIC":
		var executable = document.getElementById('executable');
		executable.value = "echo";

		var arguments = new Array();

		for (var i = 0; i < taskNumber.value; i++) {
			arguments[i] = document.getElementById('argument' + i).value = "Job "
					+ i;
		}

		jobIdentifier.value = "Demo Parametric Job: ";
		break;
	}
	jobIdentifier.value += currentTime.getDate() + "/"
			+ (currentTime.getMonth() + 1) + "/" + currentTime.getFullYear()
			+ " - " + currentTime.getHours() + ":" + currentTime.getMinutes()
			+ ":" + currentTime.getSeconds();
}

var control = true;

function updatePage() {
	cancel();
	console.log("updatePage()");
	var tablecontents = "";
	var numInput = document.getElementById(taskNumberId).value;

	var selectedCollectionType = document.getElementById(collectionTypeId);

	tablecontents = "<table>";
	tablecontents += "<tr>";
	tablecontents += "<th><h3>Sub Jobs</h3></th>";
	tablecontents += "</tr>";
	tablecontents += "<tr>";
	tablecontents += "<th>Executables</th><th>Arguments</th>";
	tablecontents += "</tr>";

	switch (selectedCollectionType.value) {
	case "JOB_COLLECTION":

		for (var i = 0; i < numInput; i++) {
			tablecontents += "<tr>";
			tablecontents += "<td><input type='text' name='executables' id='executables"
					+ i + "'></input> </td>";
			tablecontents += "<td><input type='text' name='argument' id='argument"
					+ i + "'> </input></td>";
			tablecontents += "</tr>";
		}

		break;

	case "WORKFLOW_N1":

		for (var i = 0; i < numInput; i++) {
			tablecontents += "<tr>";
			tablecontents += "<td><input type='text' name='executables' id='executables"
					+ i + "'></input> </td>";
			tablecontents += "<td><input type='text' name='argument' id='argument"
					+ i + "'> </input></td>";
			tablecontents += "</tr>";
		}

		tablecontents += "<tr>";
		tablecontents += "<th><h3>Final Job</h3></th>";
		tablecontents += "</tr>";

		tablecontents += "<tr>";
		tablecontents += "<th>Final Executable</th><th>Argument</th>";
		tablecontents += "</tr>";

		tablecontents += "<tr>";
		tablecontents += "<td><input type='text' name='final_executable' id='final_executableId'></input> </td>";
		tablecontents += "<td><input type='text' name='final_argument' id='final_argumentId'> </input></td>";
		tablecontents += "</tr>";
		break;
	case "JOB_PARAMETRIC":
		tablecontents += "<tr><td rowspan="
				+ numInput
				+ " ><input type='text' name='parametric_executable' id='executable'></input></td>";
		tablecontents += "<td><input type='text' name='argument' id='argument"
				+ 0 + "'> </input></td>";
		tablecontents += "</tr>";
		for (var i = 1; i < numInput; i++) {
			tablecontents += "<tr>";
			tablecontents += "<td><input type='text' name='argument' id='argument"
					+ i + "'> </input></td>";
			tablecontents += "</tr>";
		}
		break;

	}

	tablecontents += "</table>";
	document.getElementById('container').innerHTML += tablecontents;

	control = false;
}

//
// resetForm
//
// This function is responsible to enable all textareas
// when the user press the 'reset' form button
function resetForm() {
	document.getElementById(collectionTypeId).selectdindex = 0;
	document.getElementById(taskNumberId).value = "";
	cancel();
	document.getElementById(jobLabelId).value = "multi-infrastructure-collection job description";
	// if(document.getElementById('task').style.visibility == 'hidden')
	// document.getElementById('task').style.visibility = 'visible';
}

function cancel() {
	if (control == false) {
		var node = document.getElementById('container');
		while (node.firstChild) {
			node.removeChild(node.firstChild);
		}
	}
}