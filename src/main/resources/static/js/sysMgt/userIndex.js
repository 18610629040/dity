$(document).ready(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
	initBtn();
	initFeePerfGrid();
});

function initBtn(){
	//初始化上传插件
	$('#wximgFile').fileupload({
		//上传地址
		url:'/dity/uploadUserImg',
		//返回格式
		dataType: 'json',
		//e：事件对象
		//data：图片上传后的对象，通过data.result.picAddr可以获取上传后的图片地址
		done: function (e, data) {
			$('#wximg').attr('src',data.result.url);
			$('#wxFileName').val(data.result.fileName);
		}
	});
	
	//初始化上传插件
	$('#zfbimgFile').fileupload({
		//上传地址
		url:'/dity/uploadUserImg',
		//返回格式
		dataType: 'json',
		//e：事件对象
		//data：图片上传后的对象，通过data.result.picAddr可以获取上传后的图片地址
		done: function (e, data) {
			$('#zfbimg').attr('src',data.result.url);
			$('#zfbFileName').val(data.result.fileName);
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
		url:'/dity/qryUserList',
        datatype: "json",
        autowidth: true,
        height: 370,
//        width:1030,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames: ['ID','账号', '密码','用户姓名','手机号',
        	'地址', '年龄','生日','账户类型',
        	'银行卡号信息', '微信号','支付宝账号','创建时间',
        	'创建人'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"Center",hidden:true},
            {name: 'USER_NO',index: 'USER_NO', align :"Center",editable: true,width: 120},
            {name: 'PASS',index: 'PASS', align :"Center",editable: true,width: 100},
            {name: 'USER_NAME',index: 'USER_NAME', align :"Center",editable: true,width: 100},
            {name: 'MOBILE_NO',index: 'MOBILE_NO', align :"Center",editable: true,width: 120},
            {name: 'USER_ADD',index: 'USER_ADD', align :"Center",editable: true,width: 140},
            {name: 'USER_AGE',index: 'USER_AGE', align :"Center",editable: true,width: 100},
            {name: 'USER_BIRTH',index: 'USER_BIRTH', align :"Center",editable: true,width: 100},
            {name: 'USER_TYPE',index: 'USER_TYPE', align :"Center",editable: true,width: 100},
            {name: 'BANK_NO',index: 'BANK_NO', align :"Center",editable: true,width: 120},
            {name: 'WX_NO',index: 'WX_NO', align :"Center",editable: true,width: 100},
            {name: 'ZFB_NO',index: 'ZFB_NO', align :"Center",editable: true,width: 100},
            {name: 'CRITE_TIME',index: 'CRITE_TIME', align :"Center",editable: true,width: 120},
            {name: 'CRITE_USER',index: 'CRITE_USER', align :"Center",editable: true,width: 100}
        ],
        loadComplete: function (data) {
          　　		console.log(data)
      　　	},
        pager: "#user_list_btn",
        viewrecords: true,
        emptyrecords: "暂无数据",//viewrecords: true时生效
        caption: "用户列表",
        add: true,
        edit: true,
        editurl :'/dity/userMgt',
        hidegrid: false
    });
	
    //设置操作按钮
    $("#user_list").jqGrid('navGrid', '#user_list_btn', {
        edit: false,
        add: false,
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
    $("#user_list").jqGrid('navButtonAdd', '#user_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-trash", 
    		onClickButton:function(){   
    			var rowid = $("#user_list").getGridParam("selrow");
    			if(rowid != null){
    				var rowData = $("#user_list").getRowData(rowid);
    				$.ajax({
    					type:"post",
    					url:"/dity/delUser",
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
    				$('#USER_NO').val(rowData.USER_NO);
    				$('#PASS').val(rowData.PASS);
    				$('#USER_NAME').val(rowData.USER_NAME);
    				$('#MOBILE_NO').val(rowData.MOBILE_NO);
    				$('#USER_ADD').val(rowData.USER_ADD);
    				$('#USER_AGE').val(rowData.USER_AGE);
    				$('#USER_BIRTH').val(rowData.USER_BIRTH);
    				$("input[name='USER_TYPE'][value='"+rowData.USER_TYPE+"']").prop("checked","checked");
    				$('#BANK_NO').val(rowData.BANK_NO);
    				$('#WX_NO').val(rowData.WX_NO);
    				$('#ZFB_NO').val(rowData.ZFB_NO);
    				$('#wximg').attr("src","/dity/getUserFkmImg?TYPE=WX&USER_NO="+rowData.USER_NO);
    				$('#zfbimg').attr("src","/dity/getUserFkmImg?TYPE=ZFB&USER_NO="+rowData.USER_NO);
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