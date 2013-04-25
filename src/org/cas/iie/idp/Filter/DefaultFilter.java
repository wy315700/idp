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

public class DefaultFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("aaaa");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		System.out.println("拦截 URI="+request.getRequestURL());
		String servername = request.getServerName();
		String tenant = servername.substring(0, servername.indexOf("."));
		if(!tenant.equals("127")){
			LDAPhelper.domain = "o="+tenant;
		}else{
			LDAPhelper.domain = null;
		}
			
		System.out.println("拦截 tenant="+tenant);

		/*
		HttpSession session = request.getSession(false);
		String requestUrl = request.getRequestURL().toString().toLowerCase();
		if(requestUrl.endsWith("/login.html")
				&&requestUrl.endsWith("*.js")
				&&requestUrl.endsWith("*.png")
				&&requestUrl.endsWith("*.jpg")
				&&requestUrl.endsWith("*.")
				&&requestUrl.endsWith("*.css")){

		}
		else{
			if(session == null && !requestUrl.endsWith("/login")){
				response.sendRedirect("/login.html");
				return;
			}
		}
		*/
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		System.out.println("Filter 结束");
	}
}
