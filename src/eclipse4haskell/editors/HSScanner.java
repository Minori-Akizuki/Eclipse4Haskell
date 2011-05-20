package eclipse4haskell.editors;

import java.util.Vector;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

/*
 * コメント以外の部分をスキャニングするクラス
 */
public class HSScanner extends RuleBasedScanner
{
	static final String HS_IDENTIFY = "_hs_identify";

	public HSScanner(ColorManager manager)
	{
		IToken id =
			new Token( new TextAttribute ( manager.getColor(IHSColorConstants.HS_ID) ) );
		IToken conid =
			new Token( new TextAttribute ( manager.getColor(IHSColorConstants.HS_CONID) ) );
		IToken strings =
			new Token( new TextAttribute ( manager.getColor(IHSColorConstants.STRING) ) );
		IToken numbers =
			new Token( new TextAttribute ( manager.getColor(IHSColorConstants.NUMBER) ) );
		IToken resrved =
			new Token( new TextAttribute ( manager.getColor(IHSColorConstants.RESRVED) ) );

		Vector<IRule> tRules = new Vector<IRule>();

		tRules.add( new SingleLineRule("\"","\"",strings, '\\'));
		tRules.add( new HSResrvedWord(resrved) );
		tRules.add( new HSIDOrConIDRule(id, conid) );
		tRules.add( new HSNumberRule(numbers) );
		tRules.add( new WhitespaceRule(new HSWhitespaceDetector()) );		//空白の定義?

		IRule[] rules = new IRule[tRules.size()];
		tRules.toArray(rules);
		setRules(rules);
	}
	/*
	 * 内部仕様について
	 * setRulesで渡した配列にスキャナーを渡してるのは明らか
	 * 多分流れとしては
	 * ・先頭のクラスにスキャナーを渡す
	 * ・何らかのトークンが帰ってきたらそのトークンで文字を装飾しその状態からまた最初のクラスにスキャナーを渡す
	 * ・undefinedが帰ってきたらまた次のクラスにスキャナーを渡す
	 */
}
