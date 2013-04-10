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
import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.UserRole;

import LOG.Logger;

public class GetUserByLdap implements IGetUser {

	private LDAPhelper ldaphelper = null;
	public GetUserByLdap() {
		ldaphelper = new LDAPhelper();
	}
	@Override
	public UserRole getUserByName(String username, boolean flag) {
		// TODO Auto-generated method stub
		UserRole user = new UserRole();
		user.setUsername(username);
	    String base = "ou=member";
        String filter = "(&(objectClass=inetOrgPerson)(cn={0}))";
        String[] returnAttr = new String[] {"cn","uid"};
        
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, new String[] { username }, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			if(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				
				String userDN = entry.getNameInNamespace();
				
				user.setUserDN(userDN);
				
				Attributes attrs = entry.getAttributes();
		
				if(attrs == null ||enm.hasMore()){
					throw new NamingException("search failed");
				}
				Attribute uidAttr = attrs.get("uid");
				user.setUserID(new Integer(uidAttr.get().toString()));
				System.out.println("uid="+uidAttr.get());
			}
			
			user = getUserGroup(user);
			
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
			user = null;
		}
        ldaphelper.close();
		return user;
	}

	public UserRole getUserGroup(UserRole user){
	    String base = "ou=group";
        String filter = "(&(objectClass=groupOfUniqueNames)(uniqueMember={0}))";
        String[] returnAttr = new String[] {"cn","uniqueMember"};
        
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, new String[] { user.getUserDN() }, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			while(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				Attributes attrs = entry.getAttributes();

				Attribute cnAttr = attrs.get("cn");
				user.addUsergroup(cnAttr.get().toString());
			}
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
		}
        return user;
	}
	@Override
	public UserRole getUserByID(int userID, boolean flag) {
		// TODO Auto-generated method stub
		return null;
	}

}
