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
        	{name: 'ID',index: 'ID', align :"Center",hidden:true},
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
        addtext: 'Add',
        edittext: 'Edit',
        hidegrid: false
    });
	
    //设置操作按钮
    $("#feePerf_list").jqGrid('navGrid', '#feePerf_list_btn', {
        edit: true,
        add: true,
        del: true,
        search: false
    }, {
        height: 200,
        reloadAfterSubmit: true
    });

    //改变列宽
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#feePerf_list').setGridWidth(width);
    });
}