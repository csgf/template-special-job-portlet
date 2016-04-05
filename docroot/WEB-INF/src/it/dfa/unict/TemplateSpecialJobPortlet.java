package it.dfa.unict;

import it.dfa.unict.util.Constants;
import it.dfa.unict.util.Utils;
import it.infn.ct.GridEngine.Job.InfrastructureInfo;
import it.infn.ct.GridEngine.Job.MultiInfrastructureJobSubmission;
import it.infn.ct.GridEngine.JobCollection.JobCollection;
import it.infn.ct.GridEngine.JobCollection.JobCollectionSubmission;
import it.infn.ct.GridEngine.JobCollection.JobParametric;
import it.infn.ct.GridEngine.JobCollection.WorkflowN1;
import it.infn.ct.GridEngine.JobResubmission.GEJobDescription;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ProcessAction;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class TemplateSpecialJobPortlet extends MVCPortlet {

	private final Log _log = LogFactoryUtil
			.getLog(TemplateSpecialJobPortlet.class);

	private enum CollectionType {
		JOB_COLLECTION, WORKFLOW_N1, JOB_PARAMETRIC
	}

	public static String pilotScript;

	/**
	 * Initializes portlet's configuration with pilot-script file path.
	 * 
	 * @see com.liferay.util.bridges.mvc.MVCPortlet#init()
	 */
	@Override
	public void init() throws PortletException {
		super.init();
		pilotScript = getPortletContext().getRealPath(Constants.FILE_SEPARATOR)
				+ "WEB-INF/job/" + getInitParameter("pilot-script");
	}

	/**
	 * Processes the 'multipart/form-data' form uploading file, gets the jobs
	 * label finally submits job towards a list of enabled DCI specified in the
	 * configuration.
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 * @throws PortletException
	 */
	@ProcessAction(name = "submit")
	public void submit(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		AppInput appInput = new AppInput();
		PortletPreferences preferences = actionRequest.getPreferences();

		String JSONAppPrefs = GetterUtil.getString(preferences.getValue(
				Constants.APP_PREFERENCES, null));
		AppPreferences appPrefs = Utils.getAppPreferences(JSONAppPrefs);

		String JSONAppInfras = GetterUtil.getString(preferences.getValue(
				Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES, null));

		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TS_FORMAT);
		String timestamp = dateFormat.format(Calendar.getInstance().getTime());
		appInput.setTimestamp(timestamp);

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();

		String username = user.getScreenName();
		appInput.setUsername(username);

		appInput.setJobLabel(ParamUtil.getString(actionRequest, "jobLabel", ""));
		appInput.setCollectionType(ParamUtil.getString(actionRequest,
				"collection_type"));
		appInput.setTaskNumber(ParamUtil.getInteger(actionRequest,
				"task_number"));
		appInput.setExecutable(ParamUtil.getString(actionRequest,
				"parametric_executable"));
		String executables = ParamUtil.getString(actionRequest, "executables",
				null);
		if (executables != null && !executables.isEmpty()) {
			appInput.setExecutables(Arrays.asList(executables.split(";")));
		}
		String arguments = ParamUtil
				.getString(actionRequest, "arguments", null);
		if (arguments != null && !arguments.isEmpty()) {
			appInput.setArguments(Arrays.asList(arguments.split(";")));
		}
		appInput.setFinalJobExecutable(ParamUtil.getString(actionRequest,
				"final_executable"));
		appInput.setFinalJobArgument(ParamUtil.getString(actionRequest,
				"final_argument"));

		String joblabel = ParamUtil.getString(actionRequest, "jobLabel");
		appInput.setJobLabel(joblabel);

		_log.info(appInput);

		List<AppInfrastructureInfo> enabledInfras = Utils
				.getEnabledInfrastructureInfo(JSONAppInfras);

		if (enabledInfras.size() > 0) {
			InfrastructureInfo infrastructureInfo[] = Utils
					.convertAppInfrastructureInfo(enabledInfras);

			submitJobCollection(appPrefs, appInput, infrastructureInfo);

			PortalUtil.copyRequestParameters(actionRequest, actionResponse);
			actionResponse.setRenderParameter("jobLabel", joblabel);
			actionResponse.setRenderParameter("jspPage", "/jsps/submit.jsp");
		}
		// }

		// Hide default Liferay success/error messages
		PortletConfig portletConfig = (PortletConfig) actionRequest
				.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		LiferayPortletConfig liferayPortletConfig = (LiferayPortletConfig) portletConfig;
		SessionMessages.add(actionRequest, liferayPortletConfig.getPortletId()
				+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
	}

	private void submitJobCollection(AppPreferences preferences,
			AppInput appInput, InfrastructureInfo[] enabledInfrastructures) {

		ArrayList<GEJobDescription> descriptions = new ArrayList<GEJobDescription>();

		for (int i = 0; i < appInput.getTaskNumber(); i++) {
			GEJobDescription description = new GEJobDescription();
			switch (CollectionType.valueOf(appInput.getCollectionType())) {
			case JOB_COLLECTION:
			case WORKFLOW_N1:
				if (!appInput.getExecutables().get(i).contains("/bin/")) {
					String tmp = "/bin/" + appInput.getExecutables().get(i);
					tmp += appInput.getExecutables().set(i, tmp);
				}
				description.setExecutable(appInput.getExecutables().get(i));
				description.setInputFiles("");
				break;
			case JOB_PARAMETRIC:
				if (!appInput.getExecutable().contains("/bin/")) {
					String tmp = "/bin/";
					tmp += appInput.getExecutable();
					appInput.setExecutable(tmp);
				}
				description.setExecutable(appInput.getExecutable());
				description.setInputFiles(pilotScript);
				break;
			}
			if(appInput.getArguments()!=null && appInput.getArguments().size() > 0)
				description.setArguments(appInput.getArguments().get(i));
			description.setOutputPath("/tmp");
			description.setOutput("myOutput-" + i + ".txt");
			description.setError("myError-" + i + ".txt");
			descriptions.add(description);
		}

		JobCollection collection = null;

		switch (CollectionType.valueOf(appInput.getCollectionType())) {
		case JOB_COLLECTION:
			collection = new JobCollection(appInput.getUsername(),
					appInput.getJobLabel(), "/tmp", descriptions);
			break;
		case WORKFLOW_N1:
			GEJobDescription finalJobDescription = new GEJobDescription();

			if (!appInput.getFinalJobExecutable().contains("/bin/")) {
				appInput.setFinalJobExecutable("/bin/"
						+ appInput.getFinalJobExecutable());
			}

			finalJobDescription.setExecutable(appInput.getFinalJobExecutable());
			finalJobDescription.setArguments(appInput.getFinalJobArgument());

			String tmp = "";
			for (int i = 0; i < descriptions.size(); i++) {
				if (tmp.equals("")) {
					tmp = descriptions.get(i).getOutput();
				} else {
					tmp += "," + descriptions.get(i).getOutput();
				}
			}

			finalJobDescription.setInputFiles(tmp);
			finalJobDescription.setOutputPath("/tmp");
			finalJobDescription.setOutput("myOutput-FinalJob.txt");
			finalJobDescription.setError("myError-FinalJob.txt");

			collection = new WorkflowN1(appInput.getUsername(),
					appInput.getJobLabel(), "/tmp", descriptions,
					finalJobDescription);
			break;
		case JOB_PARAMETRIC:
			collection = new JobParametric(appInput.getUsername(),
					appInput.getJobLabel(), "/tmp", descriptions,
					appInput.getExecutable());
			break;
		}

		// GridEngine' JobCollectionSubmission job submission object
		JobCollectionSubmission tmpJobCollectionSubmission = null;

		if (!preferences.isProductionEnviroment()) {
			String DBNM = "jdbc:mysql://"
					+ preferences.getSciGwyUserTrackingDB_Hostname() + "/"
					+ preferences.getSciGwyUserTrackingDB_Database();
			String DBUS = preferences.getSciGwyUserTrackingDB_Username();
			String DBPW = preferences.getSciGwyUserTrackingDB_Password();
			tmpJobCollectionSubmission = new JobCollectionSubmission(DBNM,
					DBUS, DBPW, collection);
			_log.debug("MultiInfrastructureJobSubmission [DEVEL]\n"
					+ Constants.NEW_LINE + "    DBNM: '" + DBNM + "'"
					+ Constants.NEW_LINE + "    DBUS: '" + DBUS + "'"
					+ Constants.NEW_LINE + "    DBPW: '" + DBPW + "'");
		} else {
			tmpJobCollectionSubmission = new JobCollectionSubmission(collection);
			_log.debug("MultiInfrastructureJobSubmission [PROD]");
		}

		// GridOperations' Application Id
		int applicationId = Integer.parseInt(preferences.getGridOperationId());

		// Grid Engine' UserTraking needs the portal IP address
		String portalIPAddress = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			byte[] ipAddr = addr.getAddress();
			portalIPAddress = "" + (short) (ipAddr[0] & 0xff) + ":"
					+ (short) (ipAddr[1] & 0xff) + ":"
					+ (short) (ipAddr[2] & 0xff) + ":"
					+ (short) (ipAddr[3] & 0xff);
		} catch (Exception e) {
			_log.error("Unable to get the portal IP address");
		}

		// Ready now to submit the Job
		tmpJobCollectionSubmission.submitJobCollection(enabledInfrastructures,
				portalIPAddress, applicationId);

		// Show log
		// View jobSubmission details in the log
		_log.info(Constants.NEW_LINE + "Job Collection Sent"
				+ Constants.NEW_LINE + "-------" + Constants.NEW_LINE
				+ "Portal address: '" + portalIPAddress + "'"
				+ Constants.NEW_LINE); // _log.info
	}

	/**
	 * @param uploadRequest
	 * @param username
	 * @param timestamp
	 * @param appInput
	 * @return
	 * @throws IOException
	 */
	private File processInputFile(UploadPortletRequest uploadRequest,
			String username, String timestamp, AppInput appInput)
			throws IOException {

		File file = null;
		String fileInputName = "fileupload";

		String sourceFileName = uploadRequest.getFileName(fileInputName);

		if (Validator.isNotNull(sourceFileName)) {
			_log.debug("Uploading file: " + sourceFileName + " ...");

			String fileName = FileUtil.stripExtension(sourceFileName);
			_log.debug(fileName);

			appInput.setInputFileName(fileName);

			String extension = FileUtil.getExtension(sourceFileName);
			_log.debug(extension);

			// Get the uploaded file as a file.
			File uploadedFile = uploadRequest.getFile(fileInputName, true);
			File folder = new File(Constants.ROOT_FOLDER_NAME);
			// This is our final file path.
			file = new File(folder.getAbsolutePath() + Constants.FILE_SEPARATOR
					+ username + "_" + timestamp + "_" + fileName
					+ ((!extension.isEmpty()) ? "." + extension : ""));
			FileUtil.move(uploadedFile, file);

		}
		return file;
	}
}
