package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cas.iie.idp.admin.groupAdmin;
import org.cas.iie.idp.admin.userAdmin;
import org.cas.iie.idp.user.GroupRole;
import org.cas.iie.idp.user.UserRole;

import com.google.gson.Gson;

public class userServlet extends HttpServlet{
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action != null){
			if(action.equals("getallusers")){
				userAdmin useradmin = new userAdmin();
				List<UserRole> users = useradmin.getAllUsers(0, 0);
				Gson json = new Gson();
				String returnGroup = json.toJson(users);
				useradmin.close();
				response.setCharacterEncoding("utf-8");
				response.getWriter().println(returnGroup);
			}else if(action.equals("adduser")){
				String username = request.getParameter("username");
				String realname = request.getParameter("realname");
				String password = request.getParameter("password");
				if(username != null && realname != null && password != null){
					UserRole user = new UserRole();
					user.setUsername(username);
					user.setRealname(realname);
					user.setPassword(password);
					userAdmin useradmin = new userAdmin();
					boolean result = useradmin.addUser(user);
					response.getWriter().print(result);
				}
			}else if(action.equals("deluser")){
				String username = request.getParameter("username");
				if(username != null){
					userAdmin useradmin = new userAdmin();
					boolean result = useradmin.deleteuser(username);
					useradmin.close();
					response.getWriter().print(result);
				}
			}
		}
		
	}
}
