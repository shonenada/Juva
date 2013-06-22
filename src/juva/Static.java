package juva;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Static extends HttpServlet{

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException{
        String uri = request.getRequestURI();
        int index = uri.indexOf("/static");
        String url = uri.substring(index, uri.length());
        ServletContext context = this.getServletContext();
        String realPath = context.getRealPath(url);
        FileInputStream in = new FileInputStream(realPath);
        ServletOutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = in.read(b)) > 0) {
            out.write(b, 0, len);
        }
        in.close();
        out.close();
    }
}
