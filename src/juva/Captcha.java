package juva;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class Captcha extends HttpServlet{

    public static final char[] CHARS = { '1', '2', '3', '4', '5', '6', '7',
        '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
        'X', 'Y', 'Z', };
    
	public static Random random = new Random();
	
	public static String getRandomString() {
	    StringBuffer buffer = new StringBuffer();
	    for (int i = 0; i < 4; i++) {
	        buffer.append(CHARS[random.nextInt(CHARS.length)]);
	    }
	    return buffer.toString();
	}
	
	public static Color getRandomColor() {
	    return new Color(random.nextInt(255), random.nextInt(255), random
	            .nextInt(255));
	}
	
	public static Color getReverseColor(Color c) {
	    return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c
	            .getBlue());
	}

	public void doGet(HttpServletRequest request,
			           HttpServletResponse response)
	            throws IOException{
		response.setHeader("Cache-Control","no-cache");
		response.setContentType("image/jpeg");
	    String randomString = getRandomString();
	    request.getSession(true).setAttribute("randomString", randomString);
	    Color color = getRandomColor();
	    Color reverse = getReverseColor(color);
	    BufferedImage bi = new BufferedImage(100, 30,
	    		                             BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    g.setFont(new Font("Serif", Font.BOLD, 16));
	    g.setColor(color);
	    g.fillRect(0,0,100,30);
	    g.setColor(reverse);
	    g.drawString(randomString,18,20);
	    for (int i = 0,n = random.nextInt(100); i<n; i++) {
	        g.drawRect(random.nextInt(100), random.nextInt(30), 1, 1);
	    }
	    ServletOutputStream out = response.getOutputStream();
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    encoder.encode(bi);
	    out.flush();
	}
}
