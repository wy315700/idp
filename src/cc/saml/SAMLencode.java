package cc.saml;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;

import LOG.Logger;

public class SAMLencode {
	String SAMLrequest;
	String SAMLresult;
	public SAMLencode(String sAMLrequest) {
		super();
		SAMLrequest = sAMLrequest;
	}
	@SuppressWarnings("deprecation")
	public String doEncode(){
		if(SAMLrequest == null){
			return null;
		}
		
		
		Deflater deflater = new Deflater(Deflater.DEFLATED, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(
				byteArrayOutputStream, deflater);
		try {
			deflaterOutputStream.write(SAMLrequest.getBytes());
			deflaterOutputStream.close();
			Base64 base64encoder = new Base64();
			byte[] samlResponseByte = base64encoder.encode(byteArrayOutputStream.toByteArray());
			
			SAMLresult = URLEncoder.encode(new String(samlResponseByte));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			SAMLresult = null;
			Logger.writelog(e);
			e.printStackTrace();
		}
		return SAMLresult;
		
	}
}
