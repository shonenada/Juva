package example.controller;

import juva.Controller;

public class Index extends Controller {

	final static String[] URL_PATTERN = {"/", "/Index"};

	public Index(){
		super(URL_PATTERN);
	}

	public void get() throws Throwable{
		putVar("home", "Shonenada");
		render("test.html");
	}
	
}
