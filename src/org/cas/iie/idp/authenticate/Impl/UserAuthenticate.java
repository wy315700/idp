package org.cas.iie.idp.authenticate.Impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.cas.iie.idp.authenticate.LDAP.Impl.LDAPDriverByJndi;

public class UserAuthenticate {
	private DirContext ctx = null;
	private Hashtable env = null;
	private String username;
	private String password;
	public UserAuthenticate(String username, String password) {
		this.username = username;
		this.password = password;
	}
	@SuppressWarnings("unchecked")
	private void LDAPconnect(){
		 env = new Hashtable();
	     env.put(Context.INITIAL_CONTEXT_FACTORY, LDAPDriverByJndi.INITIAL_CONTEXT_FACTORY);
	     env.put(Context.PROVIDER_URL, LDAPDriverByJndi.PROVIDER_URL);
	     env.put(Context.SECURITY_AUTHENTICATION, LDAPDriverByJndi.SECURITY_AUTHENTICATION);
	     try {
			ctx = new InitialDirContext(env);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println("Authentication failed "+e.toString());
		}
	}
	private String getUserDN(String username){
		LDAPconnect();
		String base = "ou=member";
        String filter = "(&(objectClass=inetOrgPerson)(cn={0}))";           
        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        ctls.setReturningAttributes(new String[0]);
        ctls.setReturningObjFlag(true);
        String returnDN = null;
        try {
			NamingEnumeration enm = ctx.search(base, filter, new String[] { username }, ctls);
            if (enm.hasMore()) {
                SearchResult result = (SearchResult) enm.next();
                returnDN = result.getNameInNamespace();

                System.out.println("dn: "+returnDN);
            }

            if (returnDN == null || enm.hasMore()) {
                // uid not found or not unique
                throw new NamingException("Authentication failed");
            }
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnDN;
	}
	public boolean doAuthenticate(){
		String DN = getUserDN(username);
		
		boolean valid = false;
		try {
			ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, DN);
			ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
			ctx.lookup("");
			System.out.println(DN+" is authenticated!");
			valid = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(DN+" is not authenticated!");
			valid = false;
		}
       
		return valid;
	}
}
