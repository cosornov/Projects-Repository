import java.awt.AlphaComposite;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;


public class TransparentPanel extends JPanel{
	public TransparentPanel() { 
		super();
	    setOpaque(false); 
	}
	 
	/*This is the over ride code for transparency
	public void paint(Graphics g) { 
	    Graphics2D g2 = (Graphics2D) g.create(); 
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); 
	    super.paint(g2); 
	    g2.dispose(); 
	}*/
	
	@Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f)); 

        GradientPaint gp = new GradientPaint(0, 0,
                getBackground().brighter().brighter().brighter(), 0, getHeight(),
                getBackground()/*.darker()/*.darker()*/);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(grphcs);
    }
}
