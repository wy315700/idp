package org.cas.iie.idp.authenticate.LDAP;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

public class LDAPTest {
	private DirContext ctx = null;
	private Properties ldapProps;
	public LDAPTest(Properties ldapProps) {
		this.ldapProps = ldapProps;
	}
	public DirContext getDirContext() throws NamingException{
		if(ctx == null){
			ctx = new InitialDirContext(ldapProps);
		}
		return ctx;
	}
	public void queryEntry() throws NamingException{
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		NamingEnumeration result = getDirContext().search("ou=member", "cn=wangyang", sc);
		while(result.hasMore()){
			SearchResult entry = (SearchResult)result.next();
			Attributes attrs = entry.getAttributes();
			Attribute attr = attrs.get("uid");
			System.out.println("ou="+attr.get());
		}
	}
	public static void main(String args[]){
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/apacheds.properties");
		
		try {
			prop.load(in);
			LDAPTest ldaptest = new LDAPTest(prop);
			ldaptest.queryEntry();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
}
