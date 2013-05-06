package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cas.iie.idp.user.SamlConfigRole;
import org.cas.iie.idp.user.Configs;

import com.google.gson.Gson;

public class samlconfigServlet extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if(action != null){
			if(action.equals("getallconfigs")){
				SamlConfigRole config = Configs.getthissamlconfig();
				Gson json = new Gson();
				String returnjson = json.toJson(config);
				response.getWriter().print(returnjson);
			}else if(action.equals("modifyconfig")){
				SamlConfigRole config = Configs.getthissamlconfig();
				String SAML_NOT_BEFORE = request.getParameter("SAML_NOT_BEFORE");
				String SAML_NOT_AFTER = request.getParameter("SAML_NOT_AFTER");
				String samlavailabletime = request.getParameter("samlavailabletime");
				if(samlavailabletime != null){
					config.setSAML_NOT_BEFORE(Integer.parseInt(samlavailabletime));
					config.setSAML_NOT_AFTER(Integer.parseInt(samlavailabletime));
				}
				boolean result = Configs.saveconfig(config);
				response.getWriter().print(result);
			}
		}
	}
}
