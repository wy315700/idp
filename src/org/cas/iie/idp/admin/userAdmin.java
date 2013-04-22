package org.cas.iie.idp.admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import sun.misc.BASE64Encoder;

import LOG.Logger;

public class userAdmin {
	private LDAPhelper ldaphelper = null;
	public userAdmin() {
		ldaphelper = new LDAPhelper();
	}
	public void close(){
		ldaphelper.close();
	}
	public boolean addUser(UserRole user){
		
		if(user.getUsername() == null || user.getRealname()==null){
			return false;
		}
		
		Attributes attrs = new BasicAttributes(true);
		
		Attribute objectclass = new BasicAttribute("objectClass");
		objectclass.add("inetOrgPerson");
		objectclass.add("organizationalPerson");
		objectclass.add("person");
		objectclass.add("top");
		
		attrs.put(objectclass);
		attrs.put("cn", user.getUsername());
		attrs.put("sn", user.getRealname());

		String password = user.getPassword();
		String algorithm = "MD5";
		if(password != null){
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance(algorithm);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			md.update(password.getBytes());
			byte[] bytes = md.digest();
			BASE64Encoder base64encoder = new BASE64Encoder();
			String hash = base64encoder.encode(bytes);
			password = "{"+algorithm+"}"+hash;
			attrs.put("userPassword", password);
		}
		
		return ldaphelper.create("cn="+user.getUsername()+",ou=member", attrs);
	}
	public List<UserRole> getAllUsers(int start,int limit){
	    String base = "ou=member";
        String filter = "(objectClass=inetOrgPerson)";
        String[] returnAttr = new String[] {"cn","sn"};
        List<UserRole> users = new ArrayList<UserRole>(); 
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, null, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			while(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				Attributes attrs = entry.getAttributes();
				UserRole user = new UserRole();
				Attribute cnAttr = attrs.get("cn");
				Attribute snAttr = attrs.get("sn");
				
				user.setUsername(cnAttr.get().toString());
				user.setRealname(snAttr.get().toString());
				
				user.setUserDN(entry.getNameInNamespace());
				
				users.add(user);
			}
			return users;
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
		}finally{
		}
        return null;
	}
	public UserRole getUserByName(String username,boolean inNameSpace) { // inNameSpace =true DN包含命名空间
		// TODO Auto-generated method stub
		UserRole user = new UserRole();
		user.setUsername(username);
	    String base = "ou=member";
        String filter = "(&(objectClass=inetOrgPerson)(cn={0}))";
        String[] returnAttr = new String[] {"cn","sn"};
        
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, new String[] { username }, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			if(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				String userDN = null;
				if(inNameSpace)
					userDN = entry.getNameInNamespace();
				else
					userDN = entry.getName()+",ou=member";
				
				user.setUserDN(userDN);
				
				Attributes attrs = entry.getAttributes();
		
				if(attrs == null ||enm.hasMore()){
					throw new NamingException("search failed");
				}
				Attribute snAttr = attrs.get("sn");
				Attribute cnAttr = attrs.get("cn");

				user.setRealname(snAttr.get().toString());
				user.setUsername(cnAttr.get().toString());
				
				user = getUserGroup(user);
			}
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
			user = null;
		}
        
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
	public boolean modifyUseGroup(UserRole user){
		UserRole preuser = getUserByName(user.getUsername(),true);
		user.setUserDN(preuser.getUserDN());
		groupAdmin groupadmin = new groupAdmin();
		groupadmin.modifyGroupMembers(user);
		return true;
	}
	public boolean deleteuser(String username){
		UserRole user = getUserByName(username,false);
		return ldaphelper.delete(user.getUserDN());
	}

}
