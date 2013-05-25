package juva;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.math.*;
import java.text.*;
import java.util.*;

public class Utils {
	
	public static String MD5(String rawInput) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(rawInput.getBytes());
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hash = bigInt.toString(16);
        return hash;
	}

	public static String getCurrentTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String currentFormatTime = formatter.format(currentTime);
        return currentFormatTime;
	}

}
