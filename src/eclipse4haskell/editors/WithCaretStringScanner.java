package eclipse4haskell.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;


public class WithCaretStringScanner implements ICharacterScanner
{
	private int caret;
	private int markPoint;
	private int length;
	private String str;

	public WithCaretStringScanner(String str)
	{
		this.str = new String(str);
		length = str.length();
		markPoint = caret = 0;
	}

	//一文字読み込んでキャレット位置の文字をリターンする
	public int read()
	{
		if(caret >= length) return -1;
		int c = str.codePointAt(caret);
		caret++;
		return c;
	}

	//一文字戻る
	public void unread()
	{
		unread(1);
	}

	//任意の文字数戻る
	public void unread(int count)
	{
		if(caret >= count)	caret -= count;
		else 				caret = 0;
	}

	//マークをつける
	public void mark()
	{
		markPoint = caret;
	}

	//マークの位置まで戻る
	public void reset()
	{
		caret = markPoint;
	}

	public char[][] getLegalLineDelimiters()
	{
		return null;
	}

	public int getColumn()
	{
		return caret;
	}

	//skip tp next line
	public void skipToNextLine()
	{
		int c;

		do
		{
			c = read();
		}
		while( c=='\n');
	}
}
