import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**Custom Image Panel Class**/
	public class ImagePanel extends JPanel{

	    private BufferedImage image;

	    public ImagePanel(String s, int width, int height) {
	       try {   
	    		 image = ImageIO.read(new File("gui_images/backgrounds/"+ s +".jpg"));
	    		 image.getScaledInstance(width, height, 1);
	       } catch (IOException ex) {
	            // handle exception
	       }
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	        g.drawImage(image, 0, 0, null);
	    }
	    
	    public void setImage(String sh) throws IOException{
	    	image = ImageIO.read(new File(sh));
	    	repaint();
	    }
	}