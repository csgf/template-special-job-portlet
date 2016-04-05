<%@page import="it.dfa.unict.AppInfrastructureInfo"%>
<%@page import="it.dfa.unict.util.Constants"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@include file="../../init.jsp"%>

<%
	ResultRow row = (ResultRow) request
			.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

	AppInfrastructureInfo infrastructure = (AppInfrastructureInfo) row
			.getObject();

// 	String cmdValue;
// 	if (infra.isEnableInfrastructure())
// 		cmdValue = Constants.DISABLE_INFRASTRUCTURE;
// 	else
// 		cmdValue = Constants.ENABLE_INFRASTRUCTURE;
%>


<liferay-ui:icon-menu>

	<liferay-portlet:actionURL portletConfiguration="true"
		var="changeInfraStatusURL">
		<liferay-portlet:param name="<%=Constants.CMD%>" value="<%=Constants.TOGGLE_INFRASTRUCTURE%>" />
		<liferay-portlet:param name="id"
			value="<%=infrastructure.getId()%>" />
		<liferay-portlet:param name="enabled"
			value="<%=String.valueOf(!infrastructure.isEnableInfrastructure())%>" />
	</liferay-portlet:actionURL>
	<c:if test="<%=infrastructure.isEnableInfrastructure()%>">
		<liferay-ui:icon image="deactivate" url="<%=changeInfraStatusURL%>" />

	</c:if>
	<c:if test="<%=!infrastructure.isEnableInfrastructure()%>">
		<liferay-ui:icon image="activate" url="<%=changeInfraStatusURL%>" />
	</c:if>

	<liferay-portlet:renderURL portletConfiguration="true"
		var="editInfraURL">
		<liferay-portlet:param name="render-page"
			value="<%=Constants.EDIT_INFRA_PAGE%>" />
		<liferay-portlet:param name="id"
			value="<%=infrastructure.getId()%>" />
	</liferay-portlet:renderURL>
	<liferay-ui:icon image="edit" label="true" url="<%=editInfraURL%>" />

	<liferay-portlet:actionURL portletConfiguration="true"
		var="deleteInfraURL">
		<liferay-portlet:param name="<%=Constants.CMD%>"
			value="<%=Constants.DELETE_INFRASTRUCTURE%>" />
		<liferay-portlet:param name="id"
			value="<%=infrastructure.getId()%>" />
	</liferay-portlet:actionURL>
	<liferay-ui:icon-delete url="<%=deleteInfraURL%>" />

</liferay-ui:icon-menu>
