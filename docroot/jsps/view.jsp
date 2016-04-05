<%-- 
    Document   : view
    Created on : Feb 15, 2016, 10:29:44 AM
    Author     : mario
--%>

<%@page import="it.dfa.unict.AppInfrastructureInfo"%>
<%@page import="it.dfa.unict.AppPreferences"%>
<%@page import="it.dfa.unict.util.Constants"%>
<%@page import="it.dfa.unict.util.Utils"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@include file="../init.jsp"%>

<%
	PortletPreferences preferences = renderRequest.getPreferences();
	String JSONAppInfras = GetterUtil.getString(preferences.getValue(
	Constants.APP_INFRASTRUCTURE_INFO_PREFERENCES, null));

	List<AppInfrastructureInfo> enabledInfas = Utils
	.getEnabledInfrastructureInfo(JSONAppInfras);

	SimpleDateFormat dateFormat = new SimpleDateFormat(
	Constants.TS_FORMAT);
	String timestamp = dateFormat.format(Calendar.getInstance()
	.getTime());

	String jobLabel = user.getScreenName() + "_" + timestamp;
%>

<aui:layout>
	<aui:column columnWidth="50" first="true">
		<img src="<%=request.getContextPath()%>/images/AppLogo.png"
			height="80%" width="80%" />
	</aui:column>
	<aui:column columnWidth="50" last="true">
		<%=LanguageUtil.get(portletConfig,
							themeDisplay.getLocale(), "how-to-use")%>
	</aui:column>

	<aui:column columnWidth="100" first="true">
		<c:choose>
			<c:when test="<%=enabledInfas.size() == 0%>">
				<div class="portlet-msg-info">
					<liferay-ui:message key="no-infras-available" />
				</div>
			</c:when>
			<c:otherwise>
				<portlet:actionURL name="submit" var="submitURL" />

				<aui:form action="<%=submitURL%>" name="aform" method="post">

					<aui:fieldset label="application-input">

						<aui:select id="collection_type_id" label="Select special job"
							name="collection_type" onChange="resetForm()">
							<aui:option value="JOB_COLLECTION">Job Collection</aui:option>
							<aui:option value="WORKFLOW_N1">Workflow N1</aui:option>
							<aui:option value="JOB_PARAMETRIC">Parametric job</aui:option>
						</aui:select>
						<aui:input type="text" name="task_number" inlineField="true"
							id="task_number" label="Task number">
							<aui:validator name="required" />
							<aui:validator name="digits" />
						</aui:input>
						<aui:button value="OK" type="button" onClick="updatePage()" />
						<div id="container"></div>

						<aui:input type="text" name="jobLabel" label="job-label" size="60"
							helpMessage="job-label-help" id="jobLabel" value="<%=jobLabel%>" />

						<aui:input type="hidden" name="executables" label="executables"
							id="executables" />
							<aui:input type="hidden" name="arguments" label="arguments"
							id="arguments" />
						<aui:field-wrapper>
							<aui:button name="demo" value="Demo" type="button"
								onClick="addDemo()" />
							<aui:button name="submit" value="submit" type="button"
								onClick="preSubmit()" />
							<aui:button name="reset" value="cancel" type="reset" />
						</aui:field-wrapper>
					</aui:fieldset>
				</aui:form>
			</c:otherwise>
		</c:choose>
	</aui:column>
</aui:layout>

<script type="text/javascript">
	var taskNumberId = "<portlet:namespace/>task_number";
	var jobLabelId = "<portlet:namespace/>jobLabel";
	var collectionTypeId = "<portlet:namespace/>collection_type_id";
	var containerId = "<portlet:namespace/>container";
	var executablesId = "<portlet:namespace/>executables";
	var argumentsId = "<portlet:namespace/>arguments";
</script>