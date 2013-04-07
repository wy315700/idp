package org.cas.iie.idp.authenticate.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.cas.iie.idp.authenticate.IGetUser;
import org.cas.iie.idp.authenticate.LDAP.ILDAPDriver;
import org.cas.iie.idp.authenticate.LDAP.Impl.LDAPDriverByJndi;
import org.cas.iie.idp.user.UserRole;

public class GetUserByLdap implements IGetUser {

	private DirContext ctx = null;
	public GetUserByLdap() {
		ctx = LDAPDriverByJndi.getDirContext();
	}
	@Override
	public UserRole getUserByName(String username, boolean flag) {
		// TODO Auto-generated method stub
		UserRole user = new UserRole();
		user.setUsername(username);
	    String base = "ou=member";
        String filter = "(&(objectClass=inetOrgPerson)(cn={0}))";
        String[] returnAttr = new String[] {"cn","uid"};
        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        ctls.setReturningAttributes(returnAttr);
        ctls.setReturningObjFlag(true);
        try {
			NamingEnumeration enm = ctx.search(base, filter, new String[] { username }, ctls);
			if(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				Attributes attrs = entry.getAttributes();
				
				if(attrs == null ||enm.hasMore()){
					throw new NamingException("Authentication failed");
				}
				Attribute uidAttr = attrs.get("uid");
				user.setUserID(new Integer(uidAttr.get().toString()));
				System.out.println("uid="+uidAttr.get());
			}
			ctx.close();
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user = null;
		}
        
        
		return user;
	}

	@Override
	public UserRole getUserByID(int userID, boolean flag) {
		// TODO Auto-generated method stub
		return null;
	}

}
