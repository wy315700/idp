package org.cas.iie.idp.admin;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.GroupRole;

import LOG.Logger;

public class groupAdmin {
	private LDAPhelper ldaphelper = null;
	public groupAdmin() {
		ldaphelper = new LDAPhelper();
	}
	public GroupRole getGroupByName(String groupname){
	    String base = "ou=group";
        String filter = "(&(objectClass=groupOfUniqueNames)(cn={0}))";
        String[] returnAttr = new String[] {"cn","uniqueMember"};
		GroupRole group = new GroupRole();

        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, new String[]{groupname}, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			if(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				Attributes attrs = entry.getAttributes();
				Attribute cnAttr = attrs.get("cn");
				Attribute userAttr = attrs.get("uniqueMember");
				group.setGroupname(cnAttr.get().toString());
				if(userAttr.size() > 0){
					for(int i = 0 ; i < userAttr.size() ; i++){
						String username = userAttr.get(i).toString();
						group.addUser(username);
					}
				}
			}
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
		}
        ldaphelper.close();
        return group;
	}
	public List<GroupRole> getAllGroups(int start , int limits){
	    String base = "ou=group";
        String filter = "(objectClass=groupOfUniqueNames)";
        String[] returnAttr = new String[] {"cn","uniqueMember"};
        List<GroupRole> groups = new ArrayList<GroupRole>(); 
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, null, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			while(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				Attributes attrs = entry.getAttributes();
				GroupRole group = new GroupRole();
				Attribute cnAttr = attrs.get("cn");
				Attribute userAttr = attrs.get("uniqueMember");
				group.setGroupname(cnAttr.get().toString());
				if(userAttr.size() > 0){
					for(int i = 0 ; i < userAttr.size() ; i++){
						String username = userAttr.get(i).toString();
						group.addUser(username);
					}
				}
				
				groups.add(group);
			}
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
		}
        ldaphelper.close();
        return groups;
	}
}
