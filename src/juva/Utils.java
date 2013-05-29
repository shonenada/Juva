package juva;

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

    public static String replaceAll(String input, String str, String replacement){
        String output = input;
        String temp = output.replace(str, replacement);
        while(temp != output){
            output = output.replace(str, replacement);
            temp = output.replace(str, replacement);
        }
        return output;
    }

    public static String escapeHtml(String raw){

        String output = raw;

        output = replaceAll(output, "<", "&lt;");
        output = replaceAll(output, ">", "&gt;");
        output = replaceAll(output, "&", "&amp;");
        output = replaceAll(output, "\'", "&#39;");
        output = replaceAll(output, "\"", "&#34;");
        
        return output;

    }
}
