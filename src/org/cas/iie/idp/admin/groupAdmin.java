package org.cas.iie.idp.admin;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;

import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.GroupRole;
import org.cas.iie.idp.user.UserRole;

import LOG.Logger;

public class groupAdmin {
	private LDAPhelper ldaphelper = null;
	private String attrtype = null;
	private String searchbase = "ou=";
	public groupAdmin(String attrtype) {
		this.attrtype = attrtype;
		searchbase += attrtype;
		ldaphelper = new LDAPhelper();
	}
	public GroupRole getGroupByName(String groupname){
	    String base = searchbase;
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
				group.setGroupDN(entry.getName()+","+searchbase);
				if(userAttr.size() > 0){
					for(int i = 0 ; i < userAttr.size() ; i++){
						String username = userAttr.get(i).toString();
						if(username.equals("cn=null"))
							continue;
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
	
	public boolean modifyGoupe(GroupRole srcgroup, GroupRole disgroup){
		deleteGroup(srcgroup.getGroupname());
		return addGroup(disgroup);
	}
	public boolean modifyGroupMembers(UserRole users,String attrtype){
		List<GroupRole> groups = getAllGroups(0, 0);
		
		for(GroupRole group : groups){
			deleteUserFromGroup(group, users);
			for(String groupname : users.getUsergroup().get(attrtype)){
				if(group.getGroupname().equals(groupname)){
					addUsertoGroup(group, users);
				}
			}
		}
		return true;
	}
	public boolean deleteUserFromGroup(GroupRole group,UserRole user){
		Attributes attrs = new BasicAttributes();
		
		Attribute uniquememberattr = new BasicAttribute("uniqueMember");
		uniquememberattr.add("cn=null");
		for(String userDn : group.getUsers()){
			if( !userDn.equals(user.getUserDN()))
				uniquememberattr.add(userDn);
		}
		attrs.put(uniquememberattr);
		return ldaphelper.modify(group.getGroupDN(), attrs);
	}
	public boolean addUsertoGroup(GroupRole group,UserRole user){
		Attributes attrs = new BasicAttributes();
		
		Attribute uniquememberattr = new BasicAttribute("uniqueMember");
		uniquememberattr.add("cn=null");
		for(String userDn : group.getUsers()){
			uniquememberattr.add(userDn);
		}
		uniquememberattr.add(user.getUserDN());
		attrs.put(uniquememberattr);

		return ldaphelper.modify(group.getGroupDN(), attrs);
	}

	public List<GroupRole> getAllGroups(int start , int limits){
	    String base = searchbase;
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
				group.setGroupDN(entry.getName()+","+searchbase);
				if(userAttr.size() > 0){
					for(int i = 0 ; i < userAttr.size() ; i++){
						String username = userAttr.get(i).toString();
						if(username.equals("cn=null"))
							continue;
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
	public boolean deleteGroup(String groupname){
		GroupRole group = getGroupByName(groupname);
		LDAPhelper ldaphelper = new LDAPhelper();
		return ldaphelper.delete(group.getGroupDN());
	}
	public boolean addGroup(GroupRole group){
		if(group.getGroupname() == null){
			return false;
		}
		LDAPhelper ldaphelper = new LDAPhelper();
		Attributes attrs = new BasicAttributes(true);
		Attribute objectclass = new BasicAttribute("objectClass");
		objectclass.add("groupOfUniqueNames");
		objectclass.add("top");
		attrs.put(objectclass);
		attrs.put("cn", group.getGroupname());
		
		Attribute uniqueMember = new BasicAttribute("uniqueMember");
		if(group.getUsers().size() == 0){
			uniqueMember.add("cn=null");
		}else{
			for(int i = 0; i < group.getUsers().size() ; i++){
				uniqueMember.add("cn="+group.getUsers().get(i));
			}
		}
		attrs.put(uniqueMember);
		return ldaphelper.create("cn="+group.getGroupname()+","+searchbase, attrs);
	}
}
