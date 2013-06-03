import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	
	int size;
	
	int levelLine[];
	
	public MosaicMaker(int width, int height, int[] pixel,int _size)
	{
		super();
		this.width = width;
		this.height = height;
		this.pixel = pixel;
		
		parent = new int[width*height];
		distance = new float[width*height];
		levelLine = new int[width*height];
		
		size = _size;
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
	
	int pixelData[];
	private float sSize;
	
	public void paveTile()
	{
		formBelts();
		
		//draw tile
		for(Integer beltIdx : belts.keySet())
		{
			Belt belt = belts.get(beltIdx);
			ArrayList<Point> centralLine  = belt.centralLine;
			
			Point prevTilePoint = null;
			for(Point point : centralLine)
			{
				if(prevTilePoint != null)
				{
					if(pickAsTilePoint(prevTilePoint,point))
					{
						continue;
					}
				}
				
				int pixelIdx = getArrayIndex(point.x,point.y);
				//make rotated rect
				//place pixel in tile
			}
		}
	}

	private boolean pickAsTilePoint(Point prevTilePoint, Point point)
	{
		// TODO Auto-generated method stub
		return false;
	}

	private float getDistance(Point p0, Point p1)
	{
		return (float)p0.distance(p1);
	}

	private void formBelts()
	{
		int regionCounter = 0;
		for(Region region : regions.values())
		{
			//distance in ascending order
			for(Integer dist : region.levelLines.keySet())
			{
				int quotient = (int)dist / (2*this.size);
				int modulo = (int)dist % (2*this.size);
				int beltCounter = (regionCounter << 16) + quotient;
				
				ArrayList<Point> line = region.levelLines.get(dist);				
				
				//green line
				if(modulo == this.size)
				{
					belts.put(beltCounter, new Belt(beltCounter));
					belts.get(beltCounter).centralLine = line;
				}
				
				for(Point point : line)
				{
					int index = getArrayIndex(point.x,point.y);
					pixelData[index] = beltCounter;
				}				
			}
			++regionCounter;
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
					int m = (int)dist % (2*this.size); 
					
					int index = getArrayIndex(point.x,point.y);
					if(m == 0)
					{
						levelLine[index] = 1;
					}
					else if(m == this.size)
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