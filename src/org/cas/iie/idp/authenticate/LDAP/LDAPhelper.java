package org.cas.iie.idp.authenticate.LDAP;

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

import LOG.Logger;

public class LDAPhelper implements ILDAPDriver{
	private DirContext ctx = null;
	public static String INITIAL_CONTEXT_FACTORY =  "com.sun.jndi.ldap.LdapCtxFactory";
	public static String PROVIDER_URL =  "ldap://localhost:10389/";
	public static String SUPER_DOMAIN = "dc=org";
	public static String SECURITY_AUTHENTICATION =  "simple";
	private static String username = "uid=admin,dc=iie,dc=cas,dc=org";
	private static String password = "123456";
	private static String configfile = "apacheds.properties";
	
	public void LDAPhelper(){
		getDirContext();
	}
	private void getDirContext(){
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
				Logger.writelog(e);
			}
		}
	}
	public void close()
	{
		if(ctx != null){
			try {
				ctx.close();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Logger.writelog(e);
			}
			ctx = null;
		}
	}
	public boolean create(String DN,Attributes attrs){
		getDirContext();
		try {
			long start = System.currentTimeMillis();
			ctx.createSubcontext(DN, attrs);
			long end = System.currentTimeMillis();
			System.out.println("create "+DN+" time :"+(end-start));
			return true;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			Logger.writelog(e);
			e.printStackTrace();
		}
		return false;
	}
	public boolean delete(String dn){
		getDirContext();
		try {
			long start = System.currentTimeMillis();
			ctx.destroySubcontext(dn);
			long end = System.currentTimeMillis();
			System.out.println("delete "+dn+"time :"+(end-start));
			return true;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			Logger.writelog(e);
			e.printStackTrace();
		}
		return false;
	}
	public boolean modify(String DN,Attributes attrs){
		getDirContext();
		try {
			long start = System.currentTimeMillis();
			ctx.modifyAttributes(DN, DirContext.REPLACE_ATTRIBUTE, attrs);
			long end = System.currentTimeMillis();
			System.out.println("modify "+DN+" time :"+(end-start));
			return true;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			Logger.writelog(e);
			e.printStackTrace();
		}
		return false;
	}
	public NamingEnumeration search(String dn,String filter,String[] filtervalues,String[] returnAttributions){
		getDirContext();
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		sc.setReturningAttributes(returnAttributions);
		sc.setReturningObjFlag(true);
		try {
			long start = System.currentTimeMillis();
			NamingEnumeration result = ctx.search(dn,filter,filtervalues, sc);
			long end = System.currentTimeMillis();
			System.out.println("search time :"+(end-start));
			return result;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			Logger.writelog(e);
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public NamingEnumeration getEntriesSubTree(String dn,String filter) throws NamingException {
		// TODO Auto-generated method stub
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration result = ctx.search(dn,filter, sc);
		return result;
	}
	@Override
	public NamingEnumeration getEntryObject(String dn,String filter) throws NamingException {
		// TODO Auto-generated method stub
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		NamingEnumeration result = ctx.search(dn,filter, sc);
		return result;
	}
	@Override
	public void getEntry(String filter) {
		// TODO Auto-generated method stub
		
	}
}
