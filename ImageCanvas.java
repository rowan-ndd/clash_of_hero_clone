import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


//
// Canvas part for drawing the image and the segmented image.
// Indicate various statistics too.
//
class ImageCanvas extends Canvas implements MouseListener
{

	Srmjava applet;

	ImageCanvas(Srmjava applet)
	{
		this.applet = applet;
		setBackground(Color.white);
		this.addMouseListener(this);
	}

	
	int imgSegOriginX;
	int imgSegOriginY;
	int drawingW,drawingH;
	
	public void paint(Graphics g)
	{
		int w;
		int h;
		double scale;

		g.setColor(Color.red);

		// Preserve aspect ratio
		w = getSize().width / 2;
		h = (int) (applet.aspectratio * getSize().width / 2.0);

		if (h < applet.appleth - 140)
			scale = 1.0;
		else
			scale = (applet.appleth - 140) / (double) h;

		drawingW = w = (int) (scale * w);
		drawingH = h = (int) (scale * h);		
		
		// Draw the source image
		g.drawImage(applet.img, 0, 0, w, h, this);

		// Draw the segmented image
		imgSegOriginX = getSize().width / 2 + 10;
		imgSegOriginY = 0;
		g.drawImage(applet.imgseg, imgSegOriginX, 0, w, h, this);

		g.setFont(applet.helveticafont2);
		g.drawString("Q=" + applet.Q, 5, 50);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(3F));
		g2d.drawRect( imgSegOriginX, 0, w, h);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int relativeX = e.getX() - imgSegOriginX;
		int relativeY = e.getY() - imgSegOriginY;
		System.out.println("relative: " + relativeX + " " + relativeY);
	
		int actualW = applet.img.getWidth(this);
		int actualH = applet.img.getHeight(this);
		
		float ratiox = (float)relativeX/(float)drawingW;
		int actualX =  (int)(ratiox * (float)actualW);
		
		float ratioy = (float)relativeY/(float)drawingH;
		int actualY =  (int)(ratioy * (float)actualH);
		System.out.println("at picture: " + actualX + " " + actualY);
		
		if(applet.inputPanel.allowMouseSelection)
		{
			applet.markSelection(actualX,actualY);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)	
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

}