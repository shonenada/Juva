package example.controller;

import java.io.IOException;
import java.io.PrintWriter;

import juva.Controller;


public class Index extends Controller {
	
	final static String URL_PATTERN = "/Index";
	
	public Index(){
		super(URL_PATTERN);
	}
	
	public void get(){
		String uri = super._request.getRequestURI();
		PrintWriter out = null;
		try {
			out = super._response.getWriter();
			out.println("Hello world");
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
}
