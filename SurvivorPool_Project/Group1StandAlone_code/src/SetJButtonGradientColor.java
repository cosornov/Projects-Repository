import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.UIManager;

import javax.swing.plaf.ColorUIResource;

public class SetJButtonGradientColor
{
	public static void main(String[]args)
	{
	//Create UIManager object
	UIManager manager=new UIManager();


	//IMPORTANT : Key to know : "Button.gradient"
	
	//Set color base on RGB
	//You can get RGB value for your color at "Color picker" at above
	
	//******************************************************
	//Create linked list that will store all gradient information
	//You can try to understand it by change it's value
	LinkedList<Object> a = new LinkedList<Object>();
	a.add(0.3);
	a.add(0.3);
	
	//First colour : R=2,G=208,B=206
	a.add(new ColorUIResource(2,208,206));
	
	//Second colour : R=136,G=255,B=254
	a.add(new ColorUIResource(136,255,254));
	
	//Third colour : R=0,G=142,B=140
	a.add(new ColorUIResource(0,142,140));
	//******************************************************
	
	//Set Button.gradient key with new value
	manager.put("Button.gradient",a);
	
	//Create a button using JButton with text ( BUTTON )
	JButton button=new JButton("BUTTON");
	
	//Create a window using JFrame with title ( JButton gradient color )
	JFrame frame=new JFrame("JButton gradient color");
	
	//Add created button into JFrame
	frame.add(button);
	
	//Set default close operation for JFrame
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	//Set JFrame size
	frame.setSize(330,80);
	
	//Make JFrame visible. So we can see it.
	frame.setVisible(true);
	}
}
