package org.clash.game.core.piece;

/**
 * @author home
 * 棋子可变部分
 */
public class PieceMutable
{
	public int getHp()
	{
		return hp;
	}

	public void setHp(int hp)
	{
		this.hp = hp;
	}

	public int getChargeTurn()
	{
		return chargeTurn;
	}

	public void setChargeTurn(int chargeTurn)
	{
		this.chargeTurn = chargeTurn;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public PieceConstant getConstant()
	{
		return constant;
	}

	/**
	 * 当前hp
	 */
	int hp;
	
	/**
	 * 已经过蓄气回合数
	 */
	int chargeTurn;
	
	/**
	 * 所在格子
	 */
	int x,y;	
	
	/**
	 * 颜色
	 */
	Color color;	
	
	/**
	 * 不可变部分
	 */
	PieceConstant constant;
}
