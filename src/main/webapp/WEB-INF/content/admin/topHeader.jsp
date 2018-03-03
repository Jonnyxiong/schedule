<%@ page contentType="text/html;charset=UTF-8" %>
<!--Header-part-->
<div id="header">
  <h1><a href="${ctx}/index">云验证系统</a></h1>
</div>
<!--close-Header-part--> 
<!--top-Header-menu-->
<div id="user-nav" class="navbar navbar-inverse">
  <ul class="nav">
    <li  class="dropdown" id="profile-messages" ><a title="" href="#" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle"><i class="fa fa-user"></i>  <span class="text">欢迎光临，${login_user_realName}</span><b class="caret"></b></a>
      <ul class="dropdown-menu">
        <li><a href="${ctx}/admin/view"><i class="fa fa-user"></i> 个人资料</a></li>
        <li class="divider"></li>
        <li><a href="${ctx}/admin/editPassword"><i class="fa fa-edit"></i> 修改密码</a></li>
      </ul>
    </li>
    <li class="logout"><a title="" href="${ctx}/logout"><i class="fa fa-power-off"></i> <span class="text">退出</span></a></li>
  </ul>
</div>
<!--close-top-Header-menu-->
