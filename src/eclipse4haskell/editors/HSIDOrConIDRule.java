package eclipse4haskell.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/*
 * クラス名, 型名, モジュール名等
 * 先頭が大文字である単語を認識する為のルール
 */
public class HSIDOrConIDRule implements IRule
{

	private IToken conIDToken;
	private IToken idToken;

	HSIDOrConIDRule(IToken iToken, IToken cToken)
	{
		super();
		idToken = iToken;
		conIDToken = cToken;
	}


	public IToken evaluate(ICharacterScanner scanner)
	{
		int c = scanner.read();
		if (Character.isUpperCase(c))
		{
			do
			{
				c = scanner.read();
			}while(Character.isLetterOrDigit(c) || c == '_');
			scanner.unread();
			return conIDToken;
		}

		else
		{
			scanner.unread();
			return Token.UNDEFINED;
		}
	}
}
