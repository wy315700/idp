package org.cas.iie.idp.Servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.saml.SAMLrequest;

public class SpServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String samlresponse = request.getParameter("SAMLResponse");
		if(samlresponse == null){
			SAMLrequest samlrequest = new SAMLrequest();
			String samlrequri = samlrequest.createRequest();
			String rediredtUrl = "http://192.168.112.122:8080/sso?SAMLrequest="+samlrequri;
			response.sendRedirect(rediredtUrl);
		}
		else{
			response.getWriter().println(samlresponse);
			
		}
	}
}
