package org.clash.game.core.piece;

/**
 * 棋子 不变部分
 */
public class PieceConstant
{
	/**
	 * @param type
	 * @param name
	 * @param pieceClass
	 * @param level
	 * @param attackPoint
	 * @param defencePoint
	 * @param wallPoint
	 * @param maxChargeTime
	 */
	public PieceConstant(Type type, String name, Class pieceClass, int level,
							int attackPoint, int defencePoint, int wallPoint, int maxChargeTime)
	{
		super();
		this.type = type;
		this.name = name;
		PieceClass = pieceClass;
		this.level = level;
		this.attackPoint = attackPoint;
		this.defencePoint = defencePoint;
		this.wallPoint = wallPoint;
		this.maxChargeTime = maxChargeTime;
	}

	public Type getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public Class getPieceClass()
	{
		return PieceClass;
	}

	public int getLevel()
	{
		return level;
	}

	public int getAttackPoint()
	{
		return attackPoint;
	}

	public int getDefencePoint()
	{
		return defencePoint;
	}

	public int getWallPoint()
	{
		return wallPoint;
	}

	public int getMaxChargeTime()
	{
		return maxChargeTime;
	}

	/**
	 * 兵种类型
	 */
	private Type type;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 高 /中 /低 级别
	 */
	private Class PieceClass;
	
	/**
	 * 级别
	 */
	private int level;

	/**
	 * 组合后基本攻击力
	 */
	private int attackPoint;
	
	/**
	 * 闲置时生命值
	 */
	private int defencePoint;
	
	/**
	 * 城墙状态防御值
	 */
	private int wallPoint;
	
	/**
	 * 蓄气回合数
	 */
	private int maxChargeTime;
}
