package cc.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.opensaml.common.SAMLException;

import LOG.Logger;

public class SAMLdecode {
	String SAMLrequest;
	String SAMLresult;
	public SAMLdecode(String sAMLrequest) {
		super();
		SAMLrequest = sAMLrequest;
	}
	public String doDecode(){
		if(SAMLrequest == null || SAMLrequest.equals("undefined")){
			return null;
		}
		try {
			SAMLresult = decodeAuthnRequestXML(SAMLrequest);
		} catch (SAMLException e) {
			// TODO Auto-generated catch block
			Logger.writelog(e);
			e.printStackTrace();
			SAMLresult = null;
		}
		return SAMLresult;
	}
	
	
	/** (Based on the Google reference implementation, with some modifications suggested in the Google Api's group
	*  -Nate- to avoid buffer size problems in the original code) 
	* Retrieves the AuthnRequest from the encoded and compressed String extracted
	* from the URL. The AuthnRequest XML is retrieved in the following order: <p>
	   * 1. URL decode <br> 2. Base64 decode <br> 3. Inflate <br> Returns the String
	* format of the AuthnRequest XML.
	* 
	* @param encodedRequestXmlString the encoded request xml
	* @return the string format of the authentication request XML.
	* 
	*/
	private String decodeAuthnRequestXML(String encodedRequestXmlString) 
		throws SAMLException {
		String uncompressed = null; 
		try {
			// URL decode
			// No need to URL decode: auto decoded by request.getParameter() method
			// Base64 decode
			Base64 base64Decoder = new Base64();
			byte[] xmlBytes = encodedRequestXmlString.getBytes("UTF-8");
			byte[] base64DecodedByteArray = base64Decoder.decode(xmlBytes);
	 
			// Uncompress the AuthnRequest data using a stream decompressor, as suggested in discussions
			 // of the Google Apps Api's group.
			 try {
				uncompressed = new String(inflate(base64DecodedByteArray, true)); 
			} catch (ZipException e) {
				uncompressed = new String(inflate(base64DecodedByteArray, false));
			}
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Logger.writelog(e);
			throw new SAMLException("Error decoding AuthnRequest: " +
	           "Check decoding scheme - " + e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			Logger.writelog(e);
			throw new SAMLException("Error decoding AuthnRequest: " +
	         "Check decoding scheme - " + e.getMessage());
		}
			return uncompressed;		  
	}
  /** This version is based in the reference implementation, with a modification that avoids using a 
   * fixed buffer size during uncompression. (in its place uses an expandable ByteArrayOutputStream).
   * NOTE: not sure whether it is still necessary to call this method with the boolean flag.
   * @param bytes
   * @param nowrap
   * @return an array of bytes with the inflated content
   * @throws IOException
   */
	private static byte[] inflate(byte[] bytes, boolean nowrap)
			throws IOException {
		Inflater decompressor = null;
		ByteArrayOutputStream out = null;
		try {
			decompressor = new Inflater(nowrap);
			decompressor.setInput(bytes);
			out = new ByteArrayOutputStream(bytes.length);
			byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				try {
					int count = decompressor.inflate(buf); // PROBLEM
					out.write(buf, 0, count);
					// added check to avoid loops
					if (count == 0) {
						return altInflate(bytes);
					}
				} catch (DataFormatException e) {
					Logger.writelog(e);
					System.out.println("DataFormatException while inflating "+e);
					return altInflate(bytes);

				} catch (Exception e) {
					Logger.writelog(e);
					System.out.println("Unexpected Exception while inflating "+e);
					return altInflate(bytes);
				} catch (Throwable e) {

					System.out.println("Unexpected Throwable while inflating "+e);
					return altInflate(bytes);
				}
			}
			return out.toByteArray();
		} finally {
			if (decompressor != null)
				decompressor.end();
			try {
				if (out != null)
					out.close();
			} catch (IOException ioe) {
				/* ignore */
			}
		}
	}
  
	/**
	 * Alternative method for inflating the content, used when the default
	 * method is not successful.
	 * 
	 * @param bytes
	 * @return an array of bytes containing the inflated content
	 * @throws IOException
	 */
	protected static byte[] altInflate(byte[] bytes) throws IOException {
		System.out.println("AltInflate Processing... ");
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InflaterInputStream iis = null;
		byte[] buf = new byte[1024];
		try {
			// if DEFLATE fails, then attempt to unzip the byte array according
			// to
			// zlib (rfc 1950)
			bais = new ByteArrayInputStream(bytes);
			iis = new InflaterInputStream(bais);
			buf = new byte[1024];
			int count = iis.read(buf); // PROBLEM
			while (count != -1) {
				baos.write(buf, 0, count);
				count = iis.read(buf);
			}
			return baos.toByteArray();
		} catch (IOException ex) {
			Logger.writelog(ex);
			throw ex;
		} finally {
			if (iis != null)
				try {
					iis.close();
				} catch (IOException ex2) {
				}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex2) {
				}
			}
		}
	}
	
}
