package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cas.iie.idp.authenticate.Impl.AdminAuthenticate;

public class adminLoginServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(username == null || password == null){
			returnerror(response);
		}else{
			if(getAuthenticateState(username,password)){
				setSession(username,password,request);
				setCookies(username,response);
				String rediredtUrl = "list.html";
				response.sendRedirect(rediredtUrl);
			}else{
				returnerror(response);
			}
		}
	}
	private void returnerror(HttpServletResponse response) throws IOException{
		String rediredtUrl = "login.html";
		response.sendRedirect(rediredtUrl);
	}
	private boolean getAuthenticateState(String username,String password){
		AdminAuthenticate adminauth = new AdminAuthenticate(username, password);
		return adminauth.doAuthenticate();
	}
	private boolean setSession(String username,String password,HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			session = request.getSession();
		}
		session.setAttribute("username", username);
		session.setAttribute("admin", "true");
		session.setAttribute("password", password);
		return true;
	}
	private boolean setCookies(String username,HttpServletResponse response){
		Cookie cookies = new Cookie("adminname", username);
		cookies.setMaxAge(24*60*60);
		response.addCookie(cookies);
		return true;
	}
}