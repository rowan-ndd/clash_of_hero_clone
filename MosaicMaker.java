import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MosaicMaker
{
	public class Region
	{
		public Region(Integer integer)
		{
			regionIndex = integer;
		}
		
		public ArrayList<Point> points = new ArrayList<Point>();
		public int regionIndex;
		
		Map<Integer,ArrayList<Point>> levelLines = new TreeMap<Integer,ArrayList<Point>>();
	}
	
	int width,height;
	int pixel[];
	int parent[];
	float distance[];	
	
	int levelLine[];
	
	public MosaicMaker(int width, int height, int[] pixel,int tileW, int tileH)
	{
		super();
		this.width = width;
		this.height = height;
		this.pixel = pixel;
		
		parent = new int[width*height];
		distance = new float[width*height];
		levelLine = new int[width*height];
		
		tileWidth = tileW;
		tileHeight = tileH;
		
		beltIdxMat = new int[width*height];
		depthBuf = new int[width*height];
	}
	
	private int getArrayIndex(int x,int y)
	{
		return y * width + x;
	}

	static class Belt
	{
		public Belt(int beltCounter)
		{
			index = beltCounter;
		}
		public int index;
		public ArrayList<Point> centralLine;
	}
	
	Map<Integer,Belt> belts = new TreeMap<Integer,Belt>();
	
	int beltIdxMat[];
	int depthBuf[];
	int outputPixel[];
	private int tileWidth;
	private int tileHeight;

	public int[] paveTile(Graphics2D graphics)
	{
		formBelts();
		//return beltIdxMat;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		Rectangle bounds = graphics.getDeviceConfiguration().getBounds();
		graphics.setColor(Color.BLACK);
		graphics.clearRect(0, 0, bounds.width, bounds.height);

		//draw tile
		int tileIdx = 1;
		int count = 0;//debug counter
		for(Integer beltIdx : belts.keySet())
		{
			Belt belt = belts.get(beltIdx);
			ArrayList<Point> centralLine  = belt.centralLine;
			
			ArrayList<Point> centralPoints = new ArrayList<Point>();;
			for(int i=0;i<centralLine.size();++i)
			{
				Point point = centralLine.get(i);
				if(false == pickNextCentralPoint(centralPoints,point,tileIdx,i == centralLine.size()-1))
				{
					continue;
				}
				else
				{
					centralPoints.add(point);
				}
				
				//int idx = this.getArrayIndex(point.x, point.y);
				//System.out.print("x=" + point.x +  " y=" + point.y + " ");
				tileIdx++;
				
				//depthBuf[idx] = tileIdx;
				processTilePixel4(point, beltIdx, tileIdx, graphics);
				count++;
				//if(count >= 128) return depthBuf;
			}
			//System.out.println("");			
		}
		return null;
	}

	
	public int[] paveTile_origin(Graphics2D graphics)
	{
		formBelts();
		//return beltIdxMat;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle bounds = graphics.getDeviceConfiguration().getBounds();
		graphics.setColor(Color.BLACK);
		graphics.clearRect(0, 0, bounds.width, bounds.height);

		//draw tile
		int tileIdx = 1;
		int count = 0;//debug counter
		for(Integer beltIdx : belts.keySet())
		{
			Belt belt = belts.get(beltIdx);
			ArrayList<Point> centralLine  = belt.centralLine;
			
			ArrayList<Point> centralPoints = new ArrayList<Point>();;
			for(int i=0;i<centralLine.size();++i)
			{
				Point point = centralLine.get(i);
				if(false == pickNextCentralPoint(centralPoints,point,tileIdx,i == centralLine.size()-1))
				{
					continue;
				}
				else
				{
					centralPoints.add(point);
				}
				
				//int idx = this.getArrayIndex(point.x, point.y);
				//System.out.print("x=" + point.x +  " y=" + point.y + " ");
				tileIdx++;
				
				//depthBuf[idx] = tileIdx;
				processTilePixel4(point, beltIdx, tileIdx, graphics);
				count++;
				//if(count >= 128) return depthBuf;
			}
			//System.out.println("");			
		}
		return null;
	}

	private void processTilePixel4(Point tileCenter, Integer beltIdx, int tileIdx, Graphics2D graphics)
	{
		//make rotated rect
		Rectangle rect = new Rectangle(-tileWidth/2,-tileHeight/2,tileWidth,tileHeight);	
		
		//concat transform 
		//transform.rotate(gradient[getArrayIndex(tileCenter.x,tileCenter.y)]);
		
		 // Get the current transform
		 AffineTransform saveAT = graphics.getTransform();
		 // Perform transformation
		 graphics.translate(tileCenter.x,tileCenter.y);	
		 graphics.rotate(gradient[getArrayIndex(tileCenter.x,tileCenter.y)]);
		 // Render
		 int color = ColorTable.bigTable[tileIdx % ColorTable.bigTable.length];
		 graphics.setColor(new Color(color));
		 graphics.fill(rect);
		 // Restore original transform
		 graphics.setTransform(saveAT);		
	}	
	
	private void processTilePixel3(Point tileCenter, Integer beltIdx, int z)
	{
		//make rotated rect
		Rectangle rect = new Rectangle(-tileWidth/2,-tileHeight/2,tileWidth,tileHeight);	
		
		//concat transform 
		AffineTransform transform = new AffineTransform();
		transform.translate(tileCenter.x,tileCenter.y);	
		//transform.rotate(Math.PI*0.25);
		transform.rotate(gradient[getArrayIndex(tileCenter.x,tileCenter.y)]);
		
		Shape shape = transform.createTransformedShape(rect);
		
		Rectangle bound = shape.getBounds();
		
		for(int x=0; x<(int)bound.getWidth(); ++x)
		{
			for(int y=0; y<(int)bound.getHeight(); ++y)
			{
				Point2D pixel = new Point(x + (int)bound.getX(),y + (int)bound.getY());
				
				if((int)pixel.getX() < 0 || (int)pixel.getY() < 0
						|| (int)pixel.getX() >= this.width || (int)pixel.getY() >= this.height)
				{
					continue;
				}
				
				if(shape.contains(pixel))
				{
					//in rect test
					int pixelIdx = getArrayIndex((int)pixel.getX(),(int)pixel.getY());
					//in belt test
					if(beltIdxMat[pixelIdx] == beltIdx)						
					{					
						//do pixel shading here
						//[1]depth test
						int z0 = depthBuf[pixelIdx];
						if(z0 == 0)//keeps older tile
						{
							depthBuf[pixelIdx] = z;
						}
						else if(z0 > z)
						{
							//depthBuf[pixelIdx] = z;
						}
						else
						{
							continue;
						}
					}					
				}
			}
		}
	}	

	private void processTilePixel2(Point tileCenter, Integer beltIdx, int z)
	{
		//make rotated rect
		//place pixel in tile	
		//int R = this.tileHeight;
		for(int x=0;x<this.tileWidth;++x)
		{
			for(int y=0;y<this.tileHeight;++y)
			{
				Point2D pixel = new Point(tileCenter.x - tileWidth/2 + x,tileCenter.y - tileHeight/2 + y);
				
				if((int)pixel.getX() < 0 || (int)pixel.getY() < 0
						|| (int)pixel.getX() >= this.width || (int)pixel.getY() >= this.height)
				{
					continue;
				}
				
				//in rect test
				int pixelIdx = getArrayIndex((int)pixel.getX(),(int)pixel.getY());
				//in belt test
				if(beltIdxMat[pixelIdx] == beltIdx)						
				{					
					//do pixel shading here
					//[1]depth test
					int z0 = depthBuf[pixelIdx];
					if(z0 == 0)//keeps older tile
					{
						depthBuf[pixelIdx] = z;
					}
					else if(z0 > z)
					{
						//depthBuf[pixelIdx] = z;
					}
					else
					{
						continue;
					}
				}
			}
		}
	}	
	
	//by distance and in-rect test
	private boolean pickNextCentralPoint(ArrayList<Point> prevCentralPoints, Point point, int tileIdx, boolean isLastInLine)
	{
		int index = this.getArrayIndex(point.x, point.y);
		
		if(this.depthBuf[index] == tileIdx) return false;
		
		if(isLastInLine)
		{
			return true;
		}
		
		for(Point prevCentralPoint : prevCentralPoints)
		{
			double dist = prevCentralPoint.distance(point);
			if(dist < (this.tileWidth)/2 )//TODO:what is the proper threshold??
			{				
					return false;
			}
		}
		return true;
	}

	private void formBelts()
	{
		int regionCounter = 0;
		for(Region region : regions.values())
		{
			if(!this.selected.contains(region.regionIndex))
			{
				continue;
			}
			
			++regionCounter;
			//distance in ascending order
			for(Integer dist : region.levelLines.keySet())
			{
				int quotient = (int)dist / (this.tileHeight);
				int modulo = (int)dist % (this.tileHeight);
				int beltCounter = (regionCounter << 16) + quotient;
				
				ArrayList<Point> line = region.levelLines.get(dist);				
				
				//green line
				if(modulo == this.tileHeight/2)
				{
					belts.put(beltCounter, new Belt(beltCounter));
					belts.get(beltCounter).centralLine = line;
				}
				
				for(Point point : line)
				{
					int index = getArrayIndex(point.x,point.y);
					beltIdxMat[index] = beltCounter;
				}				
			}			
		}
	}
	
	public int[] getLevelLineMat()
	{
		//put points into each level line due to their distance
		for(Region region : regions.values())
		{			
			for(Point point : region.points)
			{
				int index = getArrayIndex(point.x,point.y);
				int dist = (int)distance[index];
				
				if(region.levelLines.get(dist) == null)
				{
					region.levelLines.put(dist,new ArrayList<Point>());
				}
				region.levelLines.get(dist).add(point);
			}
		}
		
		for(Region region : regions.values())
		{			
			for(Integer dist : region.levelLines.keySet())
			{
				ArrayList<Point> line = region.levelLines.get(dist);
				for(Point point : line)
				{
					int m = (int)dist % (this.tileHeight); 
					
					int index = getArrayIndex(point.x,point.y);
					if(m == 0)
					{
						levelLine[index] = 1;
					}
					else if(m == this.tileHeight/2)
					{
						levelLine[index] = 2;
					}
					else
					{
						levelLine[index] = 0;
					}					
				}
			}
		}		
		
		return levelLine;
	}
	
	float gradient[];
	private Map<Integer,Region> regions; 

	public float[] getGradientMat()
	{
		gradient = new float[width*height];
		
		for (int y = 1; y < height-1; y++) // for each row
		{
			for (int x = 1; x < width-1; x++) // for each column
			{
				float dy,dx;
				dy = distance[getArrayIndex(x,y+1)] - distance[getArrayIndex(x,y-1)];
				dx = distance[getArrayIndex(x+1,y)] - distance[getArrayIndex(x-1,y)];
				gradient[getArrayIndex(x,y)] = (float)Math.atan2(dy,dx);
			}
		}		
		
		return gradient;
	}
	
	public float[] getDistanceMat(UnionFind unionFind)
	{
		//[1]get parents
		Set<Integer> regionIndices = new HashSet<Integer>();	
		for(int i=0;i<pixel.length;++i)
		{
			parent[i] = unionFind.Find(i);
			regionIndices.add(parent[i]);			
		}
		
		regions = new HashMap<Integer,Region>();
		for(Integer integer : regionIndices)
		{
			Region region = new Region(integer);
			regions.put(integer,region);
		}
		
		for (int y = 0; y < height; y++) // for each row
		{
			for (int x = 0; x < width; x++) // for each column
			{
				int index = getArrayIndex(x,y);
				Point point = new Point(x,y);
				regions.get(parent[index]).points.add(point);
			}
		}
		
		//[3]each region distM Chamfer distance transform
		for(Region region : regions.values())
		{
			//3.1 top,left -> bottom,right
			ArrayList<Point> points = region.points;
			int size = points.size();
			
			for(int i=0;i<size;++i)
			{	
				Point point = points.get(i);
				if(false == isBoundary(point))
				{
					int index = getArrayIndex(point.x, point.y);
					distance[index] = updateMinNeighbor0(region.regionIndex, point.x, point.y);
				}
			}
			
			//3.2 bottom,right -> top,left
			for(int i=0;i<size;++i)
			{	
				Point point = points.get(size-1 - i);
				if(false == isBoundary(point))
				{
					int index = getArrayIndex(point.x, point.y);
					distance[index] = updateMinNeighbor1(region.regionIndex, point.x, point.y);
				}
			}
		}		
		
		return distance;
	}

	private float updateMinNeighbor1(int regionIndex, int x, int y)
	{
		float m[] = {1.0f,1.4f,1.0f,1.4f};
		
		int offsetX[] = {+1,+1,0,-1};
		int offsetY[] = {0,+1,+1,+1};
		float distMin = Float.POSITIVE_INFINITY;
		
		for(int i=0;i<4;++i)
		{
			int index = getArrayIndex(x+offsetX[i],y+offsetY[i]);//() * width + ();
			if(parent[index] == regionIndex)
			{
				float d = m[i] + distance[index];
				distMin = Math.min(distMin, d);				
			}
		}		
		
		int index = (y) * width + (x);
		return Math.min(distMin,distance[index]);
	}
	
	private float updateMinNeighbor0(int regionIndex, int x, int y)
	{
		float m[] = {1.0f,1.4f,1.0f,1.4f};
		
		int offsetX[] = {-1,-1,0,+1};
		int offsetY[] = {0,-1,-1,-1};
		float distMin = Float.POSITIVE_INFINITY;
		
		for(int i=0;i<4;++i)
		{
			int index = getArrayIndex(x+offsetX[i],y+offsetY[i]);
			if(parent[index] == regionIndex)
			{
				float d = m[i] + distance[index];
				distMin = Math.min(distMin, d);				
			}
		}		
		
		return distMin;
	}

	private boolean isBoundary(Point point)
	{
		int offsetX[] = {-1,0,1    -1,1,   -1,0,1};  
		int offsetY[] = {-1,-1,-1, 	0,0, 	1,1,1};
		
		for(int i=0; i<offsetX.length; ++i)
		{
			int x = point.x + offsetX[i];
			int y = point.y + offsetY[i];
			
			int currentIndex =  point.y * width + point.x;
			int neighborIndex =  y * width + x;
			
			if(x >= 0 && y >= 0 
					&& x < width && y < height)
			{
				if(parent[currentIndex] != parent[neighborIndex])
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		return false;
	}

	ArrayList<Integer> selected = null;
	
	public void setSelected(ArrayList<Integer> _selected)
	{
		this.selected = _selected;
	}
}