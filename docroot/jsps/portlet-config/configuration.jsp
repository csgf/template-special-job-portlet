<%-- 
    Document   : edit.jsp
    Created on : Feb 15, 2016, 10:08:35 AM
    Author     : mario
--%>

<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="it.dfa.unict.AppInfrastructureInfo"%>
<%@page import="it.dfa.unict.AppPreferences"%>
<%@page import="it.dfa.unict.util.Constants"%>
<%@page import="it.dfa.unict.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@include file="../../init.jsp"%>

<%
	PortletPreferences preferences = null;
	
	String portletResource = ParamUtil.getString(request, "portletResource");
		
	if (Validator.isNotNull(portletResource)) {
    	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
	}

	String JSONAppPrefs = GetterUtil.getString(preferences
		.getValue(Constants.APP_PREFERENCES, null));
	AppPreferences appPreferences = Utils.getAppPreferences(JSONAppPrefs);

	int infrastrucuresCount = 0;
	
	String JSONAppInfrastructuresInfo = GetterUtil.getString(preferences
		.getValue(Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES, null));
	
	List<AppInfrastructureInfo> infrastructures = Utils.getAppInfrastructureInfo(JSONAppInfrastructuresInfo);
	
	if (JSONAppInfrastructuresInfo != null){
		infrastrucuresCount = infrastructures.size();
	}
%>

<liferay-portlet:actionURL portletConfiguration="true"
	var="savePreferencesUrl">
	<portlet:param name="<%=Constants.CMD%>"
		value="<%=Constants.SAVE_PREFS%>" />
</liferay-portlet:actionURL>

<liferay-portlet:renderURL portletConfiguration="true"
	var="addInfrastructureUrl">
	<liferay-portlet:param name="render-page"
		value="<%=Constants.EDIT_INFRA_PAGE%>" />
	<liferay-portlet:param name="infrastrucuresCount"
		value="<%=String.valueOf(infrastrucuresCount)%>" />
	<liferay-portlet:param name="add" value="true" />
</liferay-portlet:renderURL>

<liferay-portlet:renderURL portletConfiguration="true"
	var="pilotScriptUrl">
	<liferay-portlet:param name="render-page"
		value="<%=Constants.VIEW_PILOT_PAGE%>" />
	<liferay-portlet:param name="pilotScript"
		value="<%=appPreferences.getPilotScript()%>" />
</liferay-portlet:renderURL>
<liferay-portlet:renderURL portletConfiguration="true"
	varImpl="iteratorURL">
	<liferay-portlet:param name="render-page"
		value="<%=Constants.CONFIGURATION_PAGE%>" />
</liferay-portlet:renderURL>

<liferay-ui:success key="<%=Constants.CONFIG_SAVED_SUCCESS%>"
	message="<%=Constants.CONFIG_SAVED_SUCCESS%>" />

<aui:form action="${savePreferencesUrl}" name="aForm" method="post">
	<aui:fieldset label="generic-pref-lbl">
		<aui:field-wrapper>
			<aui:button name="save" value="save" type="submit" />
		</aui:field-wrapper>
		<aui:layout>
			<aui:column columnWidth="50" first="true">
				<aui:input type="text" name="pref_gridOperationId"
					id="gridOperationId" label="Application identifier" size="60"
					value="<%=appPreferences.getGridOperationId()%>">
					<aui:validator name="digits" errorMessage="app-id" />
				</aui:input>

				<aui:input type="text" name="pref_gridOperationDesc"
					id="gridOperationDesc" label="Application label" size="60"
					value="<%=appPreferences.getGridOperationDesc()%>">
					<aui:validator name="required" />
				</aui:input>

				<aui:input type="text" name="pref_numInfrastructures" size="60"
					id="numInfrastructures" label="Infrastructure number"
					disabled="true" value="<%=infrastrucuresCount%>" />

				<aui:input type="checkbox" name="pref_productionEnv"
					id="productionEnv" label="Production environment" size="60"
					checked="<%=appPreferences.isProductionEnviroment()%>"
					onChange="showDevEnvPanel()" />

				<aui:input type="textarea" name="pref_jobRequirements"
					id="jobRequirements" label="Application requirements" cols="60"
					value="<%=appPreferences.getJobRequirements()%>" />

				<liferay-ui:success key="pilot-update-success"
					message="pilot-update-success" />
				<aui:button id="pilot" name="piltot_btn" value="Edit pilot script"
					onClick="<%= pilotScriptUrl %>" />
			</aui:column>

			<aui:column columnWidth="50" last="true" id="devEnvPrefs">
				<aui:input type="text" name="pref_sciGwyUserTrackingDB_Hostname"
					id="sciGwyUserTrackingDB_Hostname" label="UserTrackingDB hostname"
					size="60"
					value="<%=appPreferences
									.getSciGwyUserTrackingDB_Hostname()%>" />

				<aui:input type="text" name="pref_sciGwyUserTrackingDB_Username"
					id="sciGwyUserTrackingDB_Username" label="UserTrackingDB username"
					size="60"
					value="<%=appPreferences
									.getSciGwyUserTrackingDB_Username()%>" />

				<aui:input type="text" name="pref_sciGwyUserTrackingDB_Password"
					id="sciGwyUserTrackingDB_Password" label="UserTrackingDB password"
					size="60"
					value="<%=appPreferences
									.getSciGwyUserTrackingDB_Password()%>" />

				<aui:input type="text" name="pref_sciGwyUserTrackingDB_Database"
					id="sciGwyUserTrackingDB_Database" label="UserTrackingDB database"
					size="60"
					value="<%=appPreferences
									.getSciGwyUserTrackingDB_Database()%>" />

			</aui:column>
		</aui:layout>
	</aui:fieldset>
</aui:form>
<br />
<aui:fieldset label="available-infras">
	<aui:field-wrapper>
		<aui:button id="add" name="add" value="add-new"
			onClick="<%= addInfrastructureUrl %>" />
	</aui:field-wrapper>

	<liferay-ui:success key="infra-saved-success"
		message="infra-saved-success" />
	<liferay-ui:success key="infra-delete-success"
		message="infra-delete-success" />
	<liferay-ui:success key="infra-toggle-success"
		message="infra-toggle-success" />

	<liferay-ui:search-container delta="10" emptyResultsMessage="no-infras-available" iteratorURL="<%=iteratorURL %>">
		<liferay-ui:search-container-results
			total="<%=infrastructures.size()%>"
			results="<%=ListUtil.subList(infrastructures, searchContainer.getStart(), searchContainer.getEnd())%>" />
		<liferay-ui:search-container-row modelVar="infrastructure"
			className="it.dfa.unict.AppInfrastructureInfo">
			<liferay-ui:search-container-column-text name="name"
				value="<%=infrastructure.getName()%>" />
			<liferay-ui:search-container-column-text name="acronym"
				value="<%=infrastructure.getMiddleware()%>" />
			<liferay-ui:search-container-column-text name="status">
				<c:if test="<%=infrastructure.isEnableInfrastructure()%>">
					<liferay-ui:icon image="activate" />
				</c:if>
				<c:if test="<%=!infrastructure.isEnableInfrastructure()%>">
					<liferay-ui:icon image="deactivate" />
				</c:if>
			</liferay-ui:search-container-column-text>
			<liferay-ui:search-container-column-jsp
				path="/jsps/portlet-config/infra-action.jsp" />
		</liferay-ui:search-container-row>
		<liferay-ui:search-iterator searchContainer="<%=searchContainer%>" />
	</liferay-ui:search-container>

</aui:fieldset>

<script type="text/javascript">
	var productionEnvId = "<portlet:namespace/>productionEnv";
	var devEnvPrefsId = "<portlet:namespace/>devEnvPrefs";

	AUI().ready(function(A) {
		if (productionEnvId)
			showDevEnvPanel();
	});

	function showDevEnvPanel() {
		if (document.getElementById(productionEnvId).value === "true") {
			document.getElementById(devEnvPrefsId).style.display = 'none';
		} else {
			document.getElementById(devEnvPrefsId).style.display = 'block';
		}
	}
</script>