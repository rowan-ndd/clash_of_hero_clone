package org.clash.game.core.piece;

public enum Class
{
	CORE(0),
	ELITE(1),
	CHAMPION(2);
	
    private final int value;

    private Class(int v) 
    {
        this.value = v;
    }

	public int getValue()
	{
		return value;
	}    
};