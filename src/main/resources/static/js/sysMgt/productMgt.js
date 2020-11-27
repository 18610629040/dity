$(document).ready(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
	initBtn();
	initFeePerfGrid();
});

function initBtn(){
	//初始化上传插件
	$('#IMAGE').fileupload({
		//上传地址
		url:'/dity/uploadGoodsImg',
		//返回格式
		dataType: 'json',
		//e：事件对象
		//data：图片上传后的对象，通过data.result.picAddr可以获取上传后的图片地址
		done: function (e, data) {
			$('#spimg').attr('src',data.result.url);
			$('#IMAGE_NAME').val(data.result.fileName);
		}
	});
	
	
	$("#user_form").ajaxForm(function (data) {
        if(data.O_RUNSTATUS == 1){
        	window.location.reload();
        }else{
        	alert(data.O_MSG);
        }
    });
	$("#celBtn").click(function (){
		window.location.reload();
	});
	
}

function initFeePerfGrid(){
	$("#user_list").jqGrid({
		url:'/dity/qryGoodsMsList',
        datatype: "json",
        autowidth: true,
        height: 620,
//        width:1030,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames: ['ID','商品名称', '价格','简介','买入账号','开始时间', '截止时间','状态'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"Center",hidden:true},
            {name: 'NAME',index: 'NAME', align :"Center",editable: true,width: 220},
            {name: 'PRICE',index: 'PRICE', align :"Center",editable: true,width: 100},
            {name: 'INTRDCT',index: 'INTRDCT', align :"Center",editable: true,width: 500},
            {name: 'OWN_ACNT',index: 'OWN_ACNT', align :"Center",editable: true,width: 120},
            {name: 'BUY_TIME',index: 'BUY_TIME', align :"Center",editable: true,width: 140},
            {name: 'WT_TIME',index: 'WT_TIME', align :"Center",editable: true,width: 100},
            {name: 'STATUS_NM',index: 'STATUS_NM', align :"Center",editable: true,width: 100}
        ],
        loadComplete: function (data) {
          　　		console.log(data)
      　　	},
        pager: "#user_list_btn",
        viewrecords: true,
        emptyrecords: "暂无数据",//viewrecords: true时生效
        caption: "参与秒杀商品列表",
        add: true,
        edit: true,
        editurl :'/dity/addGoodsMs',
        hidegrid: false
    });
	
    //设置操作按钮
    $("#user_list").jqGrid('navGrid', '#user_list_btn', {
        edit: false,
        add: false,
        del: false,
        search: false
    });
    $("#user_list").jqGrid('navButtonAdd', '#user_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-trash", 
    		onClickButton:function(){   
    			var rowid = $("#user_list").getGridParam("selrow");
    			if(rowid != null){
    				var rowData = $("#user_list").getRowData(rowid);
    				$.ajax({
    					type:"post",
    					url:"/dity/delGoodsMs",
    					data : {ID: rowData.ID},
    					dataType:"json",
    					success:function(data){
    						if(data.O_RUNSTATUS == 1){
    							window.location.reload();
    						}else{
    							alert(data.O_MSG);
    						}
    					},
    					error:function(jqXHR){
    						aler("error："+ jqXHR.status);
    					}
    				});
    			}
    		}, 
    		position: "first", title:"删除所选数据", cursor: "pointer"}
    );
    $("#user_list").jqGrid('navButtonAdd', '#user_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-edit", 
    		onClickButton:function(){   
    			var rowid = $("#user_list").getGridParam("selrow");
    			if(rowid != null){
    				var rowData = $("#user_list").getRowData(rowid);
    				$('#ID').val(rowData.ID);
    				$('#NAME').val(rowData.NAME);
    				$('#PRICE').val(rowData.PRICE);
    				$('#INTRDCT').val(rowData.INTRDCT);
    				$('#BUY_TIME').val(rowData.BUY_TIME);
    				$('#WT_TIME').val(rowData.WT_TIME);
    				$('#spimg').attr("src","/dity/getGoodsImg?ID="+rowData.ID);
    				$(".window").show();
    				$(".cover").show();
    			}
    		}, 
    		position: "first", title:"修改所选数据", cursor: "pointer"}
    );
    $("#user_list").jqGrid('navButtonAdd', '#user_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-plus", 
    		onClickButton:function(){   
    			$(".window").show();
    			$(".cover").show();
    		}, 
    		position: "first", title:"新增用户", cursor: "pointer"}
    );

    //改变列宽
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#user_list').setGridWidth(width);
    });
}

function closeWindow(){
	window.location.reload();
}