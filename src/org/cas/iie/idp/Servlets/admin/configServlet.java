package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cas.iie.idp.user.ConfigRole;
import org.cas.iie.idp.user.Configs;

import com.google.gson.Gson;

public class configServlet extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if(action != null){
			if(action.equals("getallconfigs")){
				ConfigRole config = Configs.getthisconfig();
				Gson json = new Gson();
				String returnjson = json.toJson(config);
				response.getWriter().print(returnjson);
			}
		}
	}
}
