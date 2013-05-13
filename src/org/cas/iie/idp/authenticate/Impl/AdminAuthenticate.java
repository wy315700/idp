package org.cas.iie.idp.authenticate.Impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;

import LOG.Logger;

public class AdminAuthenticate {

	private DirContext ctx = null;
	private Hashtable env = null;
	private String username;
	private String password;
	public AdminAuthenticate(String username, String password) {
		this.username = username;
		this.password = password;
	}
	@SuppressWarnings("unchecked")
	private void LDAPconnect() throws NamingException{
		env = new Hashtable();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, LDAPhelper.INITIAL_CONTEXT_FACTORY);
	    env.put(Context.PROVIDER_URL, LDAPhelper.PROVIDER_URL);
	    env.put(Context.SECURITY_AUTHENTICATION, LDAPhelper.SECURITY_AUTHENTICATION);
		ctx = new InitialDirContext(env);
	}

	public boolean doAuthenticate(){
		String DN = "cn="+username+","+LDAPhelper.domain+",dc=org";
		boolean valid = false;
		try {
			LDAPconnect();
			ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, DN);
			ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
			ctx.lookup("");
			System.out.println(DN+" is authenticated!");
			valid = true;
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Logger.writelog(e1);
			System.out.println(DN+" is not authenticated!");
			valid = false;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
			System.out.println(DN+" is not authenticated!");
			valid = false;
		}
		
		
       
		return valid;
	}


}
