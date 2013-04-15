package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cas.iie.idp.admin.groupAdmin;
import org.cas.iie.idp.user.GroupRole;
import com.google.gson.Gson;

public class groupServlet extends HttpServlet{
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if(action != null){
			if(action.equals("getallgroups")){
				groupAdmin groupadmin = new groupAdmin();
				List<GroupRole> groups = groupadmin.getAllGroups(0, 0);
				Gson json = new Gson();
				String returnGroup = json.toJson(groups);
				response.getWriter().println(returnGroup);
			}else if(action.equals("addgroup")){
				String groupname = request.getParameter("groupname");
				if(groupname != null){
					GroupRole group = new GroupRole();
					group.setGroupname(groupname);
					groupAdmin groupadmin = new groupAdmin();
					boolean result = groupadmin.addGroup(group);
					response.getWriter().print(result);
				}
			}else if(action.equals("delgroup")){
				String groupname = request.getParameter("groupname");
				if(groupname != null){
					groupAdmin groupadmin = new groupAdmin();
					boolean result = groupadmin.deleteGroup(groupname);
					response.getWriter().print(result);
				}
			}
		}
		
	}
}
