<%@ page import="java.util.*,org.apache.commons.lang3.StringUtils" contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<s:set var="title">${data.id==null? '添加' : '修改'}</s:set>
<html>
<head>
	<title>权限管理 - ${title}</title>
</head>

<body menuId="8">
    <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
      <div class="main_ctn">
        <div class="author_modify">
        <form method="post" id="mainForm" class="form-horizontal">
		    <div class="control-group">
                <label class="control-label">身份名称</label>
				<div class="controls"><input type="text" name="role_name" value="${data.role_name}" maxlength="20"/></div>
            </div>

          <div class="control-group">
                <label class="control-label">权限配置</label>
				<div class="controls">
          <%
			Map<String, Object> data = (Map<String, Object>)request.getAttribute("data");
			Map<String, List<Map<String, Object>>> menuMap = (Map<String, List<Map<String, Object>>>)data.get("menuMap");
			
			//System.out.println(menuMap);
			
			showMenu(menuMap,"0", out);
			out.println("</dl>");
			%>
		    </div>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
			</div>
          <div class="form-actions">
          	  <input type="hidden" name="role_id" value="${data.id}"/>
          	  <input type="hidden" name="isUpdateMenu" id="isUpdateMenu"/>
	          <input type="button" value="确 定" class="btn btn-success" onclick="save(this)"/>
	          <input type="button" value="取 消" class="btn btn-error" onclick="back()"/>
          </div>
        </form>
        </div>
      </div>
	  </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%!
/**
*显示树形菜单
*/
private void showMenu(Map<String, List<Map<String, Object>>> menuMap, String parentId, JspWriter out) throws Exception{

	List<Map<String, Object>> menuList = menuMap.get(parentId);
	
	for(int i = 0; i<menuList.size(); i++){
		Map<String, Object> menu = menuList.get(i);
		String menuId = menu.get("menu_id").toString();
		String menuName = menu.get("menu_name").toString();
		int web_id = Integer.parseInt(menu.get("web_id").toString());
		int level = Integer.parseInt(menu.get("level").toString());
		String checked = menu.get("checked").toString();
		String childParentId;
		
		if(level==1){
			parentId = "";
			childParentId = menuId;
			
			//System.out.println("-------------");
			if(i>0){
				out.println("</dl>");
			}
			out.println("<dl>");
		}else{
			parentId = menu.get("parent_id").toString();
			childParentId = parentId + "," + menuId;
		}
		
// 		if(web_id == 2){
// 			menuName = menuName + "(代理商平台)";
// 		}
// 		if(web_id == 3){
// 			menuName = menuName + "(运营平台)";
// 		}
		
		out.print("<dd>");
		for(int j=1; j<level; j++){
			//System.out.print("\t");
			out.print("<span></span>");
		}
		//System.out.println(parentId+"--"+menu);
		out.println("<label class='checkbox_label'><input type='checkbox' id='menu_"+menuId+"' name='menu_id' value='"+menuId+"' parentId='"+parentId+"' "+ checked +" onclick='clickMenu(this)'/>"+menuName+"</label>");
		if(menuMap.containsKey(childParentId)){//存在子菜单，则递归显示
			showMenu(menuMap, childParentId, out);
		}
		out.print("</dd>");
	}
}
%>

<script type="text/javascript">
var validate;
var selectMenu;

$(function(){
	
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			role_name: "required"
		},
		messages: {
			role_name: "请输入管理身份"
		}
	});
	
	selectMenu = $(":checkbox").serialize();
});

function webIdOnChange(value, text, isInit){
	if(isInit){// 初始化的时候直接返回
		return;
	}
	
	var web_id = $("#web_id").val();
	if(web_id == 9999){
		$('#web_id-error').html("请选择web应用");
		$('#web_id-error').show();
	}else{
		//重新请求页面
		var role_name = $("input[name='role_name']").val();
		var dataId = '${data.id}';
		if(dataId == ""){
			location.href = "${ctx}/authority/add?role_name=" + encodeURI(encodeURI(role_name))+"&web_id="+web_id;
		}else{
			var id = $("input[name='role_id']").val();
			location.href = "${ctx}/authority/edit?id=" + id+"&web_id="+web_id+"&role_name=" + encodeURI(encodeURI(role_name));
		}
	}
}


//保存
function save(btn){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	

	var newSelectMenu = $(":checkbox").serialize();
	$("#isUpdateMenu").val(selectMenu != newSelectMenu); //菜单是否已修改
	selectMenu = newSelectMenu;
	
	var options = {
		beforeSubmit : function() {
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			
			if(data.result==null){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/authority/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}

//点击一个菜单
function clickMenu(menu){
	var value = $(menu).val();
	var checked = $(menu).is(":checked");
	var parentId = $(menu).attr("parentId");
	
	if(parentId != ""){
		var parentIdArray = parentId.split(",").reverse();
		
		for(var i=0; i<parentIdArray.length; i++){
			var box = $(":checkbox#menu_"+parentIdArray[i]); //父菜单
			
			if(checked){
				box.attr("checked", true).prop("checked", true);
				box.parent("span").addClass("checked");
				
			}else{
				/* 取消菜单不影响父菜单的选中，即一个菜单可以没有子菜单
				var childParentId = box.val();
				if(box.attr("parentId")!=""){
					childParentId = box.attr("parentId")+"," + childParentId;
				}
				var child = $(":checkbox[parentId='"+childParentId+"']:checked"); //子菜单
				
				if(child.length==0){
					box.attr("checked", false).prop("checked", false);
					box.parent("span").removeClass("checked");
				}
				 */
			}
		}
	}
	
	var childParentId = value;
	if(parentId!=""){
		childParentId = parentId+"," + childParentId;
	}
	
	var box2 = $(":checkbox[parentId='" +childParentId+ "'], :checkbox[parentId^='" +childParentId+ ",']");
	
	box2.attr("checked", checked).prop("checked", checked);//选中或取消所有子菜单
	if(checked) {
		box2.parent("span").addClass("checked");
	}else{
		box2.parent("span").removeClass("checked");
	}
}
</script>
</body>
</html>