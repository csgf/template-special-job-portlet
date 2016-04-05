<%-- 
    Document   : edit.jsp
    Created on : Feb 24, 2016, 10:08:35 AM
    Author     : mario
--%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="it.dfa.unict.AppInfrastructureInfo"%>
<%@page import="it.dfa.unict.util.Constants"%>
<%@page import="it.dfa.unict.util.Utils"%>
<%@include file="../../init.jsp"%>

<%
	String id = ParamUtil.getString(renderRequest, "id", "");
	String portletResource = ParamUtil.getString(request,
			"portletResource");

	PortletPreferences preferences = PortletPreferencesFactoryUtil
			.getPortletSetup(request, portletResource);
	String JSONAppInfrastructuresInfo = GetterUtil
			.getString(preferences
					.getValue(
							Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES,
							null));
	AppInfrastructureInfo infrastructureInfo = Utils.getInfrastructure(
			id, JSONAppInfrastructuresInfo);
%>

<liferay-portlet:renderURL portletConfiguration="true" var="backURL">
	<liferay-portlet:param name="render-page"
		value="<%=Constants.CONFIGURATION_PAGE%>" />
</liferay-portlet:renderURL>

<liferay-ui:header backLabel="&laquo; Back" title=""
	backURL="<%=backURL%>" />

<liferay-portlet:actionURL portletConfiguration="true"
	var="editInfrastructureURL">
	<portlet:param name="<%=Constants.CMD%>"
		value="<%=Constants.SAVE_INFRASTRUCTURE%>" />
	<portlet:param name="id" value="<%=id%>" />
</liferay-portlet:actionURL>

<aui:form action="<%=editInfrastructureURL%>" method="post">

	<aui:fieldset label="infra-preferences">

		<aui:layout>
			<aui:column columnWidth="50" first="true">

				<aui:input type="checkbox" checked="true"
					name="pref_enabledInfrastructure" id="enabled" label="Enabled"
					value="<%=infrastructureInfo
									.isEnableInfrastructure()%>" />

				<aui:input type="text" size="60" name="pref_nameInfrastructure"
					id="infraName" value="<%=infrastructureInfo.getName()%>"
					label="Infrastructure name">
					<aui:validator name="required" />
				</aui:input>

				<aui:input type="text" size="60" name="pref_acronymInfrastructure"
					id="infraAcronym" value="<%=infrastructureInfo.getMiddleware()%>"
					helpMessage="supported-middleware" label="Middleware">
					<aui:validator name="required" />
					<aui:validator name="alpha" />
				</aui:input>

				<aui:input type="text" size="60" name="pref_bdiiHost" id="bdiiHost"
					value="<%=infrastructureInfo.getBDII()%>" label="BDII Host" />

				<aui:input type="text" size="60" name="pref_wmsHosts" id="wmsHosts"
					value="<%=StringUtil.merge(
									infrastructureInfo.getResourcemanagerList(),
									";")%>"
					label="WMS Hosts" />

				<aui:input type="text" size="60" name="pref_pxServerHost"
					id="pxServerHost" value="<%=infrastructureInfo.getETokenServer()%>"
					label="Proxy Robot host server" />

				<aui:input type="text" size="60" name="pref_pxServerPort"
					id="pxServerPort"
					value="<%=infrastructureInfo.getETokenServerPort()%>"
					label="Proxy Robot host port" />

				<aui:input type="checkbox" checked="true" name="pref_pxServerSecure"
					id="enabled" label="Proxy Robot secure connection"
					value="<%=infrastructureInfo.isPxServerSecure()%>" />
			</aui:column>
			<aui:column columnWidth="50" last="true">

				<aui:input type="text" size="60" name="pref_pxRobotId"
					id="pxRobotId" value="<%=infrastructureInfo.getProxyId()%>"
					label="Proxy Robot Identifier" />

				<aui:input type="text" size="60" name="pref_pxRobotVO"
					id="pxRobotVO" value="<%=infrastructureInfo.getVO()%>"
					label="Proxy Robot Virtual Organization" />

				<aui:input type="text" size="60" name="pref_pxRobotRole"
					id="pxRobotRole" value="<%=infrastructureInfo.getFQAN()%>"
					label="Proxy Robot VO Role" />

				<aui:input type="checkbox" checked="true"
					name="pref_pxRobotRenewalFlag" id="enabled"
					label="Proxy Robot Renewal Flag"
					value="<%=infrastructureInfo.isPxRobotRenewalFlag()%>" />

				<aui:input type="checkbox" checked="true" name="pref_pxRFC"
					id="enabled" label="RFC Proxy Robot"
					value="<%=infrastructureInfo.getRFC()%>" />

				<aui:input type="text" size="60" name="pref_pxUserProxy"
					id="pxUserProxy" value="<%=infrastructureInfo.getUserProxy()%>"
					label="Local Proxy" />

				<aui:input type="text" size="60" name="pref_softwareTags"
					id="softwareTags" value="<%=infrastructureInfo.getSWTag()%>"
					label="Software Tags" />
			</aui:column>
		</aui:layout>

		<aui:field-wrapper cssClass="centered">
			<aui:button name="save" value="save" type="submit" />
			<aui:button name="cancel" value="reset" type="reset" />
		</aui:field-wrapper>

	</aui:fieldset>
</aui:form>