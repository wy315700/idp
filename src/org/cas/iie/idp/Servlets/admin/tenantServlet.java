package org.cas.iie.idp.Servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cas.iie.idp.admin.groupAdmin;
import org.cas.iie.idp.admin.tenantAdmin;
import org.cas.iie.idp.user.GroupRole;
import org.cas.iie.idp.user.TenantRole;

import com.google.gson.Gson;

public class tenantServlet extends HttpServlet{
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if(action != null){
			tenantAdmin tenantadmin = new tenantAdmin();
			if(action.equals("getalltenants")){
				List<TenantRole> tenants = tenantadmin.getAllTenant();
				Gson json = new Gson();
				String returnTenant = json.toJson(tenants);
				response.getWriter().println(returnTenant);

			}else if(action.equals("deltenant")){
				String tenantname = request.getParameter("tenantname");
				if(tenantname == null){
					returnstate(false, response);
				}else{
					TenantRole tenant = new TenantRole();
					tenant.setTenantname(tenantname);
					boolean result = tenantadmin.delTenant(tenant);
					returnstate(result,response);
				}
			}else if(action.equals("addtenant")){
				String tenantname     = request.getParameter("tenantname");
				String tenantadminname    = request.getParameter("tenantadmin");
				String tenantpassword = request.getParameter("tenantpassword");

				if(tenantname == null || tenantadminname==null ||tenantpassword == null){
					returnstate(false, response);
				}else{
					TenantRole tenant = new TenantRole();
					tenant.setTenantname(tenantname);
					tenant.setTenanturl(tenantname);
					boolean result = tenantadmin.addTenant(tenant);
					returnstate(result,response);
				}

			}
		}
	}
	public void returnstate(boolean result,HttpServletResponse response) throws IOException{
		if(result == true){
			response.getWriter().print("true");
		}else{
			response.getWriter().print("false");
		}
	}
}
