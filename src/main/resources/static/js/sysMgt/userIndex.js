$(document).ready(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
	initBtn();
	initFeePerfGrid();
});

function initBtn(){
	
}

var actulID;
function initFeePerfGrid(){
	$("#feePerf_list").jqGrid({
		url:'/dity/feePerfMgt/srchFeePerfData',
        datatype: "json",
        autowidth: true,
        height: 370,
//        width:1030,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames: ['ID','费率名称', '费率值'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"Center",hidden:true},
            {name: 'RATE_NAME',index: 'RATE_NAME', align :"Center",editable: true,width: '50%'},
            {name: 'RATE_VAL',index: 'RATE_VAL', align :"Center",editable: true,width: '50%'},
        ],
        loadComplete: function (data) {
          　　		console.log(data)
      　　	},
        pager: "#feePerf_list_btn",
        viewrecords: true,
        emptyrecords: "暂无数据",//viewrecords: true时生效
        caption: "费率管理",
        add: true,
        edit: true,
        editurl :'/dity/userMgt',
        hidegrid: false
    });
	
    //设置操作按钮
    $("#feePerf_list").jqGrid('navGrid', '#feePerf_list_btn', {
        edit: true,
        add: true,
        del: false,
        search: false
    }, {
    	// edit: true 修改的
        height: 200,
        reloadAfterSubmit: true,
        checkOnSubmit:true,
   	 	closeAfterEdit:true,
   	 	serializeEditData:function(postData){
   	 		postData = {"postData":JSON.stringify(postData)};
   	 		return postData;
   	 	},
   	 	afterSubmit:function(reqData){
	        var jsonResponse = $.parseJSON(reqData.responseText);
	        if (jsonResponse.O_RUNSTATUS != 1) {
	            return [false, jsonResponse.O_MSG];//提示信息
	        } else {
	            return [true];
	        }
   	 	}
    }, {
    	// add: true 新增的
        height: 200,
        reloadAfterSubmit: true,
        checkOnSubmit:true,
   	 	closeAfterAdd:true,
   	 	serializeEditData:function(postData){
   	 		postData = {"postData":JSON.stringify(postData)};
   	 		return postData;
   	 	},
   	 	afterSubmit:function(reqData){
		 	var jsonResponse = $.parseJSON(reqData.responseText);
		    if (jsonResponse.O_RUNSTATUS != 1) {
		        return [false, jsonResponse.O_MSG];//提示信息
		    } else {
		        return [true];
		    }
   	 	}
    });
    $("#feePerf_list").jqGrid('navButtonAdd', '#feePerf_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-trash", 
    		onClickButton:function(){   
    			var rowid = $("#feePerf_list").getGridParam("selrow");
    			var rowData = $("#feePerf_list").getRowData(rowid);
    			alert("删除这一行" + rowData.ID);
    		}, 
    		position: "last", title:"删除所选数据", cursor: "pointer"}
    );

    //改变列宽
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#feePerf_list').setGridWidth(width);
    });
}