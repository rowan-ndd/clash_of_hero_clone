package org.clash.game.core.piece;

public enum Type
{
	//commons
	NULL(0),
	
	//sylvan units
	HUNTER(1),
	BEAR(2),
	PIXIE(3),
	DRUID(4),
	UNICORN(5),
	DEER(6),
	TREANT(7),
	DRAGON(8),
	SYLVAN_WALL(9);
	
	
    private final int value;

    private Type(int v) 
    {
        this.value = v;
    }	
    
	public int getValue()
	{
		return value;
	}  
}
