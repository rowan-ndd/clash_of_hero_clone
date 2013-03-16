package org.clash.game.core.piece;

/**
 * @author home
 * ���ӿɱ䲿��
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
	 * ��ǰhp
	 */
	int hp;
	
	/**
	 * �Ѿ��������غ���
	 */
	int chargeTurn;
	
	/**
	 * ���ڸ���
	 */
	int x,y;	
	
	/**
	 * ��ɫ
	 */
	Color color;	
	
	/**
	 * ���ɱ䲿��
	 */
	PieceConstant constant;
}
