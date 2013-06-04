import java.awt.Color;

/*
	Black	#000000	(0,0,0)
	White	#FFFFFF	(255,255,255)
	Red		#FF0000	(255,0,0)
	Lime	#00FF00	(0,255,0)
	Blue	#0000FF	(0,0,255)
	Yellow	#FFFF00	(255,255,0)
	Cyan	#00FFFF	(0,255,255)
	Magenta #FF00FF	(255,0,255)
	Silver	#C0C0C0	(192,192,192)
	Gray	#808080	(128,128,128)
	Maroon	#800000	(128,0,0)
	Olive	#808000	(128,128,0)
	Green	#008000	(0,128,0)
	Purple	#800080	(128,0,128)
	Teal	#008080	(0,128,128)
	Navy	#000080	(0,0,128)
*/

public interface ColorTable
{
	public static Color[] ColorTable = 
	{
		new Color(0,0,0),
		new Color(255,255,255),
		new Color(255,0,0),
		new Color(0,255,0),
		new Color(0,0,255),
		new Color(255,255,0),
		new Color(0,255,255),
		new Color(255,0,255),
		new Color(192,192,192),
		new Color(128,128,128),
		new Color(128,0,0),
		new Color(128,128,0),
		new Color(0,128,0),
		new Color(128,0,128),
		new Color(0,128,128),
		new Color(0,0,128),
	};
}
