package test;

import LOG.Logger;
import sun.util.logging.resources.logging;

public class mysqltest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Exception e = new Exception("example error");
		Logger.writelog(e);
	}

}
