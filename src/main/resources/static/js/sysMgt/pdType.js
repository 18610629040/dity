$(document).ready(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
	initBtn();
	initFeePerfGrid();
});

function initBtn(){
	//初始化上传插件
	$('#qyimgFile').fileupload({
		//上传地址
		url:'/dity/uploadQyImg',
		//返回格式
		dataType: 'json',
		add: function(e, data) {
			var acceptFileTypes = /(\.|\/)(gif|jpe?g|png)$/i;
			if(data.originalFiles[0]['type'].length && !acceptFileTypes.test(data.originalFiles[0]['type'])) {
				alert('请上传图片格式文件！')
			} else {
				data.submit();
			}
		},
		//e：事件对象
		//data：图片上传后的对象，通过data.result.picAddr可以获取上传后的图片地址
		done: function (e, data) {
			if(data.result.O_RUNSTATUS == -1){
				alert(data.result.O_MSG);
			}else{
				$('#qyimg').attr('src',data.result.url);
				$('#FILE_URL').val(data.result.url);
			}
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
	
	$("#updtStat").click(function(){
		var rowid = $("#user_list").getGridParam("selrow");
		var rowData = $("#user_list").getRowData(rowid);
		if(rowid == null){
			parent.layer.msg('请选择数据！', {shift: 6});
		}else{
			var statNm = rowData.STATUS_NAME;
			var lbNm = rowData.TYPE_NAME;
			parent.layer.confirm('【'+lbNm+'】当前状态为【'+statNm+'】，确定更改开放状态？', {
				btn: ['确定','取消'],
			    shade: false //不显示遮罩
			}, function(){
				var postData = {ID:rowData.ID, oper:'updtStat'};
				//更改开放状态
				$.ajax({
			        type:'POST',
			        data:{postData:JSON.stringify(postData)},
			        url: '/dity/feePerfMgt/optPrdtLbData',
			        dataType:"json", 
			        success:function(data){
			        	parent.layer.alert(data.O_MSG);
    			        if((data.O_MSG).indexOf('成功') != -1){
    			        	$("#user_list").trigger("reloadGrid");
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

function initFeePerfGrid(){
	$("#user_list").jqGrid({
		url:'/dity/feePerfMgt/srchPrdtLbData',
        datatype: "json",
        autowidth: true,
        height: 520,
//        width:1030,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames: ['ID','排序', '类型名称','STATUS','是否开放','FILE_URL', '操作人', '操作时间'],
        colModel: [
        	{name: 'ID',index: 'ID',editable: true, align :"Center",hidden:true},
            {name: 'TYPE_ORDER',index: 'TYPE_ORDER', align :"Center",editable: true,width: '30%'},
            {name: 'TYPE_NAME',index: 'TYPE_NAME', align :"Center",editable: true,width: '30%'},
            {name: 'STATUS',index: 'STATUS',editable: true, align :"Center",hidden:true},
            {name: 'STATUS_NAME',index: 'STATUS_NAME', align :"Center",editable: true,width: '30%'},
            {name: 'FILE_URL',index: 'FILE_URL',editable: true, align :"Center",hidden:true},
            {name: 'USER_NAME',index: 'USER_NAME', align :"Center",editable: false,width: '20%'},
            {name: 'CRITETIME',index: 'CRITETIME', align :"Center",editable: false,width: '20%'},
        ],
        loadComplete: function (data) {
          　　		console.log(data)
      　　	},
        pager: "#user_list_btn",
        viewrecords: true,
        emptyrecords: "暂无数据",//viewrecords: true时生效
        caption: "商品类别维护管理",
        add: true,
        edit: true,
        editurl :'/dity/feePerfMgt/addQy',
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
				var rowData = $("#user_list").getRowData(rowid);
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
		    			        	$("#user_list").trigger("reloadGrid");
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
    $("#user_list").jqGrid('navButtonAdd', '#user_list_btn',
    		{caption:"", 
    		buttonicon:"glyphicon glyphicon-edit", 
    		onClickButton:function(){   
    			var rowid = $("#user_list").getGridParam("selrow");
    			if(rowid != null){
    				var rowData = $("#user_list").getRowData(rowid);
    				$('#ID').val(rowData.ID);
    				$('#TYPE_ORDER').val(rowData.TYPE_ORDER);
    				$('#TYPE_NAME').val(rowData.TYPE_NAME);
    				$("input[name='STATUS'][value='"+rowData.STATUS+"']").prop("checked","checked");
    				$('#qyimg').attr("src",rowData.FILE_URL);
    				$('#FILE_URL').val(rowData.FILE_URL);
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
    		position: "first", title:"新增商品类别", cursor: "pointer"}
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