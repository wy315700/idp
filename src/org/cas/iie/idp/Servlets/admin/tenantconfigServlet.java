package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cas.iie.idp.admin.groupAdmin;
import org.cas.iie.idp.user.Configs;
import org.cas.iie.idp.user.SamlConfigRole;
import org.cas.iie.idp.user.TenantConfigRole;

import com.google.gson.Gson;

public class tenantconfigServlet extends HttpServlet{
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		Gson json = new Gson();
		if(action != null){
			if(action.equals("getallattrs")){
				TenantConfigRole config = Configs.getthistenantconfig();
				String returnjson = json.toJson(config);
				response.getWriter().print(returnjson);
			}else if(action.equals("getattrdispname")){
				String attr = request.getParameter("attrtype");
				TenantConfigRole config = Configs.getthistenantconfig();
				if(config.getAttributeset().containsKey(attr)){
					String returnjson = json.toJson(config.getAttributeset().get(attr));
					response.getWriter().print(returnjson);
				}
			}else if(action.equals("delattr")){
				String attrtype = request.getParameter("attrtype");
				if(attrtype != null){
					TenantConfigRole config = Configs.getthistenantconfig();
					if(config.getAttributeset().containsKey(attrtype)){
						groupAdmin groupadmin = new groupAdmin(attrtype);
						if(groupadmin.getAllGroups(0, 0).size() != 0){
							response.getWriter().print("false");
						}else{
							config.delAttribute(attrtype);
							Configs.saveconfig(config);
							response.getWriter().print("true");
						}
					}else{
						response.getWriter().print("false");
					}
				}
			}else if(action.equals("addattr")){
				String attrtype = request.getParameter("attrtype");
				String attrdispname = request.getParameter("attrdispname");
				if(attrtype != null && attrdispname != null){
					TenantConfigRole config = Configs.getthistenantconfig();
					if(config.getAttributeset().containsKey(attrtype)){
						response.getWriter().print("false");
					}else{
						config.addAttribute(attrtype,attrdispname);
						boolean resullt = Configs.saveconfig(config);
						response.getWriter().print(resullt);
					}
				}
			}
		}
	}
}