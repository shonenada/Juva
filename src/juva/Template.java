package juva;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
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
                                 "\\{%[\\s]*?for [\\S]+? in [\\S]+?[\\s]*?\\:[\\s]*?%\\}";
    private final static Pattern forStartPattern =
                                 Pattern.compile(forStartStr);

    private final static String forEndStr =
                                 "\\{%[\\s]*?endfor[\\s]*?%\\}";
    private final static Pattern forEndPattern =
                                 Pattern.compile(forEndStr);
    
    private final static String varString = "\\{\\{[\\s\\S]+?\\}\\}";
    private final static Pattern varPattern =
                                 Pattern.compile(varString);
    
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
    
    // TODO: Let the cmds embed with each others.
    public String parse(String html){

        String output = "";
        String[] lines = html.split("\n");

        for (int i=0;i<lines.length;i++){
            String line = lines[i];
            Matcher ifStartMatcher = ifStartPattern.matcher(line);
            Matcher forStartMatcher = forStartPattern.matcher(line);
            if (ifStartMatcher.find()){
                String ifCmd = "";
                Matcher ifEndMatcher = ifEndPattern.matcher(line);
                while(!ifEndMatcher.find()){
                    ifCmd += line + "\n";
                    i++;
                    line = lines[i];
                    ifEndMatcher = ifEndPattern.matcher(line);
                }
                ifCmd += line + "\n";
                line = parseIF(ifCmd);
            }
            else if(forStartMatcher.find()){
                String forCmd = "";
                Matcher forEndMatcher = forEndPattern.matcher(line);
                while(!forEndMatcher.find()){
                    forCmd += line + "\n";
                    i++;
                    line = lines[i];
                    forEndMatcher = forEndPattern.matcher(line);
                }
                forCmd += line + "\n";
                line = parseFor(forCmd);
            }
            else{
                line = parseVar(line);
            }
            output = output + line + "\n";
        }
        
        output = removeCmd(output);
        
        return output;
    }
    
    public String parseIF(String input){
        String output = "";
        String condition = getIfCondition(input);
        String var = (String) variables.get(condition);
        if (var != null){
            boolean conditionIsTrue = var.equalsIgnoreCase("True");
            if (conditionIsTrue){
            	output = parseVar(input);
            }
        }
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
    
}
