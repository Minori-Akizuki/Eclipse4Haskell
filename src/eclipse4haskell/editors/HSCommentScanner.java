package eclipse4haskell.editors;

import java.util.Vector;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;

//…これ, 元々XMLのタグの中をパースする為のルールなんだけど
//もうコメント領域にしちゃったからいらないよね? よね?
public class HSCommentScanner extends RuleBasedScanner
{

	public HSCommentScanner(ColorManager manager)
	{
		Vector<IRule> tRules = new Vector<IRule>();

		tRules.add( new WhitespaceRule(new HSWhitespaceDetector()) );

		IRule[] rules = new IRule[tRules.size()];
		tRules.toArray(rules);
		setRules(rules);
	}
}
