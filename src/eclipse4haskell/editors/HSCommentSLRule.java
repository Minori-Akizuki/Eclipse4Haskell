package eclipse4haskell.editors;

import org.eclipse.jface.text.rules.*;

public class HSCommentSLRule extends EndOfLineRule
{

	public HSCommentSLRule(IToken token)
	{
		super("--", token);
	}
/*
	protected boolean sequenceDetected
	(
			ICharacterScanner scanner,
			char[] sequence,
			boolean eofAd
	)
	{
		int c;
		char ch = (char)scanner.read();
		scanner.unread();
		if(sequence[0] == '-')
		{
			//コレで--の直前の文字が読める…筈?
			scanner.unread();
			scanner.unread();
//			scanner.unread();
			c = scanner.read();
			ch = (char)c;
			scanner.read();
			scanner.read();
			//記号全般を拾いたいんだけど…「一般文字でも空白文字でも無い」ってしないと
			//取れないのかなぁ…
			if( !(Character.isLetterOrDigit(c) || Character.isWhitespace(c) ) )
			{
				return false;
			}
			else
			{
				c = scanner.read();
				ch = (char)c;
				if( !(Character.isLetterOrDigit(c) || Character.isWhitespace(c) ) )
				{
					scanner.unread();
					return false;
				}
			}
		}
		ch = (char)scanner.read();
		scanner.unread();
		return super.sequenceDetected(scanner, sequence, eofAd);
	}
*/
}
