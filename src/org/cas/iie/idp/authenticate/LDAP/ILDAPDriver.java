package org.cas.iie.idp.authenticate.LDAP;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public interface ILDAPDriver {
	public void getEntry(String filter);

	NamingEnumeration getEntriesSubTree(String dn, String filter)
			throws NamingException;

	NamingEnumeration getEntryObject(String dn, String filter)
			throws NamingException;
}
