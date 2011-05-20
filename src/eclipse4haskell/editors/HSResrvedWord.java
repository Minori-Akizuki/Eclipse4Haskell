package eclipse4haskell.editors;

import java.util.Vector;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class HSResrvedWord implements IRule
{
	private IToken defaultToken;
	private Vector<String> keyword;
	static final private String[] HS_RVWORD =
	{
		"case", "class","data", "default", "deriving", "do", "else", "if",
		"import", "in", "infix", "infixl", "infixr", "instance", "let", "module",
		"newtype", "of", "then", "type", "where"
	};

	HSResrvedWord(IToken dToken)
	{
		super();
		defaultToken = dToken;
		keyword = new Vector<String>(HS_RVWORD.length);
		int i,l=HS_RVWORD.length;
		for(i=0;i<l;i++)
		{
			keyword.add(HS_RVWORD[i]);
		}
	}

	public IToken evaluate(ICharacterScanner scanner)
	{
		int c = scanner.read();
		if (Character.isLowerCase(c))
		{
			StringBuffer value = new StringBuffer();
			do
			{
				value.append((char)c);
				c = scanner.read();
			}while( Character.isLetterOrDigit(c) || c=='\'' );
			scanner.unread();
			String buf = new String(value.toString());
			if( keyword.contains( buf ) && Character.isWhitespace(c))
			{
				return defaultToken;
			}
			else
			{
				return Token.UNDEFINED;
			}
		}
		else
		{
			scanner.unread();
			return Token.UNDEFINED;
		}
	}

}
