package eclipse4haskell.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class HSNumberRule implements IRule
{
	private IToken defaultToken;

	HSNumberRule(IToken dToken)
	{
		super();
		defaultToken = dToken;
	}

	/*
	 * 数字リテラル
	 * <degit> = (0[xX])?[a-fA-F0-9]+(.[0-9]*)?([eE]-?[0-9]*)?
	 * Character.isWhitespace(c)
	 * ・パース終了について
	 * 	数字の後に空白があればその文字は成立してる
	 * 	非空白文字でも記号(演算子)だったら成立してる
	 * 	→つまるところ英字以外だったら成立してる? 多分.
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{
		int c = scanner.read();

		if( c == '0' )
		{	//最初の文字がゼロだった場合
			c = scanner.read();
			if( c=='x' || c=='X' )
			{	//16進数の場合
				do{
					c=scanner.read();
				}while( isHexChar(c) );
				//英字以外かどうかを確認
				if(!Character.isLetter(c))
				{
					scanner.unread();
					return defaultToken;
				}
				return Token.UNDEFINED;
			}
			else if( Character.isDigit(c) )
			{	//8進数
				do{
					c=scanner.read();
				}while( '0' <= c && c >= '7' );
				//
				if(!Character.isLetter(c))
				{
					scanner.unread();
					return defaultToken;
				}
				scanner.unread();
				return Token.UNDEFINED;
			}
			if(c!='.')
			{
				scanner.unread();
				return defaultToken;
			}
			else
			{
				c = scanner.read();
			}
		}
		if( Character.isDigit(c) )
		{	//ゼロ以外の場合
			c = readWhileEndDigit(scanner);
			//[0-9]+
			if( c=='.' )
			{
				c = scanner.read();
				if(!Character.isDigit(c))
				{
					scanner.unread();
					scanner.unread();
					return defaultToken;
				}
				c = readWhileEndDigit(scanner);
			}

			if( c=='e'||c=='E' )
			{	//[eE]?
				c = scanner.read();
				if( c=='+' || c=='-' )	//[\+-]?
					c = scanner.read();
				c = readWhileEndDigit(scanner);
					//[0-9]+
			}
			//単語の終わり
			if(!Character.isLetter(c))
			{
				scanner.unread();
				return defaultToken;
			}

			scanner.unread();
			return Token.UNDEFINED;
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	private int readWhileEndDigit(ICharacterScanner scanner)
	{
		int c;
		do
		{
			c = scanner.read();
		}while(Character.isDigit(c));
		return c;
	}

	private boolean isHexChar(int c)
	{
		return ( Character.isDigit(c) || ('a' <= c && c >= 'f') || ('A' <= c && c >= 'F') );
	}

}
