$(document).ready(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
	initBtn();
	initFeePerfGrid();
});

function initBtn(){
	$("#sData").click(function(){
		alert(123);
	});
}

function initFeePerfGrid(){
	$("#prdtLbMgt_list").jqGrid({
		url:'/dity/feePerfMgt/srchPrdtLbData',
        datatype: "json",
        autowidth: true,
        height: 370,
//        width:1030,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames: ['ID','排序', '类型名称', '操作人', '操作时间'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"Center",hidden:true},
            {name: 'TYPE_ORDER',index: 'TYPE_ORDER', align :"Center",editable: true,width: '30%'},
            {name: 'TYPE_NAME',index: 'TYPE_NAME', align :"Center",editable: true,width: '30%'},
            {name: 'CRITE_USER',index: 'CRITE_USER', align :"Center",editable: false,width: '20%'},
            {name: 'CRITETIME',index: 'CRITETIME', align :"Center",editable: false,width: '20%'},
        ],
        loadComplete: function (data) {
          　　console.log(data)
      　　 },
        pager: "#prdtLbMgt_list_btn",
        viewrecords: true,
        emptyrecords: "暂无数据",//viewrecords: true时生效
        caption: "商品类别维护管理",
        add: true,
        edit: true,
        editurl :'/dity/feePerfMgt/optPrdtLbData',
        addtext: 'Add',
        edittext: 'Edit',
        hidegrid: false
    });
	
    //设置操作按钮
    $("#prdtLbMgt_list").jqGrid('navGrid', '#prdtLbMgt_list_btn', {
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
    //自定义删除按钮
    $("#prdtLbMgt_list").jqGrid('navButtonAdd', '#prdtLbMgt_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-trash", 
    		onClickButton:function(){   
    			var rowid = $("#prdtLbMgt_list").getGridParam("selrow");
				var rowData = $("#prdtLbMgt_list").getRowData(rowid);
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
	    			        url: '/dity/feePerfMgt/optPrdtLbData',
	    			        dataType:"json", 
	    			        success:function(data){
	    			        	parent.layer.alert(data.O_MSG);
	    			           if((data.O_MSG).indexOf('成功') != -1){
	    			        	   $("#prdtLbMgt_list").trigger("reloadGrid");
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
    );

    //改变列宽
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#prdtLbMgt_list').setGridWidth(width);
    });
}