$(document).ready(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
	initBtn();
	initFeePerfGrid();
});

function initBtn(){
	
}

var actulID;
function initFeePerfGrid(){
	$("#pdOrder_list").jqGrid({
		url:'/dity/feePerfMgt/srchPdOrderData',
        datatype: "json",
        autowidth: true,
        height: 370,
//        width:1030,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames: ['ID','订单号', '产品名称', '订单价格', '订单小计', '订单费率', '计算收益', '快递单号', '下单人', '订单状态', '操作人', '操作时间'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"center",hidden:true},
            {name: 'ORDER_NO',index: 'ORDER_NO', align :"center",editable: true,width: '30%'},
            {name: 'ORDER_PDNM',index: 'ORDER_PDNM', align :"center",editable: true,width: '30%'},
            {name: 'ORDER_PRICE',index: 'ORDER_PRICE', align :"center",editable: true,width: '30%'},
            {name: 'ORDER_CVAL',index: 'ORDER_CVAL', align :"center",editable: true,width: '30%'},
            {name: 'ORDER_CFEE',index: 'ORDER_CFEE', align :"center",editable: true,width: '30%'},
            {name: 'ORDER_INCOM',index: 'ORDER_INCOM', align :"center",editable: true,width: '30%'},
            {name: 'ORDER_EXPRESS',index: 'ORDER_EXPRESS', align :"center",editable: true,width: '30%'},
            {name: 'ORDER_USER',index: 'ORDER_USER', align :"center",editable: true,width: '30%'},
            {name: 'STATUS',index: 'STATUS', align :"center",editable: true,width: '30%'},
            {name: 'CRITE_USER',index: 'CRITE_USER', align :"Center",editable: false,width: '20%'},
            {name: 'CRITETIME',index: 'CRITETIME', align :"Center",editable: false,width: '20%'},
        ],
        pager: "#pdOrder_list_btn",
        viewrecords: true,
        emptyrecords: "暂无数据",//viewrecords: true时生效
        caption: "订单管理",
        add: true,
        edit: true,
        editurl :'/dity/feePerfMgt/optPdOrderData',
        hidegrid: false
    });
	
    //设置操作按钮
    $("#pdOrder_list").jqGrid('navGrid', '#pdOrder_list_btn', {
        edit: true,
        add: true,
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
    //自定义删除按钮
    $("#pdOrder_list").jqGrid('navButtonAdd', '#pdOrder_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-trash", 
    		onClickButton:function(){   
    			var rowid = $("#pdOrder_list").getGridParam("selrow");
				var rowData = $("#pdOrder_list").getRowData(rowid);
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
	    			        	   $("#pdOrder_list").trigger("reloadGrid");
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
        $('#pdOrder_list').setGridWidth(width);
    });
}