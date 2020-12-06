$(document).ready(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
	initBtn();
	initFeePerfGrid();
});

function initBtn(){
	$("#qrwtBtn").click(function(){
		var rowid = $("#pdOrder_list").getGridParam("selrow");
		var rowData = $("#pdOrder_list").getRowData(rowid);
		if(rowid == null){
			parent.layer.msg('请选择数据！', {shift: 6});
		}else{
			var saleNm = rowData.ORDER_USER_SEL_NO;
			var buyNm = rowData.ORDER_USER_BUY_NO;
			var pdNm = rowData.ORDER_PD_NAME;
			var orderNo = rowData.ORDER_NO;
			parent.layer.confirm('确定确认买家【'+buyNm+'】卖家【'+saleNm+'】商品【'+pdNm+'】订单号【'+orderNo+'】的委托？', {
				btn: ['确定','取消'],
			    shade: false //不显示遮罩
			}, function(){
				$.ajax({
			        type:'POST',
			        data:{ID:rowData.ID, STATUS:5},
			        url: '/dity/setOrder',
			        dataType:"json", 
			        success:function(data){
			        	parent.layer.alert(data.O_MSG);
			           if((data.O_MSG).indexOf('成功') != -1){
//			        	   $("#pdOrder_list").trigger("reloadGrid");
			        	   window.location.reload();
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
	});
}

var actulID;
function initFeePerfGrid(){
	$("#pdOrder_list").jqGrid({
		url:'/dity/qryOrder?STATUS=4',
        datatype: "json",
        mtype:'post',
        height: 520,
        loadonce:true,//前台分页,页面刷新不走请求了
        rowNum:50, 
		rowList:[10,50,200],   
	    altRows: true, 
	    rownumbers:true,
		multiselect: false,  
		multiboxonly:true,
		sortorder: "asc",     
		autowidth:true,
		shrinkToFit:false,  
		autoScroll: true, //开启水平滚动条
		
        colNames: ['ID','订单号', '产品ID', '产品名称', 'ORDER_PD_TYPE', '是否秒杀商品订单',
        		'订单价格', '预计收益', '委托费用', '订单小计', '快递信息', '卖家','买家','STATUS', '订单状态', '操作人', '操作时间'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"center",hidden:true},
            {name: 'ORDER_NO',index: 'ORDER_NO', align :"center",editable: true,width: 200},
            {name: 'ORDER_PD',index: 'ORDER_PD',editable: true, align :"center",hidden:true},
            {name: 'ORDER_PD_NAME',index: 'ORDER_PD_NAME', align :"center",editable: true,width: 200},
            {name: 'ORDER_PD_TYPE',index: 'ORDER_PD_TYPE',editable: true, align :"center",hidden:true},
            {name: 'ORDER_PD_TYPE_NAME',index: 'ORDER_PD_TYPE_NAME', align :"center",editable: true,width: 200},
            {name: 'ORDER_PRICE',index: 'ORDER_PRICE', align :"center",editable: true,width: 200},
            {name: 'ORDER_CVAL',index: 'ORDER_CVAL', align :"center",editable: true,width: 200},
            {name: 'ORDER_CFEE',index: 'ORDER_CFEE', align :"center",editable: true,width: 200},
            {name: 'ORDER_INCOM',index: 'ORDER_INCOM', align :"center",editable: true,width: 200},
            {name: 'ORDER_EXPRESS',index: 'ORDER_EXPRESS', align :"center",editable: true,width: 200},
            {name: 'ORDER_USER_SEL_NO',index: 'ORDER_USER_SEL_NO', align :"center",editable: true,width: 200},
            {name: 'ORDER_USER_BUY_NO',index: 'ORDER_USER_BUY_NO', align :"center",editable: true,width: 200},
            {name: 'STATUS',index: 'STATUS', align :"center",editable: true,width: '30%',hidden:true},
            {name: 'STATUS_NAME',index: 'STATUS_NAME', align :"center",editable: true,width: 200},
            {name: 'CRITE_USER',index: 'CRITE_USER', align :"Center",editable: false,width: 200},
            {name: 'CRITETIME',index: 'CRITETIME', align :"Center",editable: false,width: 200}
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
        edit: false,
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