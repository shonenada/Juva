package juva;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.controller.Index;


public class Juva extends HttpServlet {

	Router routers = new Router();

	public Juva() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void init() throws ServletException {
		routers.addRouter("/hello", new Index());
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		Controller controller = routers.getController(uri);
		if (controller != null){
			controller.doGet(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}


}
