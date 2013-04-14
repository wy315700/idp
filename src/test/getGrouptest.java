package test;

import org.cas.iie.idp.admin.groupAdmin;
import org.cas.iie.idp.user.GroupRole;

public class getGrouptest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		groupAdmin groupadmin = new groupAdmin();
		//System.out.println(groupadmin.getGroupByName("grp2").toString());
		groupadmin.deleteGroup("grp2");
		GroupRole group = new GroupRole();
		group.setGroupname("grp2");
		group.addUser("aaa");
		group.addUser("bbb");
		groupadmin.addGroup(group);
	}

}
