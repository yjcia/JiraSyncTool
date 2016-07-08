<%--
  Created by IntelliJ IDEA.
  User: YanJun
  Date: 2016/7/4
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="../bootstrap-table/dist/bootstrap-table.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../bootstrap/js/jquery.js"></script>
    <script type="text/javascript" src="../bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="../bootstrap-table/dist/bootstrap-table.js"></script>
</head>
<body class="home-body">
<jsp:include page="toolbar.jsp"/>
<div class="container" style="margin-top: 50px" >
    <div class="row">
        <div class="col-md-12">
            <div class="btn-group" role="group" aria-label="...">
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="issue_table"
                   data-height="800"
                   data-click-to-select="true"
                   data-row-style="rowStyle"/>

        </div>
    </div>

</div>
<script>
    var issuesData = null;
    var $table = null;

    $(function () {
        //var dataObj = [{"keyId":"1001","title":"title"}];

        $table = $('#issue_table').bootstrapTable({
            method: 'post',
            url: '/jira/issue.action?method=loadAll',
            cache: false,
            striped: true,
            search: true,
            showRefresh: true,
            showToggle: true,
            clickToSelect: true,
            pagination:true,
            pageSize:20,
            columns: [
                {
                    field: 'state',
                    checkbox: true,
                    align: 'center',
                    valign: 'middle',
                    visible: true,

                }, {
                    field: 'keyId',
                    title: 'id',
                    align: 'center',
                    valign: 'middle',
                    visible: true,
                    cellStyle:issueCellStyle
                }, {
                    field: 'title',
                    title: 'Title',
                    align: 'center',
                    valign: 'middle',
                    cellStyle:issueCellStyle

                }, {
                    field: 'summary',
                    title: 'Summary',
                    align: 'center',
                    valign: 'middle',
                    cellStyle:issueCellStyle

                }, {
                    field: 'type',
                    title: 'Type',
                    align: 'center',
                    valign: 'middle',
                    cellStyle:issueCellStyle

                }, {
                    field: 'status',
                    title: 'Status',
                    align: 'center',
                    valign: 'middle',
                    cellStyle:issueCellStyle
                }, {
                    field: 'resolution',
                    title: 'Resolution',
                    align: 'center',
                    valign: 'middle',
                    cellStyle:issueCellStyle
                }, {
                    field: 'assignee',
                    title: 'Assignee',
                    align: 'center',
                    valign: 'middle',
                    cellStyle:issueCellStyle
                }, {
                    field: 'reporter',
                    title: 'Reporter',
                    align: 'center',
                    cellStyle:issueCellStyle
                    //events: operateEvents,
                    //formatter: operateFormatter
                }
            ]
        });

    })

    function rowStyle(row, index) {
        var classes = ['active', 'success', 'info', 'warning', 'danger'];
        if (row.status == 'Closed') {
            return {
                classes: classes[1]
            };
        }else if(row.status == 'Open'){
            return {
                classes: classes[4]
            };
        }
        else{
            return {
                classes: classes[2]
            };
        }
    }
    function issueCellStyle(value, row, index, field) {

        return {
            css: {"font-size": "10px"}
        };
    }</script>
</body>
</html>
