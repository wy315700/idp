package org.cas.iie.idp.Servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.saml.SAMLrequest;

public class ssoServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session == null){
			RequestDispatcher rq = request.getRequestDispatcher("/login.html");
			rq.forward(request, response);
		}
		else{
			RequestDispatcher rq = request.getRequestDispatcher("/login");
			rq.forward(request, response);
		}
	}

}
