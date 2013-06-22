package juva;

import java.security.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.io.PrintWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Utils {

    public static class Json{

        public static PrintWriter printer;

        public static void registerPrinter(PrintWriter printer){
            Utils.Json.printer = printer;
        }

        public static JSONArray StringToJson(String str){
            JSONArray output = JSONArray.fromObject(str);
            return output;
        }

        public static JSONArray ArrayToJson(Object[] array){
            JSONArray output = JSONArray.fromObject(array);
            return output;
        }

        public static JSONArray ArrayListToJson(List list){
            JSONArray output = JSONArray.fromObject(list);
            return output;
        }

        public static JSONObject MapToJson(Map map){
            JSONObject output = JSONObject.fromObject(map);
            return output;
        }

        public static List JsonToList(String json){
            JSONObject jsonObject = JSONObject.fromObject(json);
            List list = (List) JSONArray.toCollection(
                             jsonObject.getJSONArray("array"));
            return list;
        }

        public static JSONArray encode(String str){
            return StringToJson(str);
        }

        public static JSONArray encode(Object[] array){
            return ArrayToJson(array);
        }

        public static JSONArray encode(List list){
            return ArrayListToJson(list);
        }

        public static JSONObject encode(Map map){
            return MapToJson(map);
        }

        public static List decode(String json){
            return JsonToList(json);
        }

        public static JSONObject encode(String arg1, String arg2){
            Map<String, String> map = new HashMap<String, String>();
            map.put("success", arg1);
            map.put("message", arg2);
            return encode(map);
        }

        public static void json(String str){
            if (printer != null){
                printer.println(encode(str));                
            }
        }

        public static void json(Object[] array){
            if (printer != null){
                printer.println(encode(array));
            }
        }

        public static void json(List list){
            if (printer != null){
                printer.println(encode(list));
            }
        }

        public static void json(Map map){
            if (printer != null){
                printer.println(encode(map));
            }
        }

        public static void json(String arg1, String arg2){
            if (printer != null){
                printer.println(encode(arg1, arg2));
            }
        }
    };

    public static String MD5(String rawInput)
            throws NoSuchAlgorithmException{
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

    public static String fixTime(String input){
        return input.substring(0, input.length()-2);
    }
}