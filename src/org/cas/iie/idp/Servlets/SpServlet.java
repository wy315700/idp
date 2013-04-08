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
	
	
	private static String ISSUER = "http://sp.example.org";
	private static String PROVIDE_NAME = "sp.example.org";
	private static String ACS_URL = "http://192.168.112.122:8080/sp";
	
	private static String IDP_URL = "http://192.168.112.122:8080/sso";
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session == null ||session.getAttribute("username") == null){
			String samlresponse = request.getParameter("SAMLResponse");
			if(samlresponse == null){
				
				String samlrequest = makeRequest();
				String rediredtUrl = IDP_URL+"?SAMLrequest="+samlrequest;
				response.sendRedirect(rediredtUrl);
			}
			else{
				response.getWriter().println(samlresponse);
			}
		}
		else{
			
			response.getWriter().println(session.getAttribute("username"));
		}
		
	}
	
	private String makeRequest(){
		SAMLrequest requesthandle = new SAMLrequest(ISSUER, PROVIDE_NAME, ACS_URL);
		String samlrequest = requesthandle.generateAuthnRequest();
		return samlrequest;
		
	}
}
