package example;

import juva.Juva;


public class JuvaExample extends Juva {

	final static String PROJECT_NAME = "example";
	final static String URL_PREFIX = "/Juva";

	public JuvaExample(){
		super(PROJECT_NAME);
		this.setUrlPrefix(URL_PREFIX);
	}

}
