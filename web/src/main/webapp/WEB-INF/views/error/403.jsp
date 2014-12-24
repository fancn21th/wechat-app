<%--
  403 access denied page.
  User: Bill Lv (Lv, Chao)
  Date: 2014/12/17
  Time: 16:54
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="Disallow">
    <meta name="author" content="lvchao, billcc.lv@hotmail.com">
    <title>Checkin</title>
    <c:set value="${pageContext.request.contextPath}" var="contextPath"/>
    <link rel="shortcut icon" type="image/x-icon" href="${contextPath}/assets/images/favicon.ico">
    <link rel="stylesheet" href="${contextPath}/libs/semantic-ui/dist/semantic.min.css">
</head>
<body>
<div class="ignored ui error message">禁止访问</div>
</body>
</html>
