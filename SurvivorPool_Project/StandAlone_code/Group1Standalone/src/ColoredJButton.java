import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ButtonModel;
import javax.swing.JButton;


public class ColoredJButton extends JButton{
	
	private Color startColor = new Color(192, 192, 192);
	private Color endColor = new Color(82, 82, 82);
	private Color rollOverColor = new Color(255, 143, 89);
	private Color pressedColor = new Color(204, 67, 0);;
	private int outerRoundRectSize = 10;
	private int innerRoundRectSize = 8;
	private GradientPaint GP;
	
	public ColoredJButton(String text){
		super();
		this.setText(text);
	}
	
	/*
	@Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
       
        int h = getHeight();
        int w = getWidth();
        
        ButtonModel model = getModel();
        if (!model.isEnabled()) {
	        setForeground(Color.RED);
	        GP = new GradientPaint(0, 0, new Color(183,225,0), 0, h, new Color(225,225,225),true);
        }else{
        	setForeground(Color.BLUE);
	        if (model.isRollover()) {
	        	GP = new GradientPaint(0, 0, new Color(183,225,0), 0, h, new Color(225,225,225),true);
	        } else {
	        	GP = new GradientPaint(0, 0, startColor, 0, h, endColor, true);
	        } 
        }
	}
	*/
	
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
