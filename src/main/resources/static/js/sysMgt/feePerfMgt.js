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
        autowidth: false,
        height: 270,
        width:800,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 50,
        rowList: [10, 50, 200],
        colNames: ['ID','费率名称', '费率值（%）', '操作人', '操作时间'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"center",hidden:true},
            {name: 'RATE_NAME',index: 'RATE_NAME', align :"center",editable: false,width: '30%'},
            {name: 'RATE_VAL',index: 'RATE_VAL', align :"center",editable: true,width: '30%'},
            {name: 'USER_NAME',index: 'USER_NAME', align :"Center",editable: false,width: '20%'},
            {name: 'CRITETIME',index: 'CRITETIME', align :"Center",editable: false,width: '20%'},
        ],
        pager: "#feePerf_list_btn",
        viewrecords: true,
        emptyrecords: "暂无数据",//viewrecords: true时生效
        caption: "费率管理",
        add: true,
        edit: true,
        editurl :'/dity/feePerfMgt/optFeePerfData',
        hidegrid: false
    });
	
    //设置操作按钮
    $("#feePerf_list").jqGrid('navGrid', '#feePerf_list_btn', {
        edit: true,
        add: false,
        del: false,
        search: false,
        refresh:false
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
    /*//自定义删除按钮
    $("#feePerf_list").jqGrid('navButtonAdd', '#feePerf_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-trash", 
    		onClickButton:function(){   
    			var rowid = $("#feePerf_list").getGridParam("selrow");
				var rowData = $("#feePerf_list").getRowData(rowid);
				if(rowid == null){
					parent.layer.msg('请选择数据！', {shift: 6});
				}else{
					parent.layer.confirm('确定删除所选数据？', {
	    				btn: ['确定','取消'],
	    			    shade: false //不显示遮罩
	    			}, function(){
	    				var postData = {ID:rowData.ID, oper:'del'};
	    				//删除数据
	    				$.ajax({
	    			        type:'POST',
	    			        data:{postData:JSON.stringify(postData)},
	    			        url: '/dity/feePerfMgt/optFeePerfData',
	    			        dataType:"json", 
	    			        success:function(data){
	    			        	parent.layer.alert(data.O_MSG);
	    			           if((data.O_MSG).indexOf('成功') != -1){
	    			        	   $("#feePerf_list").trigger("reloadGrid");
	    			           }
	    			        },
	    			        error:function(jqXHR){
	    			        	parent.layer.alert("服务器发生错误："+ jqXHR.status);
	    			        }
	    				});
	    				
	    			}, function(){
	    			    parent.layer.close();
	    			});
				}
    		}, 
    		position: "last", title:"删除所选数据", cursor: "pointer"}
    );*/

    //改变列宽
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#feePerf_list').setGridWidth(width);
    });
}