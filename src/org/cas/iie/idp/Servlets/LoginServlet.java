package org.cas.iie.idp.Servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;

import org.cas.iie.idp.authenticate.IGetUser;
import org.cas.iie.idp.authenticate.Impl.GetUserByLdap;
import org.cas.iie.idp.authenticate.Impl.GetUserImpl;
import org.cas.iie.idp.authenticate.Impl.UserAuthenticate;
import org.cas.iie.idp.user.UserRole;
import org.opensaml.common.SAMLException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.xml.sax.SAXException;

import cc.saml.SAMLProtocol;
import cc.saml.SAMLdecode;
import cc.saml.SAMLrequest;

import com.google.gson.Gson;

public class LoginServlet extends HttpServlet{
	HttpSession session;
	private String IssueInstant = "test";
	private String ProviderName = "test";
	private String AssertionConsumerServiceURL = "test";
	private String ID = "test";
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		System.out.println(request.getParameter("aa"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String samlRequest = request.getParameter("SAMLrequest");
		boolean needToAuthn = false;
		if(username == null ||password == null|| samlRequest == null){
			needToAuthn = false;
		}else{
			needToAuthn = true;
		}
		
		if(needToAuthn){
			authenticateUser(username,password, request);
		}
	}
	
	private String generateJson(String[] keys,String[] values){
		Gson gson = new Gson();
		Map<String,String> userObj = new HashMap<String, String>();
		//userObj.put("username", username);
		//userObj.put("islogin", "true");
		if(keys.length != values.length){
			return null;
		}
		for(int i = 0 ; i < keys.length ;i++ ){
			userObj.put(keys[i],values[i]);
		}
		return gson.toJson(userObj);
	}
	private boolean setSession(String username,HttpServletRequest request){
		IGetUser getuser = new GetUserByLdap();
		UserRole user = getuser.getUserByName(username, true);
		if(user != null){
			if(session == null){
				session = request.getSession();
			}
			session.setAttribute("uid",new Integer(user.getUserID()));
			return true;
		}
		else{
			return false;
		}
	}
	private boolean authenticateUser(String username,String password,HttpServletRequest request)
	{
		if(username == null ||password == null){
			return false;
		}

		UserAuthenticate uAuth = new UserAuthenticate(username, password);
		return uAuth.doAuthenticate();
	}
}
