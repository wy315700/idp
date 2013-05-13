package org.cas.iie.idp.Filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.Configs;

public class AdminFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		System.out.println("拦截 URI="+request.getRequestURL());
		String servername = request.getServerName();
		String tenant = servername.substring(0, servername.indexOf("."));
		String requestUrl = request.getRequestURL().toString().toLowerCase();
			

		
		HttpSession session = request.getSession(false);
		
		if(requestUrl.endsWith("/login.html")
				||requestUrl.endsWith(".js")
				||requestUrl.endsWith(".png")
				||requestUrl.endsWith(".jpg")
				||requestUrl.endsWith(".ico")
				||requestUrl.endsWith(".css")){
		}
		else{
			if(session == null && !requestUrl.endsWith("/login")){
				response.sendRedirect("login.html");
				return;
			}
		}
		
		chain.doFilter(req, res);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}


}
