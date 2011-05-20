package eclipse4haskell.editors;

import org.eclipse.jface.text.rules.*;
import java.util.*;

/*
 * ソースコードをまず大まかに分ける. 細かいルーリングは後で行う.
 *ここで受け付けるパーティショナがIPredicateRule(範囲指定に特化した物)なのはそのため
 *Haskellは単行コメントと複数行コメントが違うシンタックスだから普通に分けちゃってもいい
 */
public class HSPartitionScanner extends RuleBasedPartitionScanner
{
	public final static String HS_COMMENT_ML = "__hs_comment_ml";
	public final static String HS_COMMENT_SL = "__hs_comment_sl";

	public HSPartitionScanner()
	{

		IToken hsCommentML = new Token(HS_COMMENT_ML);
		IToken hsCommentSL = new Token(HS_COMMENT_SL);

		Vector <IPredicateRule> rules = new Vector <IPredicateRule>();
		rules.add(new MultiLineRule("{-", "-}", hsCommentML));
		rules.add(new HSCommentSLRule(hsCommentSL));

		IPredicateRule[] rTemp = new IPredicateRule[rules.size()];
		rules.toArray(rTemp);
		setPredicateRules(rTemp);

	}
}
