package it.dfa.unict.util;

import it.dfa.unict.AppInfrastructureInfo;
import it.dfa.unict.AppPreferences;
import it.infn.ct.GridEngine.Job.InfrastructureInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;

public class Utils {

	public static AppPreferences getAppPreferences(String JSONAppPrefs) {
		AppPreferences appPreferences = new AppPreferences();

		if (JSONAppPrefs != null && !JSONAppPrefs.equals(""))
			appPreferences = JSONFactoryUtil.looseDeserialize(JSONAppPrefs,
					AppPreferences.class);

		return appPreferences;
	}

	public static List<AppInfrastructureInfo> getAppInfrastructureInfo(
			String JSONAppInfrastructuresInfo) {

		List<AppInfrastructureInfo> appInfrastructureInfoPreferences = new ArrayList<AppInfrastructureInfo>();

		if (JSONAppInfrastructuresInfo != null
				&& !JSONAppInfrastructuresInfo.equals("")) {
			try {

				JSONArray JSONAppInfrastructuresInfoArray = JSONFactoryUtil
						.createJSONArray(JSONAppInfrastructuresInfo);

				for (int i = 0; i < JSONAppInfrastructuresInfoArray.length(); i++) {
					appInfrastructureInfoPreferences.add(JSONFactoryUtil
							.looseDeserialize(JSONAppInfrastructuresInfoArray
									.getJSONObject(i).toString(),
									AppInfrastructureInfo.class));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return appInfrastructureInfoPreferences;
	}

	public static List<AppInfrastructureInfo> getEnabledInfrastructureInfo(
			String JSONAppInfrastructuresInfo) {
		List<AppInfrastructureInfo> result = new ArrayList<AppInfrastructureInfo>();
		List<AppInfrastructureInfo> appInfrastructureInfoPreferences = getAppInfrastructureInfo(JSONAppInfrastructuresInfo);

		for (AppInfrastructureInfo appInfrastructureInfo : appInfrastructureInfoPreferences) {
			if (appInfrastructureInfo.isEnableInfrastructure())
				result.add(appInfrastructureInfo);
		}

		return result;
	}

	public static JSONArray createJSONArray(
			List<AppInfrastructureInfo> appInfrastructureInfosPreferences) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		for (AppInfrastructureInfo appInfrastructureInfoPreferences : appInfrastructureInfosPreferences) {
			try {
				JSONSerializer jsonSerializer = JSONFactoryUtil
						.createJSONSerializer();
				jsonSerializer.include("ResourcemanagerList");
				jsonSerializer.include("CEList");
				jsonArray.put(JSONFactoryUtil.createJSONObject(jsonSerializer
						.serializeDeep(appInfrastructureInfoPreferences)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	public static AppInfrastructureInfo getInfrastructure(String id,
			String JSONAppInfrastructuresInfo) {
		List<AppInfrastructureInfo> infrastructures = getAppInfrastructureInfo(JSONAppInfrastructuresInfo);
		for (AppInfrastructureInfo infrastructure : infrastructures) {
			if (infrastructure.getId().equals(id))
				return infrastructure;
		}
		return new AppInfrastructureInfo();
	}

	/**
	 * This method takes as input a filename and will transfer its content
	 * inside a String variable
	 *
	 * @param file
	 *            A complete path to a given file
	 * @return File content into a String
	 * @throws IOException
	 */
	public static String file2String(String file) throws IOException {
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(Constants.NEW_LINE);
		}
		reader.close();
		return stringBuilder.toString();
	}

	/**
	 * This method will transfer the content of a given String into a given
	 * filename
	 *
	 * @param fileName
	 *            A complete path to a file to write
	 * @param fileContent
	 *            The string content of the file to write
	 * @throws IOException
	 */
	public static void string2File(String fileName, String fileContent)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		writer.write(fileContent);
		writer.close();
	}

	public static InfrastructureInfo[] convertAppInfrastructureInfo(
			List<AppInfrastructureInfo> enabledInfras)
			throws UnsupportedEncodingException {

		// Initialize the array of GridEngine' infrastructure objects
		InfrastructureInfo infrastructuresInfo[] = new InfrastructureInfo[enabledInfras
				.size()];
		// For each infrastructure
		for (int i = 0, h = 0; i < enabledInfras.size(); i++) {
			// For each enabled infrastructure ...
			AppInfrastructureInfo enabledAppInfrastructureInfo = enabledInfras
					.get(i);

			// Take care of the adaptor/wms list
			// GridEngine supports a list of WMSes/adaptors as an array of
			// strings
			// while the AppPreferences uses a ';' separated list of entries
			// Following code makes the necessary conversion
			String wmsHostList[] = null;
			if (null != enabledAppInfrastructureInfo.getResourcemanagerList()
					&& !enabledAppInfrastructureInfo.getResourcemanagerList()
							.equals("")) {
				wmsHostList = enabledAppInfrastructureInfo
						.getResourcemanagerList();
			} // if wmsList

			// Multi-infrastructure support
			// Different Infrastructure objects can be created accordingly to
			// the
			// requested infrastructure type specified into the
			// acronymInfrastructure
			// field. Each infrastructure maps its specific values accordingly
			// to
			// several field mappings shown below on each infrastructure case
			if (enabledAppInfrastructureInfo.getMiddleware().equalsIgnoreCase(
					"ssh")) {
				// Multi-infrastructure support (field mapping)
				// ssh requires to specify:
				// * Username -> bdiiHost first field of the given ';' separated
				// string
				// * Password -> bdiiHost second field of the given ';'
				// separated string
				// * JSAGA adaptor -> wmsHosts
				String sshItems[] = enabledAppInfrastructureInfo.getBDII()
						.split(";");
				int numSshItems = sshItems.length;
				String username = "";
				String password = "";
				if (numSshItems > 0)
					username = sshItems[0].trim();
				if (numSshItems > 1)
					password = sshItems[1].trim();

				// Build the GridEngine' infrastructure object and assign it to
				// the infrastructures array
				infrastructuresInfo[h++] = new InfrastructureInfo(
						enabledAppInfrastructureInfo.getName(),
						enabledAppInfrastructureInfo.getMiddleware()
								.toLowerCase(), username, password, wmsHostList);

			} else if (enabledAppInfrastructureInfo.getMiddleware()
					.equalsIgnoreCase("rocci")) {
				// Multi-infrastructure support (field mapping)
				// rocci requires to specify:
				// * OCCI_ENDPOINT_HOST -> bdiiHost first field of the given ';'
				// separated string
				// * Password -> bdiiHost second field of the given ';'
				// separated string
				// * JSAGA adaptor -> wmsHosts

				String OCCI_ENDPOINT_HOST = wmsHostList[0];

				String OCCI_AUTH = "x509";

				// Possible RESOURCE values: 'os_tpl', 'resource_tpl', 'compute'
				String OCCI_RESOURCE = "compute";
				String OCCI_VM_TITLE = URLEncoder.encode(
						enabledAppInfrastructureInfo.getBDII(), "UTF-8")
						.toString();

				// Possible ACTION values: 'list', 'describe', 'create' and
				// 'delete'
				String OCCI_ACTION = "create";

				String[] occiItems = enabledAppInfrastructureInfo.getSWTag()
						.split(";");
				int occiItemscount = occiItems.length;

				String OCCI_OS = "";
				String OCCI_FLAVOR = "";
				String OCCI_LINK = "";

				if (occiItemscount > 0)
					OCCI_OS = occiItems[0].trim();
				if (occiItemscount > 1)
					OCCI_FLAVOR = occiItems[1].trim();
				if (occiItemscount > 2)
					OCCI_LINK = occiItems[2].trim();

				String rOCCIURL = OCCI_ENDPOINT_HOST + "?" + "action="
						+ OCCI_ACTION + "&resource=" + OCCI_RESOURCE
						+ "&attributes_title=" + OCCI_VM_TITLE
						+ "&mixin_os_tpl=" + OCCI_OS + "&mixin_resource_tpl="
						+ OCCI_FLAVOR + "&auth=" + OCCI_AUTH + "&link="
						+ OCCI_LINK;

				String rOCCIResourcesList[] = { rOCCIURL };

				infrastructuresInfo[h++] = new InfrastructureInfo(
						enabledAppInfrastructureInfo.getMiddleware()
								.toLowerCase(), "", rOCCIResourcesList,
						enabledAppInfrastructureInfo.getETokenServer(),
						enabledAppInfrastructureInfo.getETokenServerPort(),
						enabledAppInfrastructureInfo.getProxyId(),
						enabledAppInfrastructureInfo.getVO(),
						enabledAppInfrastructureInfo.getFQAN(), true);

			} else {
				// Multi-infrastructure support (no matching cases)
				// If the acronym does not match to a specific infrastructure
				// type
				// the preference fields will be interpreted as gLite middleware
				// settings

				// Build the GridEngine' infrastructure object and assign it to
				// the infrastructures array
				// (!)Not yet used values:
				// pxServerSecure
				// pxRobotRenewalFlag
				// pxUserProxy
				infrastructuresInfo[h++] = new InfrastructureInfo(
						enabledAppInfrastructureInfo.getMiddleware()
								.toLowerCase(),
						enabledAppInfrastructureInfo.getBDII(), wmsHostList,
						enabledAppInfrastructureInfo.getETokenServer(),
						enabledAppInfrastructureInfo.getETokenServerPort(),
						enabledAppInfrastructureInfo.getProxyId(),
						enabledAppInfrastructureInfo.getVO(),
						enabledAppInfrastructureInfo.getFQAN(),
						enabledAppInfrastructureInfo.getSWTag());
			}
			// Shows the added infrastructure

		} // for each infrastructure
		return infrastructuresInfo;
	}
}
