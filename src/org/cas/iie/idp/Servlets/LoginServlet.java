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

import LOG.Logger;
import cc.saml.SAMLdecode;
import cc.saml.SAMLrequest;
import cc.saml.SAMLresponse;

import com.google.gson.Gson;

public class LoginServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		boolean continueLogin = false;
		boolean needToReturn = false;
		String[] samlResponseAttributes = null;
		String username     = request.getParameter("username");
		String password     = request.getParameter("password");
		String samlRequest  = request.getParameter("SAMLrequest");
		if(request.getHeader("Referer") == null){
			continueLogin = false;
		}

		// 如果session 已经存在 就不需要再次登录了
		if(session != null && session.getAttribute("username") != null){
			continueLogin = true;
		}else{
			if(username == null ||password == null){
				continueLogin = false;
			}else{
				continueLogin = authenticateUser(username,password, request);
			}
		}
		
		if(continueLogin == true){
			//setSession(username, request);
			// 判断是正常登陆还是SSO登陆
			if(samlRequest == null || samlRequest.equals("null") || samlRequest.equals("undefined")){
				needToReturn = false;
			}else{
				needToReturn = true;
			}
			try {
				samlResponseAttributes = makeAssertion(samlRequest, username);
				if(samlResponseAttributes == null){
					throw new SAMLException("samlrequest is invalid!");
				}
			} catch (SAMLException e) {
					// TODO Auto-generated catch block
				needToReturn = false;
				Logger.writelog(e);
				e.printStackTrace();
			}
			if(needToReturn == true){
				String samlresponse = samlResponseAttributes[0];
				String acsUrl = samlResponseAttributes[1];
				String[] keys   = {"islogin","action","acsUrl","username","samlResponse"};
				String[] values = {"true","submitresponse",acsUrl,username,samlresponse};
				String returnJsonMessage = generateJson(keys, values);
				response.getWriter().println(returnJsonMessage);
			}else{
				String[] keys   = {"islogin","action","username"};
				String[] values = {"true","welcome",username};
				String returnJsonMessage = generateJson(keys, values);
				response.getWriter().println(returnJsonMessage);
			}	
		}else{
			returnErrorMessage(response);
		}	
	}
	
	private void returnErrorMessage(HttpServletResponse response) throws IOException{
		response.getWriter().println("{\"islogin\":\"false\"}");
	}
	
	private void printResult(HttpServletResponse response){
		try {
			response.getWriter().println("hello world");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.writelog(e);
			e.printStackTrace();
		}
	}
	//0 = samlresponse 1 = AssertionConsumerServiceURL
	private String[] makeAssertion(String samlreuest,String username){
		SAMLrequest requestHandle = new SAMLrequest(samlreuest);
		if(requestHandle.readFromRequest() == false){
			return null;
		}
		String issuer       = requestHandle.getIssuerURL();
		String acsUrl       = requestHandle.getAcsURL();
		String requestID    = requestHandle.getRequestID();
		String providerName = requestHandle.getprovideName();
		
		UserRole user = new UserRole();
		user.setUsername(username);
		
		SAMLresponse responseHandle = new SAMLresponse(user, issuer, requestID,acsUrl);
		responseHandle.generateAuthnResponse();
		
		String samlresponse = responseHandle.getSamlResponse();
		
		String[] samlResponseAttributes = new String[]{samlresponse,acsUrl};
		
		return samlResponseAttributes;
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
		HttpSession session = request.getSession(false);
		if(session == null){
			session = request.getSession();
		}
		session.setAttribute("username", username);
		return true;
	}
	private boolean authenticateUser(String username,String password,HttpServletRequest request)
	{
		boolean result = false;
		if(username == null ||password == null){
			return false;
		}

		UserAuthenticate uAuth = new UserAuthenticate(username, password);
		result = uAuth.doAuthenticate();
		return result;
	}
}
