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
import org.cas.iie.idp.user.TenantRole;
import org.cas.iie.idp.user.UserRole;

import sun.misc.BASE64Encoder;

import LOG.Logger;

public class tenantAdmin {
	private LDAPhelper ldaphelper = null;
	public tenantAdmin() {
		ldaphelper = new LDAPhelper();
	}
	public boolean addTenant(TenantRole tenant){
		if(tenant.getTenantname() == null){
			return false;
		}
		if(tenant.getTenanturl() == null){
			tenant.setTenanturl(tenant.getTenantname());
		}
		LDAPhelper ldaphelper = new LDAPhelper();
		Attributes attrs = new BasicAttributes(true);
		Attribute objectclass = new BasicAttribute("objectClass");
		objectclass.add("organization");
		objectclass.add("top");
		attrs.put(objectclass);
		attrs.put("o", tenant.getTenantname());
		attrs.put("l", tenant.getTenantadminname());
		
		String password = tenant.getTenantadminpassword();
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

		
		return ldaphelper.create("o="+tenant.getTenantname()+","+LDAPhelper.SUPER_DOMAIN, attrs);
	}
	public List<TenantRole> getAllTenant(){
	    String base = LDAPhelper.SUPER_DOMAIN;
        String filter = "(objectClass=organization)";
        String[] returnAttr = new String[] {"o","l"};
        List<TenantRole> tenants = new ArrayList<TenantRole>(); 
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, null, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			while(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				Attributes attrs = entry.getAttributes();
				TenantRole tenant = new TenantRole();
				Attribute oAttr = attrs.get("o");
				Attribute lAttr = attrs.get("l");
				
				tenant.setTenantname(oAttr.get().toString());	
				tenant.setTenanturl(oAttr.get().toString());
				if(lAttr != null){
					tenant.setTenantadminname(lAttr.get().toString());
				}
				tenant.setTenantDN(entry.getNameInNamespace());
				tenants.add(tenant);
			}
			return tenants;
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
		}finally{
		}
        return null;
	}
	public TenantRole getTenantByName(String tenantname){
		TenantRole tenant = new TenantRole();
		tenant.setTenantname(tenantname);
	    String base = LDAPhelper.SUPER_DOMAIN;
        String filter = "(&(objectClass=organization)(o={0}))";
        String[] returnAttr = new String[] {"o"};
        
        try {
			NamingEnumeration enm = ldaphelper.search(base, filter, new String[] { tenantname }, returnAttr);
			if(enm == null){
				throw new NamingException("search failed");
			}
			if(enm.hasMore()){
				SearchResult entry = (SearchResult)enm.next();
				String tenantDN = null;
				tenantDN = entry.getNameInNamespace();
				tenant.setTenantDN(tenantDN);
				tenant.setTenanturl(tenantname);
			}
			else{
				tenant = null;
			}
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
			tenant = null;
		}
        
		return tenant;

	}
	public boolean delTenant(TenantRole tenant){
		tenant = getTenantByName(tenant.getTenantname());
		if(tenant == null){
			return false;
		}
		return ldaphelper.delete(tenant.getTenantDN());
	}
}
