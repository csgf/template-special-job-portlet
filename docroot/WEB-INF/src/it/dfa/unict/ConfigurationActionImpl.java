package it.dfa.unict;

import it.dfa.unict.util.Constants;
import it.dfa.unict.util.Utils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

public class ConfigurationActionImpl implements ConfigurationAction {

	private final Log _log = LogFactoryUtil
			.getLog(ConfigurationActionImpl.class);

	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String portletResource = ParamUtil.getString(actionRequest,
				"portletResource");

		_log.debug("processAction()");
		String cmd = ParamUtil.getString(actionRequest, Constants.CMD, "");
		_log.debug("Command: " + cmd);

		switch (cmd) {
		case Constants.SAVE_PREFS:
			savePrefereces(actionRequest, portletResource);
			break;
		case Constants.SAVE_INFRASTRUCTURE:
			saveInfrastructure(actionRequest, portletResource);
			break;
		case Constants.TOGGLE_INFRASTRUCTURE:
			toggleInfrastructureStatus(actionRequest, portletResource);
			break;
		case Constants.DELETE_INFRASTRUCTURE:
			deleteInfrastructure(actionRequest, portletResource);
			break;
		case Constants.SAVE_PILOT:
			savePilot(actionRequest, portletResource);
			break;
		default:
			_log.warn("Unsupported command.");
			SessionErrors.add(actionRequest, "usupported-action");
		}

	}

	@Override
	public String render(PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		String page = ParamUtil.getString(renderRequest, "render-page",
				Constants.CONFIGURATION_PAGE);

		_log.debug("Loading: " + Constants.CONFIGURATION_JSPS_FOLDER + page);
		return Constants.CONFIGURATION_JSPS_FOLDER + page;
	}

	private void savePrefereces(ActionRequest actionRequest,
			String portletResource) throws PortalException, SystemException {

		PortletPreferences preferences = PortletPreferencesFactoryUtil
				.getPortletSetup(actionRequest, portletResource);

		AppPreferences appPreferences_new = new AppPreferences();

		String gridOperationId = ParamUtil.getString(actionRequest,
				"pref_gridOperationId", "");
		String gridOperationDesc = ParamUtil.getString(actionRequest,
				"pref_gridOperationDesc", "");
		boolean productionEnv = ParamUtil.getBoolean(actionRequest,
				"pref_productionEnv", true);
		String jobRequirements = ParamUtil.getString(actionRequest,
				"pref_jobRequirements", "");
		String pilotScript = ParamUtil.getString(actionRequest,
				"pref_pilotScript", "");

		appPreferences_new.setGridOperationId(gridOperationId);
		appPreferences_new.setGridOperationDesc(gridOperationDesc);
		appPreferences_new.setProductionEnviroment(productionEnv);
		appPreferences_new.setJobRequirements(jobRequirements);
		appPreferences_new.setPilotScript(pilotScript);

		if (!productionEnv) {
			appPreferences_new.setSciGwyUserTrackingDB_Hostname(ParamUtil
					.getString(actionRequest,
							"pref_sciGwyUserTrackingDB_Hostname", ""));
			appPreferences_new.setSciGwyUserTrackingDB_Username(ParamUtil
					.getString(actionRequest,
							"pref_sciGwyUserTrackingDB_Username", ""));
			appPreferences_new.setSciGwyUserTrackingDB_Password(ParamUtil
					.getString(actionRequest,
							"pref_sciGwyUserTrackingDB_Password", ""));
			appPreferences_new.setSciGwyUserTrackingDB_Database(ParamUtil
					.getString(actionRequest,
							"pref_sciGwyUserTrackingDB_Database", ""));
		}

		String JSONAppPrefs_new = JSONFactoryUtil
				.looseSerialize(appPreferences_new);
		_log.debug(JSONAppPrefs_new);

		try {
			preferences.setValue(Constants.APP_PREFERENCES, JSONAppPrefs_new);
			preferences.store();
			_log.debug(preferences.getValue(Constants.APP_PREFERENCES, null));
			SessionMessages.add(actionRequest, Constants.CONFIG_SAVED_SUCCESS);
		} catch (ReadOnlyException e) {
			_log.error(e.getMessage());
			SessionErrors.add(actionRequest, e.getMessage());
			e.printStackTrace();
		} catch (ValidatorException e) {
			_log.error(e.getMessage());
			SessionErrors.add(actionRequest, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error(e.getMessage());
			SessionErrors.add(actionRequest, e.getMessage());
			e.printStackTrace();
		}

	}

	private void saveInfrastructure(ActionRequest actionRequest,
			String portletResource) throws PortalException, SystemException {
		_log.debug("editInfrastructure()");

		AppInfrastructureInfo infrastructure = new AppInfrastructureInfo();
		infrastructure.setBDII(ParamUtil.getString(actionRequest,
				"pref_bdiiHost", ""));
		infrastructure.setEnableInfrastructure(ParamUtil.getBoolean(
				actionRequest, "pref_enabledInfrastructure"));
		infrastructure.setETokenServer(ParamUtil.getString(actionRequest,
				"pref_pxServerHost", ""));
		infrastructure.setETokenServerPort(ParamUtil.getString(actionRequest,
				"pref_pxServerPort", ""));
		infrastructure.setFQAN(ParamUtil.getString(actionRequest,
				"pref_pxRobotRole", ""));
		infrastructure.setMiddleware(ParamUtil.getString(actionRequest,
				"pref_acronymInfrastructure"));
		infrastructure.setName(ParamUtil.getString(actionRequest,
				"pref_nameInfrastructure"));
		infrastructure.setProxyId(ParamUtil.getString(actionRequest,
				"pref_pxRobotId", ""));
		infrastructure.setPxRobotRenewalFlag(ParamUtil.getBoolean(
				actionRequest, "pref_pxRobotRenewalFlag"));
		infrastructure.setPxServerSecure(ParamUtil.getBoolean(actionRequest,
				"pref_pxServerSecure"));
		infrastructure.setResourcemanagerList(ParamUtil.getString(
				actionRequest, "pref_wmsHosts", "").split(";"));
		infrastructure
				.setRFC(ParamUtil.getBoolean(actionRequest, "pref_pxRFC"));
		infrastructure.setSWTag(ParamUtil.getString(actionRequest,
				"pref_softwareTags", ""));
		infrastructure.setUserProxy(ParamUtil.getString(actionRequest,
				"pref_pxUserProxy", ""));
		infrastructure.setVO(ParamUtil.getString(actionRequest,
				"pref_pxRobotVO", ""));

		PortletPreferences preferences = PortletPreferencesFactoryUtil
				.getPortletSetup(actionRequest, portletResource);

		String JSONAppInfrastructuresInfo = preferences.getValue(
				Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES, null);

		List<AppInfrastructureInfo> infrastructures = Utils
				.getAppInfrastructureInfo(JSONAppInfrastructuresInfo);

		String infraId = ParamUtil.getString(actionRequest, "id", "");
		_log.debug("Adding/Updating infrastructure {id: '" + infraId + "' "
				+ infrastructure.toString());
		if (infraId.equals("")) {
			infraId = UUID.randomUUID().toString();
			infrastructure.setId(infraId);
			infrastructures.add(infrastructure);
		} else {
			for (int i = 0; i < infrastructures.size(); i++) {
				AppInfrastructureInfo oldInfrastructure = infrastructures
						.get(i);

				if (oldInfrastructure.getId().equals(infraId)) {
					infrastructure.setId(oldInfrastructure.getId());
					infrastructures.set(i, infrastructure);
				}
			}
		}

		JSONArray jsonArray = Utils.createJSONArray(infrastructures);

		try {
			storeInfrastructureInfo(actionRequest, preferences, jsonArray);
			SessionMessages.add(actionRequest, "infra-saved-success");
		} catch (ReadOnlyException e) {
			e.printStackTrace();
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void toggleInfrastructureStatus(ActionRequest actionRequest,
			String portletResource) throws PortalException, SystemException {

		String infraId = ParamUtil.getString(actionRequest, "id");

		PortletPreferences preferences = PortletPreferencesFactoryUtil
				.getPortletSetup(actionRequest, portletResource);
		String JSONAppInfrastructuresInfo = preferences.getValue(
				Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES, null);

		List<AppInfrastructureInfo> infrastructures = Utils
				.getAppInfrastructureInfo(JSONAppInfrastructuresInfo);

		for (int i = 0; i < infrastructures.size(); i++) {
			AppInfrastructureInfo infrastructure = infrastructures.get(i);
			if (infrastructure.getId().equals(infraId)) {
				_log.debug("Changing status of infrastructure: "
						+ infrastructure);
				infrastructure.setEnableInfrastructure(!infrastructure
						.isEnableInfrastructure());
				infrastructures.set(i, infrastructure);
			}
		}

		JSONArray jsonArray = Utils.createJSONArray(infrastructures);
		try {
			storeInfrastructureInfo(actionRequest, preferences, jsonArray);
			SessionMessages.add(actionRequest, "infra-toggle-success");
		} catch (ReadOnlyException e) {
			e.printStackTrace();
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void deleteInfrastructure(ActionRequest actionRequest,
			String portletResource) throws PortalException, SystemException {
		String infraId = ParamUtil.getString(actionRequest, "id");
		_log.debug("deleteInfra()");

		PortletPreferences preferences = PortletPreferencesFactoryUtil
				.getPortletSetup(actionRequest, portletResource);

		String JSONAppInfrastructuresInfo = preferences.getValue(
				Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES, null);

		List<AppInfrastructureInfo> infrastructures = Utils
				.getAppInfrastructureInfo(JSONAppInfrastructuresInfo);

		for (int i = 0; i < infrastructures.size(); i++) {
			AppInfrastructureInfo infrastructure = infrastructures.get(i);
			if (infrastructure.getId().equals(infraId)) {
				_log.debug("Deleting Infrastructure: "
						+ infrastructure.toString());
				infrastructures.remove(i);
				break;
			}
		}

		JSONArray jsonArray = Utils.createJSONArray(infrastructures);

		try {
			storeInfrastructureInfo(actionRequest, preferences, jsonArray);
			SessionMessages.add(actionRequest, "infra-delete-success");
		} catch (ReadOnlyException e) {
			e.printStackTrace();
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void storeInfrastructureInfo(ActionRequest actionRequest,
			PortletPreferences preferences, JSONArray jsonArray)
			throws ReadOnlyException, ValidatorException, IOException {

		String JSONAppInfrastructureInfosPreferences_new = JSONFactoryUtil
				.looseSerialize(jsonArray);
		_log.debug(JSONAppInfrastructureInfosPreferences_new);

		preferences.setValue(Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES,
				JSONAppInfrastructureInfosPreferences_new);
		preferences.store();
		_log.debug(preferences.getValue(
				Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES, null));

	}

	private void savePilot(ActionRequest actionRequest, String portletResource)
			throws IOException {
		String pilotScript = ParamUtil.getString(actionRequest, "pilotScript");
		pilotScript.replaceAll("\r", "");
		Utils.string2File(TemplateSpecialJobPortlet.pilotScript, pilotScript);

		SessionMessages.add(actionRequest, "pilot-update-success");
	}

}
