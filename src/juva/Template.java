package juva;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.*;


public class Template {

    private String _path;
    private Map<String, Object> variables = new HashMap<String, Object>();
    
    private final static String ifStartStr =
                                 "\\{%[\\s]*?if [\\S]+?[\\s]*?\\:[\\s]*?%\\}";
    private final static Pattern ifStartPattern =
                                 Pattern.compile(ifStartStr);

    private final static String ifEndStr =
                                 "\\{%[\\s]*?endif[\\s]*?%\\}";
    private final static Pattern ifEndPattern =
                                 Pattern.compile(ifEndStr);

    private final static String forStartStr =
                    "\\{%[\\s]*?for[\\s]+?[\\S]+?[\\s]+?in[\\s]+?[\\S]+?[\\s]*?\\:[\\s]*?%\\}";
    private final static Pattern forStartPattern =
                                 Pattern.compile(forStartStr);

    private final static String forEndStr =
                                 "\\{%[\\s]*?endfor[\\s]*?%\\}";
    private final static Pattern forEndPattern =
                                 Pattern.compile(forEndStr);
    
    private final static String varString = "\\{\\{[\\s\\S]+?\\}\\}";
    private final static Pattern varPattern =
                                 Pattern.compile(varString);
    
    private Stack<Integer> tempIndex = new Stack<Integer>();
    private Stack<Integer> cmdsIndex = new Stack<Integer>();
    private Map<Integer, Integer> cmdEndsIndex =
    	                           new HashMap<Integer, Integer>();
    
    public Template(String path){
        this._path = path;
    }
    
    public void putVariables(String key, Object value){
        this.variables.put(key, value);
    }
    
    public String render() throws IOException{
        String line = "";
        String lines = "";
        FileInputStream stream = new FileInputStream(this._path);
        InputStreamReader reader = new InputStreamReader(stream, "utf-8");
        BufferedReader buffer = new BufferedReader(reader);
        line = buffer.readLine();
        while(line != null){
            lines = lines + line + "\n";
            line = buffer.readLine();
        }
        String html = parse(lines);
        return html;
    }
    
    public boolean isMatchIfStart(String input){
    	Matcher matcher = ifStartPattern.matcher(input);
    	if(matcher.find()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean isMatchIfEnd(String input){
    	Matcher matcher = ifEndPattern.matcher(input);
    	if(matcher.find()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean isMatchForStart(String input){
    	Matcher matcher = forStartPattern.matcher(input);
    	if(matcher.find()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean isMatchForEnd(String input){
    	Matcher matcher = forEndPattern.matcher(input);
    	if(matcher.find()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public String parseSub(String html){
    	
    	scanHtml(html);
    	
    	if (this.cmdsIndex.empty()) {
    		return html;
    	}
    	
    	int begin = this.cmdsIndex.pop();
    	int end = this.cmdEndsIndex.get(begin);
    	
    	String subHtml = html.substring(begin, end);
    	
    	String parsedHtml = subHtml;
    	if (isMatchIfStart(subHtml)){
    		parsedHtml = parseIF(parsedHtml);
    	}
    	if (isMatchForStart(subHtml)){
    		parsedHtml = parseFor(parsedHtml);
    	}
    	
    	html = replaceStrByIndex(html, begin, end, parsedHtml);
    	
    	html = parseSub(html);
    	
    	return html;
    		
    }
    
    public String parse(String html){
     	String output = html;
        output = parseSub(html);
    	output = parseVar(output);

    	return output;
    }
    
    public void scanHtml(String html){
    	this.cmdsIndex.clear();
    	this.cmdEndsIndex.clear();
    	String[] lines = html.split("\n");
    	
    	for (int i=0;i<lines.length; ++i){
    		
    		String line = lines[i];
    		
	    	Matcher ifStartMatcher = ifStartPattern.matcher(line);
	    	if (ifStartMatcher.find()){
	    		String cmd = ifStartMatcher.group(0);
	    		int index = html.indexOf(cmd);
	    		this.cmdsIndex.push(index);
	    		this.tempIndex.push(index);
	    		html = replaceSubStrWithBlank(html, cmd);
	    	}

	    	Matcher ifEndMatcher = ifEndPattern.matcher(line);
	    	if (ifEndMatcher.find()){
	    		String cmd = ifEndMatcher.group(0);
	    		int index = html.indexOf(cmd) + cmd.length();
	    		int lastcmdPair = this.tempIndex.pop();
	    		this.cmdEndsIndex.put(lastcmdPair, index);
	    		html = replaceSubStrWithBlank(html, cmd);
	    	}
	    	
	    	Matcher forStartMatcher = forStartPattern.matcher(line);
	    	if (forStartMatcher.find()){
	    		String cmd = forStartMatcher.group(0);
	    		int index = html.indexOf(cmd);
	    		this.cmdsIndex.push(index);
	    		this.tempIndex.push(index);
	    		html = replaceSubStrWithBlank(html, cmd);
	    	}
	    	
	    	Matcher forEndMatcher = forEndPattern.matcher(line);
	    	if (forEndMatcher.find()){
	    		String cmd = forEndMatcher.group(0);
	    		int index = html.indexOf(cmd) + cmd.length();
	    		int lastcmdPair = this.tempIndex.pop();
	    		this.cmdEndsIndex.put(lastcmdPair, index);
	    		html = replaceSubStrWithBlank(html, cmd);
	    	}
    	}
    	
    }

    public String parseIF(String input){
        String output = "";
        String condition = getIfCondition(input);
        String var = (String) variables.get(condition);
        if (var != null){
            boolean conditionIsTrue = var.equalsIgnoreCase("true");
            if (conditionIsTrue){
            	output = parseVar(input);
            }
        }
        output = removeCmd(output);
        return output;
    }

    public String parseFor(String input){
        String output = "";
        String condition = getForCondition(input);
        String[] conditions = condition.split("[\\s]+?in[\\s]+?");
        String singleName = conditions[0];
        String setName = conditions[1];
        Object[] varSet = (Object[]) variables.get(setName);
        if (varSet != null){
        	String[] lines = input.split("\n");
        	for (int i=0;i<varSet.length; ++i){
        		for (int j=0; j<lines.length; ++j){
        			String line = lines[j];
        			variables.put(singleName, varSet[i]);
        		 	line = parseVar(line);
        		 	output += line;
        		}
        	}
        }

        output = removeCmd(output);

        return output;
    }

    public String parseVar(String input){
        String output = input;
        String variablePattern = "\\{\\{[\\s\\S]+?\\}\\}";
        Pattern pattern = Pattern.compile(variablePattern);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            String foundString = matcher.group(0);
            String key = foundString;
            key = key.replaceAll("\\{\\{", "");
            key = key.replaceAll("\\}\\}", "");
            key = key.replaceAll(" ", "");
            String replacement = (String) variables.get(key);
            if (replacement != null){
                output = Utils.replaceAll(output, foundString, replacement);
            }
            else{
                output = Utils.replaceAll(output, foundString, "No Key!");
            }
        }
        return output;
    }

    // TODO: Combine getXXXCondition methods.
    public String getForCondition(String input){
    	 String condition = "";
         Matcher forMatcher = forStartPattern.matcher(input);
         if (forMatcher.find()){
             condition = forMatcher.group(0);
             condition = condition.replaceAll("\\{%[\\s]*?for[\\s]*?", "");
         }
         condition = condition.replaceAll(":[\\s]*?%\\}", "");
         condition = condition.trim();
         return condition;
    }

    public String getIfCondition(String input){
        String condition = "";
        Matcher ifMatcher = ifStartPattern.matcher(input);
        if (ifMatcher.find()){
            condition = ifMatcher.group(0);
            condition = condition.replaceAll("\\{%[\\s]*?if[\\s]*?", "");
        }
        condition = condition.replaceAll(":[\\s]*?%\\}", "");
        condition = condition.trim();
        return condition;
    }

    public String removeCmd(String input){
        String output = input;
        
        output = output.replaceAll(ifStartStr, "");
        output = output.replaceAll(ifEndStr, "");
        output = output.replaceAll(forStartStr, "");
        output = output.replaceAll(forEndStr, "");
        
        return output;
    }
    
    public String replaceSubStrWithBlank(String input, String subStr){
    	int index = input.indexOf(subStr);
    	String front = input.substring(0, index);
    	String rear = input.substring(index + subStr.length(), input.length());
    	for (int i=0;i<subStr.length();++i){
    		front = front + " ";
    	}
    	String output = front + rear;
    	return output;
    }

    public String replaceStrByIndex(String input, int begin,
    		                         int end, String replacement){
    	String front = input.substring(0, begin);
    	String rear = input.substring(end);
    	String output = front + replacement + rear;
    	return output;    	
    }
    
}
