<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
    <h2>Power switch status page</h2>
    <c:if test="${not empty switchList}">
    <table>
        <tr>
            <td>Id</td>
            <td>Name</td>
            <td>State</td>
            <td>Toggle</td>
        </tr>
        <c:forEach var="listValue" items="${switchList}">
            <tr>
                <td>${listValue.id}</td>
                <td>${listValue.name}</td>
                <td>${listValue.state}</td>
                <c:url value="/web/switches/setState" var="url">
                    <c:param name="switchId" value="${listValue.id}"/>
                    <c:choose>
                        <c:when test="${listValue.state == 'ON'}">
                            <c:param name="state" value="OFF"/>
                        </c:when>
                        <c:otherwise>
                            <c:param name="state" value="ON"/>
                        </c:otherwise>
                    </c:choose>
                </c:url>
                <td>
                    <a href="${url}">
                        Toggle
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
    </c:if>
</body>
</html>
