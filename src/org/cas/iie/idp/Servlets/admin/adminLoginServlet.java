package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class adminLoginServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(username == null || password == null){
			
		}else{
			if(username.equals("admin")&& password.equals("admin")){
				setSession(username,request);
				setCookies(username,response);
				String rediredtUrl = "list.html";
				response.sendRedirect(rediredtUrl);

			}
		}
	}
	
	private boolean setSession(String username,HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			session = request.getSession();
		}
		session.setAttribute("username", username);
		session.setAttribute("admin", "true");
		return true;
	}
	private boolean setCookies(String username,HttpServletResponse response){
		Cookie cookies = new Cookie("adminname", username);
		cookies.setMaxAge(24*60*60);
		response.addCookie(cookies);
		return true;
	}
}