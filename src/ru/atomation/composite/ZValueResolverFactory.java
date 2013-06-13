package ru.atomation.composite;
import java.awt.geom.GeneralPath;

/**
 * Utils class, for work with planes
 * @author caiiiycuk
 *
 */
public class ZValueResolverFactory {

	/**
	 * Create a {@link ZValueResolver} for given polygon
	 * and three z coordinates 
	 * @param xpoints 
	 * @param ypoints
	 * @param z1 z-coordinate for point[0,0]
	 * @param z2 z-coordinate for point[1,1]
	 * @param z3 z-coordinate for point[2,2]
	 * @return
	 */
	public static ZDepthResolver createDepthResolver(final int[] xpoints, final int[] ypoints, double z, int beltIdx)
	{
		GeneralPath generalPath = createClippingShape(xpoints, ypoints);
		ZDepthResolver resolver = new ZDepthResolver(z , beltIdx);
		resolver.setClippingShape(generalPath);
		
		return resolver;
	}		

	/**
	 * Create polygon clipping shape
	 * @param xpoints
	 * @param ypoints
	 * @return
	 */
	public static GeneralPath createClippingShape(final int[] xpoints,	final int[] ypoints) {
		if (xpoints.length < 3) {
			throw new IllegalArgumentException("Polygon must have >2 points");
		}
		
		GeneralPath generalPath = new GeneralPath();
		generalPath.moveTo(xpoints[0], ypoints[0]);
		for (int i=1; i<xpoints.length; i++) {
			generalPath.lineTo(xpoints[i], ypoints[i]);
		}
		generalPath.closePath();
		return generalPath;
	}
	
	/**
	 * Create polygon clipping shape
	 * @param xpoints
	 * @param ypoints
	 * @return
	 */
	public static GeneralPath createClippingShape(final double[] xpoints,	final double[] ypoints) {
		if (xpoints.length < 3) {
			throw new IllegalArgumentException("Polygon must have >2 points");
		}
		
		GeneralPath generalPath = new GeneralPath();
		generalPath.moveTo(xpoints[0], ypoints[0]);
		for (int i=1; i<xpoints.length; i++) {
			generalPath.lineTo(xpoints[i], ypoints[i]);
		}
		generalPath.closePath();
		return generalPath;
	}
}
