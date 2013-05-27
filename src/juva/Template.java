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
	private Map<String, Object> varibles = new HashMap<String, Object>();
	
	public Template(String path){
		this._path = path;
	}
	
	public void putVariables(String key, Object value){
		this.varibles.put(key, value);
	}
	
	public void readFile(){
		
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
	
	public String parse(String html){
		String output = "";
		String[] lines = html.split("\n");
		
		Pattern ifPattren = Pattern.compile("\\{%[\\s]*?if [\\s\\S]+?\\:%\\}");
//		Matcher ifMatcher = ifPattren.matcher(html);
		
		Pattern forPattern = Pattern.compile("\\{%[\\s]*?for[\\s\\S]+?\\:%\\}");
//		Matcher forMatcher = forPattern.matcher(html);
		
		Pattern varPattern = Pattern.compile("\\{\\{[\\s\\S]+?\\}\\}");
//		Matcher varMatcher = varPattern.matcher(html);
		
		for (int i=0;i<lines.length;i++){
			String line = lines[i];
			Matcher ifMatcher = ifPattren.matcher(line);
			if (ifMatcher.find()){
				
			}
			output = output + parseVar(line) + "\n";
		}
		return output;
	}
	
	public void parseIF(String input){
		
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
			String replacement = (String) varibles.get(key);
			if (replacement != null){
				output = replaceAll(output, foundString, replacement);
			}
			else{
				output = replaceAll(output, foundString, "No Key!");
			}
		}
		return output;
	}
	
	public String replaceAll(String input, String str, String replacement){
		String output = input;
		String temp = output.replace(str, replacement);
		while(temp != output){
			output = output.replace(str, replacement);
			temp = output.replace(str, replacement);
		}
		return output;
	}

}
