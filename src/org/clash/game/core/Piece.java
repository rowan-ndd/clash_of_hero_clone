package org.clash.game.core;

//TODO:固定部分 和 可变部分 要分开
public class Piece
{
	static public enum UnitClass
	{
		CORE,
		ELITE,
		CHAMPION,
	};
	
	static public enum Color
	{
		RED,
		GREEN,
		BLUE,
	};
	
	public enum UnitType
	{
		//commons
		NULL,
		
		
		//sylvan units
		HUNTER,
		BEAR,
		PIXIE,
		DRUID,
		UNICORN,
		DEER,
		TREANT,
		DRAGON,
		SYLVAN_WALL,
	}
	
	
	private UnitType type;
	
	public Piece()
	{
		type = UnitType.NULL;
	}
	
	private Color color;

	private int attackPoint;
	
	private int defencePoint;
	
	private int wallPoint;
	
	//int maxChargeTime;
	//int[] dimension
	//UnitClass class; it's fixed

	private int elapsedChargeTurn;
	
	private Piece square;
}
