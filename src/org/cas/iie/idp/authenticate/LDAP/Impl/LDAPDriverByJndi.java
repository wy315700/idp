package org.cas.iie.idp.authenticate.LDAP.Impl;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.cas.iie.idp.authenticate.LDAP.ILDAPDriver;

public class LDAPDriverByJndi implements ILDAPDriver {
	private static DirContext ctx = null;
	public static String INITIAL_CONTEXT_FACTORY =  "com.sun.jndi.ldap.LdapCtxFactory";
	public static String PROVIDER_URL =  "ldap://localhost:10389/dc=iie,dc=cas,dc=org";
	public static String SECURITY_AUTHENTICATION =  "simple";
	private static String username = "uid=admin,dc=iie,dc=cas,dc=org";
	private static String password = "123456";
	public static DirContext getDirContext(){
		if(ctx == null){
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        	env.put(Context.PROVIDER_URL, PROVIDER_URL);
			env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			try {
				ctx = new InitialDirContext(env);
				System.out.println("认证成功");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				System.out.println("认证失败");
				e.printStackTrace();
			}
		}
		return ctx;
	}
	public static void closeCtx()
	{
		if(ctx != null){
			try {
				ctx.close();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ctx = null;
		}
	}
	@Override
	public NamingEnumeration getEntriesSubTree(String dn,String filter) throws NamingException {
		// TODO Auto-generated method stub
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration result = getDirContext().search(dn,filter, sc);
		return result;
	}
	@Override
	public NamingEnumeration getEntryObject(String dn,String filter) throws NamingException {
		// TODO Auto-generated method stub
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		NamingEnumeration result = getDirContext().search(dn,filter, sc);
		return result;
	}
	@Override
	public void getEntry(String filter) {
		// TODO Auto-generated method stub
		
	}
}
