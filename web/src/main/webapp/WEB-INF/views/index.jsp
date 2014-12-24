<%--
  Index Page.
  User: Bill Lv <billcc.lv@hotmail.com>
  Date: 2014/12/20
  Time: 11:27
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1,user-scalable=no">
    <meta name="robots" content="Disallow">
    <meta name="author" content="lvchao, billcc.lv@hotmail.com">
    <meta name="copyright" content="http://renyuxian.mobi/">
    <title>${user.nickname}</title>
    <c:set value="${pageContext.request.contextPath}" var="contextPath"/>
    <link rel="shortcut icon" type="image/x-icon" href="${contextPath}/assets/images/favicon.ico">
    <link rel="stylesheet" href="${contextPath}/assets/css/main.css">
</head>
<body ng-app="main">
<div ng-controller="mainController" data-ng-init="init('${user.id}')">

    <section>
        <form id="checkinForm" name="checkinForm" novalidate>
            <div class="textarea-area">
                <div class="textarea">
                    <textarea id="record" name="record" placeholder="输入内容...."
                              ng-model="model.newCheckin.record"
                              ng-required="true" ng-maxlength="240"></textarea>

                    <div class="release-success"><span>发布成功！</span></div>
                </div>
                <div class="textarea-btns">
                    <div class="upload-btn {{ model.uploaded ? 'noact-btn' : ''}}">
                        <input type="file" name="attachmentName" id="attachmentName"
                               ng-file-select ng-model="model.newPhotos"
                               ng-multiple="false"
                               accept="image/*"
                               resetOnClick="true">
                        <span>{{model.uploaded ? "已传图" : "传图"}}</span>
                    </div>
                    <a class="act-btn {{model.checkedin || checkinForm.record.$invalid || checkinForm.record.$pristine ? 'noact-btn' : ''}}"
                       href=""
                       ng-click="checkin()"><span>打卡</span></a>
                </div>
            </div>
        </form>

        <div ng-repeat="checkin in model.checkins | orderBy: 'timestamp' : true">
            <article class="act-mod"
                     ng-class="{'act-color-red': $index % 3 == 0, 'act-color-blue': $index % 3 == 1, 'act-color-yellow': $index % 3 == 2}">
                <h2>{{checkin.timestamp | date:'yyyy年MM月dd日'}}</h2>

                <p>{{checkin.record}}</p>

                <div ng-show="checkin.photoIds.length > 0">
                    <div ng-repeat="photoId in checkin.photoIds">
                        <img ng-show="photoId" ng-src="/checkin/photos/{{photoId}}" alt="">
                    </div>
                </div>
            </article>
        </div>
    </section>
</div>

<footer><span>想做就做</span></footer>

<script type="text/javascript" src="${contextPath}/libs/jquery/dist/jquery.min.js"></script>
<script type="text/javascript" src="${contextPath}/assets/javascripts/zepto.min.js"></script>
<script type="text/javascript" src="${contextPath}/libs/angular/angular.min.js"></script>
<script type="text/javascript" src="${contextPath}/libs/angular-resource/angular-resource.min.js"></script>
<script type="text/javascript" src="${contextPath}/libs/ng-file-upload/angular-file-upload.min.js"></script>
<script type="text/javascript" src="${contextPath}/app/main/app.js"></script>
<script type="text/javascript" src="${contextPath}/app/main/config.js"></script>
<script type="text/javascript" src="${contextPath}/app/main/controllers.js"></script>
<script type="text/javascript" src="${contextPath}/app/main/services.js"></script>

</body>
</html>
