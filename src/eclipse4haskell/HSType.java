package eclipse4haskell;

/*
 * 型情報覚書
 * 基本型は型である
 * ある型を0個以上並べた物をリスト型と呼ぶ. これは型である.
 * 複数の型を組にした物をタプル型と呼ぶ. これは型である.
 * ある型から別の型への写像を帰す物を関数型と呼ぶ. これは型である.
 *
 * <Type> := <List>|<Taple>|<Function>|<ConID>
 * <List> := "[" <Type> "]"
 * <Taple> := "(" (<Type> ("," <Type>)* )| ε ")"
 * <Function> := <Type> -> <Type>
 * <ConID> := [A-Z]<IDChar>
 */

public abstract class HSType
{
	public boolean equals(HSType a, HSType b)
	{
		return a.typeName().equals(b.typeName());
	}
	public abstract String typeName();
}

class HSBaseType extends HSType
{
	private String name;

	public HSBaseType(String _name)
	{
		name = _name;
	}

	public String typeName()
	{
		return name;
	}

}

class HSListType extends HSType
{
	private HSType inType;

	public HSListType(HSType t)
	{
		inType = t;
	}

	public String typeName()
	{
		return "[" + inType.typeName() + "]";
	}
}

class HSTapleType extends HSType
{
	private HSType[] inTypes;

	public HSTapleType(HSType[] ts)
	{
		inTypes = ts;
	}

	public String typeName()
	{
		String tStr="(";
		for(HSType t:inTypes)
		{
			tStr += t.typeName() + ",";
		}

		tStr.replaceFirst(",$", ")");

		return tStr;
	}
}

class HSTypeUnit extends HSType
{
	public String typeName() {	return "()"; }
}

class HSTypeFunction extends HSType
{
	HSType typeFrom, typeTo;
	public HSTypeFunction(HSType f,HSType t)
	{
		typeFrom = f;
		typeTo = t;
	}

	public String typeName()
	{
		return typeFrom.typeName() + "->" + typeTo.typeName();
	}
}