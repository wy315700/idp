package test;

import org.cas.iie.idp.user.ConfigRole;
import org.cas.iie.idp.user.Configs;

public class configtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigRole config = Configs.getConfig("iie");
		config.setSAML_NOT_AFTER(20);
		config.setSAML_NOT_BEFORE(25);
		Configs.saveconfig(config);
	}

}