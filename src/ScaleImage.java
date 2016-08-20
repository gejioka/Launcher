import java.io.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.image.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import javax.imageio.*;

public class ScaleImage {
	int width;
	int height;
	String filepath;
	String nameOfImage;
	BufferedImage image;

	public ScaleImage ( int width, int height, String filepath, String nameOfImage ) {

		this.width = width;
		this.height = height;
		this.filepath = filepath;
		this.nameOfImage = nameOfImage;
	}

	public BufferedImage convertFileToBufferedImage ( ) {
		
		try {
			
			File file = new File ( filepath );
			BufferedImage in = ImageIO.read( file );
			BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = newImage.createGraphics();
			
			g.drawImage(in, 0, 0, null);
			g.dispose();
			
			return newImage;
		}catch (Exception e){
		
		}

		return null;
	}

	public void scale( ) {
		 
		int localWidth = image.getWidth();
		int localHeight = image.getHeight();

		try {

			Image im = image.getScaledInstance(width, height,Image.SCALE_SMOOTH);
			BufferedImage newImage = new BufferedImage( width, height, image.getType());
			newImage.createGraphics().drawImage(im,0,0,null);
			image = newImage;
		}catch (Exception e) {
			
			System.out.println(e.getMessage());
		}
	}

	public void createFilefromImage ( BufferedImage bufferedImage, String name ) {
		
		try {
			
			File outputfile =new File(name);
			ImageIO.write(bufferedImage,"png",outputfile);
		}catch (Exception e) {
			
		}	
	}
}
