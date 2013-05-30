package example;

import java.lang.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import juva.Controller;
import juva.Juva;

import example.controller.Index;


public class JuvaExample extends Juva {

	final static String PROJECT_NAME = "example";
	final static String URL_PREFIX = "/Juva";

	public JuvaExample(){
		super(PROJECT_NAME);
		this.setUrlPrefix(URL_PREFIX);
	}

}
