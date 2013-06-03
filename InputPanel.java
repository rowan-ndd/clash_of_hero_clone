import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


//
// The applet control panel
//
class InputPanel extends Panel implements ActionListener, ItemListener,
		AdjustmentListener
{

	Button applyButton;
	Button processButton;
	
	Label title;
	Scrollbar slider; // for parameter Q

	Srmjava applet;

	InputPanel(Srmjava applet)
	{

		this.applet = applet;
		this.setBackground(Color.white);

		setLayout(new BorderLayout());

		title = new Label(
				"SRMj - Statistical Region Merging in Java by F. Nielsen and R. Nock",
				Label.CENTER);

		title.setFont(applet.helveticafont1);
		title.setBackground(Color.white);
		add("North", title);

		slider = new Scrollbar(Scrollbar.HORIZONTAL, 32, 10, 1, 1024);
		add("South", slider);
		slider.addAdjustmentListener(this);

		applyButton = new Button("Push me to Segment!");
		applyButton.addActionListener(this);
		add("Center", applyButton);
	}

	// Slider for Q
	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		applet.Q = slider.getValue();
		applet.imageCanvas.repaint();
	}

	public void actionPerformed(ActionEvent ev)
	{
		if (ev.getActionCommand().equals("Push me to Segment!"))
		{
			applyButton.setEnabled(false);
			restore();
			applyButton.setLabel("After Foreground Selection,Click here!");
			applet.inputPanel.allowMouseSelection = true;
			applyButton.setEnabled(false);
		}
		
		if (ev.getActionCommand().equals("After Foreground Selection,Click here!")) 
		{
			applet.inputPanel.allowMouseSelection = false;
			applyButton.setLabel("Push me to Get Distance!");
		}
		
		if (ev.getActionCommand().equals("Push me to Get Distance!"))
		{
			applyButton.setEnabled(false);
			try
			{
				getDitanceMat();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}		
		
		if (ev.getActionCommand().equals("Push me to Get Gradient!"))
		{
			applyButton.setEnabled(false);
			try
			{
				getGradientMat();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}	
		
		if (ev.getActionCommand().equals("Push me to Get Level Line!"))
		{
			applyButton.setEnabled(false);
			try
			{
				getLevelLineMat();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(ev.getActionCommand().equals("Push me to Pave Tiles!"))
		{
			try
			{
				paveTile();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}			
		}
	}
	
	private void paveTile() throws IOException
	{
		ImageProducer ip = new MemoryImageSource(applet.pg.getWidth(), applet.pg.getHeight(),
				applet.rastero, 0, applet.pg.getWidth());
		
		BufferedImage bufferedImage = new BufferedImage(applet.pg.getWidth(), applet.pg.getHeight(),BufferedImage.TYPE_INT_ARGB);		
		applet.imgseg = bufferedImage;	
		mosaicMaker.paveTile();
		applet.imageCanvas.repaint();
	}

	private void getLevelLineMat() throws IOException
	{
		int[] llmat = mosaicMaker.getLevelLineMat();
		
		FileWriter outFile = new FileWriter(".\\level.txt");
		PrintWriter out = new PrintWriter(outFile);
		for (int i = 0; i < applet.h; i++) // for each row
		{
			for (int j = 0; j < applet.w; j++) // for each column
			{
				int index = i * applet.w + j;
				out.print(llmat[index]+" ");
				
				int ll = llmat[index];
				int r = 0,g = 0,b = 0;
				switch(ll)
				{
					case 2:
					{
						r = 0;
						g = 255;
						b = 0;							
					}	
					break;
					
					case 1:
					{
						r = 0;
						g = 0;
						b = 0;						
					}	
					break;

					case 0:
					{
						r = 255;
						g = 255;
						b = 255;											
					}	
					break;					
				}
				int rgba = (0xff000000 | b << 16 | g << 8 | r);
				applet.rastero[index] = rgba;				
			}
			out.println("");
		}
		out.close();
		
		ImageProducer ip = new MemoryImageSource(applet.pg.getWidth(), applet.pg.getHeight(),
				applet.rastero, 0, applet.pg.getWidth());
		applet.imgseg = createImage(ip);		
		
		applet.imageCanvas.repaint();
		applyButton.setEnabled(true);
		applyButton.setLabel("Push me to Pave Tiles!");			
	}
	
	private void getGradientMat() throws IOException
	{
		float gradient[] = mosaicMaker.getGradientMat();
		
		FileWriter outFile = new FileWriter(".\\grad.txt");
		PrintWriter out = new PrintWriter(outFile);
		for (int i = 0; i < applet.h; i++) // for each row
		{
			for (int j = 0; j < applet.w; j++) // for each column
			{
				int index = i * applet.w + j;
				out.print(gradient[index]+" ");
				
				int r = (int) ((gradient[index]+Math.PI)/(2*Math.PI) * 255.f);
				int rgba = (0xff000000 | r << 16 | r << 8 | r);
				applet.rastero[index] = rgba;				
			}
			out.println("");
		}
		out.close();
		
		ImageProducer ip = new MemoryImageSource(applet.pg.getWidth(), applet.pg.getHeight(),
				applet.rastero, 0, applet.pg.getWidth());
		applet.imgseg = createImage(ip);		
		applet.imageCanvas.repaint();
		applyButton.setEnabled(true);
		applyButton.setLabel("Push me to Get Level Line!");		
	}

	MosaicMaker mosaicMaker = null;
	boolean allowMouseSelection = false;	
		
	private void getDitanceMat() throws IOException
	{
		//merge foreground
		if(selected.size() > 1)
		{
			int root = selected.get(0);
			for(int i=1;i<selected.size();++i)
			{
				root = applet.UF.UnionRoot(root,selected.get(i));
			}
		}
		
		mosaicMaker = new MosaicMaker(applet.w, applet.h, applet.raster,16,8);
		mosaicMaker.setSelected(selected);
		
		long t0 = System.currentTimeMillis();
		float dist[] = mosaicMaker.getDistanceMat(applet.UF);
		long t1 = System.currentTimeMillis();
		
		System.out.println("t1: " + (t1 - t0));
		
		float maxDist = -Float.MAX_VALUE;
		for(Float f:dist)
		{
			maxDist = Math.max(maxDist,f);
		}
		
		FileWriter outFile = new FileWriter(".\\dist.txt");
		PrintWriter out = new PrintWriter(outFile);
		
		for (int i = 0; i < applet.h; i++) // for each row
		{
			for (int j = 0; j < applet.w; j++) // for each column
			{
				int index = i * applet.w + j;
				
				int r = (int) (dist[index]/maxDist * 255.f);
				int g = (int) (dist[index]/maxDist * 255.f);
				int b = (int) (dist[index]/maxDist * 255.f);

				int rgba = (0xff000000 | b << 16 | g << 8 | r);
				
				out.print(dist[index] + " ");
				applet.rastero[index] = rgba;
			}
			out.println("");
		}
		out.close();
		
		ImageProducer ip = new MemoryImageSource(applet.pg.getWidth(), applet.pg.getHeight(),
				applet.rastero, 0, applet.pg.getWidth());
		applet.imgseg = createImage(ip);
	
		applet.imageCanvas.repaint();
		applyButton.setEnabled(true);
		applyButton.setLabel("Push me to Get Gradient!");
	}

	// Get the choice item events here
	public void itemStateChanged(ItemEvent e)
	{
	}

	public void restore()
	{
		applet.OneRound();
		applet.imageCanvas.repaint();
		applyButton.setEnabled(true);
	}
	
	ArrayList<Integer> selected = new ArrayList<Integer>();
	
	public void markSelection(int x, int y)
	{
		int index = y * applet.w + x;
		System.out.println("calc:" + y + " " + applet.w + " " + x);
		int parent = applet.UF.Find(index);
		
		//newly selected
		if(false == selected.contains(parent))
		{
			selected.add(parent);
			for (int i = 0; i < applet.h; i++) // for each row
			{
				for (int j = 0; j < applet.w; j++) // for each column
				{
					index = i * applet.w + j;
					parent = applet.UF.Find(index);
					if(selected.contains(parent))
					{
						Color color = new Color(applet.raster[index]);
						int r = 255 - color.getRed();
						int g = 255 - color.getGreen();
						int b = 255 - color.getBlue();
						
						int rgba = (0xff000000 | b << 16 | g << 8 | r);
						
						applet.rastero[index] = 0x00FFFFff ^ applet.raster[index];
						//applet.rastero[index] = rgba;
					}
				}
			}
			
			ImageProducer ip = new MemoryImageSource(applet.pg.getWidth(), applet.pg.getHeight(),
					applet.rastero, 0, applet.pg.getWidth());
			applet.imgseg = createImage(ip);			
			applet.imageCanvas.repaint();	
			
			applyButton.setEnabled(true);			
		}

	}
}