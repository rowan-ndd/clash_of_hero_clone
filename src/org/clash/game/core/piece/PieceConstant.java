package org.clash.game.core.piece;

/**
 * ���� ���䲿��
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
	 * ��������
	 */
	private Type type;
	
	/**
	 * ����
	 */
	private String name;
	
	/**
	 * �� /�� /�� ����
	 */
	private Class PieceClass;
	
	/**
	 * ����
	 */
	private int level;

	/**
	 * ��Ϻ����������
	 */
	private int attackPoint;
	
	/**
	 * ����ʱ����ֵ
	 */
	private int defencePoint;
	
	/**
	 * ��ǽ״̬����ֵ
	 */
	private int wallPoint;
	
	/**
	 * �����غ���
	 */
	private int maxChargeTime;
}
