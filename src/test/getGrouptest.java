package test;

import org.cas.iie.idp.admin.groupAdmin;

public class getGrouptest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		groupAdmin groupadmin = new groupAdmin();
		System.out.println(groupadmin.getGroupByName("grp1").toString());
	}

}
