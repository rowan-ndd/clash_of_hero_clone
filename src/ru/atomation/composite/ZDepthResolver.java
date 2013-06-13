package ru.atomation.composite;


import java.awt.Shape;

public class ZDepthResolver implements ZValueResolver
{
	private Shape s = null;
	private boolean b = false;
	private double depth = 0.0f;
	private int beltIndex = 0;
	
	public int getBeltIndex()
	{
		return beltIndex;
	}

	public ZDepthResolver(double z, int beltIdx)
	{
		depth = z;
		beltIndex = beltIdx;
	}

	@Override
	public double resolve(double x, double y)
	{
		/*
		if (false == s.contains(x, y))//out of bound 
		{
			return Double.MAX_VALUE;
		}
		*/
		return depth;
	}

	@Override
	public boolean isAntialiasingEnabled()
	{
		return b;
	}

	@Override
	public void setAntialiasingEnabled(boolean isEnabled)
	{
		b = isEnabled;
	}

	@Override
	public void setClippingShape(Shape shape)
	{
		s = shape;
	}
}
