<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/fcf/FusionCharts.js"></script>
<script type="text/javascript">
function edit(index){
 	$('#list_table').datagrid('selectRow',index);// 关键在这里 
	var selectedRow = $('#list_table').datagrid('getSelected');
	id = selectedRow.id; 
	
    $.messager.confirm('确认','确认重置密码?',function(){  
         $.ajax({  
         url:'${ctx}/setDefaultPwd?userId='+id,
         type : 'GET',  
         
         success:function(msg){
				if(msg.success){
					$('#list_table').datagrid('reload');
					alert('重置成功');
				    }else{
					  alert("重置失败");
				    }
         },
         error:function(data){
	           	if(data.status == 405){
	           		alert("用户没有操作该功能的权限。。。");
	           	}else{
	           		alert("数据加载失败");
	                   hideLoad();
	           	}
		 }
         });  
         
         
    })
}
function todomain(index){
   alert("改功能代码已删除。。。。");	
}
/*显示表格*/
function showDataGrid(){
	var editRow = undefined;
	typeList=[{ "value": "cdn", "text": "cdn" }, { "value": "cloud", "text": "cloud" }];
	var email = $("#email").val();
	$("#list_table").datagrid({
		url: "${ctx}/userEditData?email="+email,
		height: 480,
		title: '用户管理',
		loadMsg:'数据加载中...',
		fitColumns: true,
		singleSelect: true,
		pagination: true,
		pageNumber:1,
		pageList : [10,15,20,30,40,50],
		pageSize: 15,
		striped: true,
		idField: 'id',
		columns:[[
			{field:'id',title:'ID',width:100,align:'center' },      
	        {field:'username',title:'用户名',width:100,align:'center',editor: { type: 'text', options: { required: true } } },
	        {field:'email',title:'邮箱',width:100,align:'center' }, 
	        {field:'type',title:'用户类型',width:80,align:'center',
	        	editor : {
				type : 'combobox',
				options : {
					data : typeList,
					valueField : 'value',
					textField : 'text',
					required : true,
					editable:false 
				}
			}},
			{field:'defaultpwd',title:'初始密码',width:100,align:'center' },
	        {field:'opt',title:'操作',width:100,align:'center',  
                formatter:function(value,index,row){
                	if(''!=value){
                    return '| <a href="javascript:void(0)" onclick= "javascript:edit ('+  row  +') ">重置密码</a> | ' + '<a href="javascript:void(0)" onclick= "javascript:todomain ('+  row  +') ">关联域名</a> |'; 
                	}
                }
	        }
        ]],
		onLoadError:function(error) {
			alert("查询错误");
		},
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
                if (editRow != undefined) {
                    $("#list_table").datagrid('endEdit', editRow);
                }
                if (editRow == undefined) {
                    $("#list_table").datagrid('insertRow', {
                        index: 0,
                        row: {}
                    });
                    $("#list_table").datagrid('beginEdit', 0);
                    editRow = 0;
                }
            }
        }, '-', {
            text: '保存', iconCls: 'icon-save', handler: function () {
                $("#list_table").datagrid('endEdit', editRow);
                //如果调用acceptChanges(),使用getChanges()则获取不到编辑和新增的数据。
                //使用JSON序列化datarow对象，发送到后台。
                var rows = $("#list_table").datagrid('getChanges');
                var rowstr = JSON.stringify(rows);
                 $.ajax({
                    type: 'POST',
                    url: '${ctx}/savaUser?rowstr='+rowstr,
                   dataType: 'json',
                   success:function(msg){
       				if(msg.success){
       					$('#list_table').datagrid('reload');
       					alert("保存成功");
  				    }else{
  					  alert("保存失败");
  				    }
                	   
                   },
                   error:function(data){
     		           	if(data.status == 405){
     		           		alert("用户没有操作该功能的权限。。。");
     		           		$('#list_table').datagrid('reload');
     		           	}else{
     		           		alert("数据加载失败");
     		                   hideLoad();
     		           	}
     			 }
               }); 
            }
        }, '-', {
            text: '撤销', iconCls: 'icon-redo', handler: function () {
                editRow = undefined;
                $("#list_table").datagrid('rejectChanges');
                $("#list_table").datagrid('unselectAll');
            }
        }, '-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
                if (editRow == undefined){return}
                $("#list_table").datagrid('endEdit', editRow);
                $.messager.confirm('确认','确认删除?',function(editRow){  
                if(editRow){  
                var selectedRow = $('#list_table').datagrid('getSelected');  //获取选中行  
                 $.ajax({  
                 url:'${ctx}/delUser?userId='+selectedRow.id,    
                 success:function(msg){
        				if(msg.success){
        					alert('删除成功');
        					$('#list_table').datagrid('reload');
      				    }else{
      					  alert("删除失败");
      				    }
                	  },
                	  error:function(data){
      		           	if(data.status == 405){
      		           		alert("用户没有操作该功能的权限。。。");
      		           	}else{
      		           		alert("数据加载失败");
      		                   hideLoad();
      		           	}
      			 }
                 });  
                 }
                 
                })  
            }
        }, '-', {
            text: '修改', iconCls: 'icon-edit', handler: function () {
                var row = $("#list_table").datagrid('getSelected');
                if (row !=null) {
                    if (editRow != undefined) {
                        $("#list_table").datagrid('endEdit', editRow);
                    }
                    if (editRow == undefined) {
                        var index = $("#list_table").datagrid('getRowIndex', row);
                        $("#list_table").datagrid('beginEdit', index);
                        editRow = index;
                        $("#list_table").datagrid('unselectAll');
                    }
                } else {
                }
            }
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#list_table").datagrid('endEdit', editRow);
            }
            if (editRow == undefined) {
                $("#list_table").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
		onClickRow:function(rowIndex,rowData){
            if (editRow != undefined) {
                $("#list_table").datagrid('endEdit', editRow);
            }
        }
	}); 

}


function relaod() {
	$.ajax({
		type : "GET",
		url : "${ctx}/reloadPrivilege",
		beforeSend : function() {
			$("#tipMsg").text("数据更新中,请稍后...");
		},
		success : function(msg) {
			if (msg.failed) {
				$("#tipMsg").text("数据更新失败");
			} else {
				$("#tipMsg").text("数据更新成功");
				location.reload(true);
			}
		}
	});
}

$(document).ready(function() {
	
	showDataGrid();

});

//按邮箱名进行搜索
function searchFunc() {
	var email = $("#email").val();
	if(email==""){
		alert("请输入要查询的邮箱账号名关键字。。。");
		return ;
	}
	showDataGrid();
	}
	
//点击清空按钮出发事件
function clearSearch() {
$("#email").val("");//找到form表单下的所有input标签并清空
}

</script>
</head>
<body>
<div class="easyui-panel ms-panel-outer" title="查询条件" data-options="collapsible:true">
  <table width="100%" border="0" cellpadding="3" cellspacing="2">
    <tr>
      <td class="item_name">邮箱账号关键字:</td>
      
      <td class="item_value span3" colspan="3">
          <input id="email" name="email" /><span id="tipMsg"></span>
        <a id="btn_addCodeType" href="javascript:void(0)" class="easyui-linkbutton" onClick="searchFunc();" data-options="iconCls:'icon-search'">查找</a> 
        <a id="btn_deleteCodeType" href="javascript:void(0)" class="easyui-linkbutton" onClick="clearSearch();" data-options="iconCls:'icon-remove'">清空</a> 
        <a id="btn_editCodeType" href="javascript:void(0)" class="easyui-linkbutton" onClick="relaod();" data-options="iconCls:'icon-edit'">更新用户信息</a></td>
    </tr>
  </table>
</div>
<div fit="true" border="false">
	<div data-options="region:'center',split:false">
		<table id="dg"></table>
		<div>
			<table id="list_table"></table>
		</div>
	</div>
</div>
</body>
</html>