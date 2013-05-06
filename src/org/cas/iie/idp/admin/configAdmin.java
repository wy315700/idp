package org.cas.iie.idp.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;

import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.GroupRole;

import LOG.Logger;

public class configAdmin {
	private LDAPhelper ldaphelper = null;

	public configAdmin() {
		super();
		ldaphelper = new LDAPhelper();
	}
	public Set<String> getAllAttibutions(){
		String base = null;
        String filter = "(objectClass=organizationalUnit)";
        String[] returnAttr = new String[] {"ou"};
        Set<String> attributions = new HashSet<>(); 
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, null, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			while(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				Attributes attrs = entry.getAttributes();
				Attribute ouAttr = attrs.get("ou");
				String ou = ouAttr.get().toString();

				if(!ou.equals("member")){
					attributions.add(ou);
				}
			}
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
		}
        return attributions;
	}
	public boolean isAttrbutionExists(String attrtype){
		String base = null;
        String filter = "(&(objectClass=organizationalUnit)(ou={0}))";
        String[] returnAttr = new String[] {"ou"};
        boolean result = false;
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, new String[] { attrtype}, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			if(enm.hasMore()){
				enm.next();
				if(enm.hasMore()){
					throw new NamingException("search failed");
				}
				result = true;
			}
			else{
				result = false;
			}
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			Logger.writelog(e);
		}
        return result;
	}
	public boolean deleteAttribution(String attrtype){
		LDAPhelper ldaphelper = new LDAPhelper();
		return ldaphelper.delete("ou="+attrtype);
	}

	public boolean addAttribute(String attrtype){
		if(attrtype == null){
			return false;
		}
		Attributes attrs = new BasicAttributes(true);
		Attribute objectclass = new BasicAttribute("objectClass");
		objectclass.add("organizationalUnit");
		objectclass.add("top");
		attrs.put(objectclass);
		attrs.put("ou", attrtype);
		
		return ldaphelper.create("ou="+attrtype, attrs);
	}
}
