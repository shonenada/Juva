package juva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	
	public String render() throws IOException{
		String line = "";
		String lines = "";
		File htmlFile = new File(this._path);
		FileReader reader = new FileReader(htmlFile);
		BufferedReader buffer = new BufferedReader(reader);
		line = buffer.readLine();
		while(line!=null){
			lines = lines + line + "\n";
			line = buffer.readLine();
		}
		String html = parserAll(lines);
		return html;
	}
	
	public String parserAll(String html){
		String output = "";
		String[] lines = html.split("\n");
		for (int i=0;i<lines.length;i++){
			output = output + parserLine(lines[i]);			
		}
		return output;
	}
	
	public String parserLine(String input){
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
			output = output.replace(foundString, replacement);
		}
		return output;
		
	}

}
