package org.clash.game.core.piece;

public enum Color 
{
	RED(0), GREEN(1), BLUE(2);

    private final int value;

    private Color(int v) 
    {
        this.value = v;
    }
	public int getValue()
	{
		return value;
	}  
}