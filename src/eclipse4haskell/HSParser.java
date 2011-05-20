package eclipse4haskell;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.TreeNode;

public class HSParser
{

	private TreeNode[] outLine;

	public TreeNode[] parse(String string)
	{
		Vector<TreeNode> tempVTree = new Vector<TreeNode>();
		parseModuleDeclaration(string,tempVTree);
		//モジュール宣言以下の検出
		outLine = new TreeNode[tempVTree.size()];
		tempVTree.toArray(outLine);

		return outLine;
	}

	/*
	 * コメント削除留意点
	 * ・文に二つ以上の連続したハイフンが現れたらそこから行末まではコメント
	 * ・文に "{-" が現れたらそこから "-}" まではコメント
	 * ・範囲コメントはネストできる
	 * ・行末コメント内の "{-" は意味をなさない よって "-- {-" からは範囲コメントを開始しない
	 * ・同様に範囲コメント内のダッシュも意味をなさない
	 * ・つまり範囲コメント内の "-- -}" は正しくコメントを終了する
	 * ・ダッシュが正しい字句の一部である時( "-->" ".--" など )はコメントを開始しない ← めんどい
	 * ・全てのコメントは正統な空白文字である.
	 */

	private void parseImportDeclaration(String string, TreeNode parent, Vector<TreeNode> tempVTree)
	{
		Matcher match = Pattern.compile("^import[ \\t]+([A-Z][\\w\\.]+)([ \\t]+\\w*[ \\t]*(\\([ \\t\\w]*\\))??)??$", Pattern.DOTALL | Pattern.MULTILINE).matcher(string);
		TreeNode tempNode;
		String moduleName;
		TreeNode modulet;
		//モジュール宣言の下に成り得る木をとりあえず宣言

		if(match.find())
		{	//一つでもインポート宣言があればインポート部分のツリーを作る
			modulet = new TreeNode( new HSElement("import", "import", match.start(), match.start()+6 ));
			modulet.setParent(parent);
			Vector<TreeNode> tempModName = new Vector<TreeNode>();
			do
			{
				moduleName = match.group(1);	//インポートのモジュール名を抽出
				tempNode =  new TreeNode(new HSElement(moduleName, "", match.start(1), match.end() ));
					//ノードの生成
				tempNode.setParent(modulet);	//親ノードの指定
				tempModName.add(tempNode);	//ベクトルに追加
			}
			while(match.find());

			TreeNode[] temptree = new TreeNode[tempModName.size()];
			tempModName.toArray(temptree);		//インポート群を配列に変換
			modulet.setChildren(temptree);		//配列をインポート枝の子に指定
			tempVTree.add(modulet);
				//インポート宣言を木に追加する
		}
	}

	private void parseOthersDeclaration(String source, TreeNode parent, Vector<TreeNode> tempTree)
	{
		Matcher match = Pattern.compile("^\\S.*?$", Pattern.DOTALL | Pattern.MULTILINE).matcher(source);
		String lastFuncName="";
		String topWord, line, name;
		TreeNode tempNode=null, tempCNode, tempFNode = null;
		Vector<TreeNode> tempCVNode = null, tempFCVNode=null;

		while(match.find())
		{
			line = match.group();
			if	(	line.startsWith("module") | line.startsWith("import")	)
			{
				continue;
			}
			else if(line.matches("^data[ \\t].*") | line.matches("^(new)?type[ \\t].*"))
			{	//データ構成子宣言
				name = line.split("[ \\t]*=[ \\t]*")[0];
				tempNode = new TreeNode( new HSElement(name, "data", match.start(), match.end()));
				tempNode.setParent(parent);
				tempTree.add(tempNode);
			}
			else if(line.matches("^infix(l|r)[ \\t]*.*"))
			{	//演算子順位宣言
				Matcher mFix = Pattern.compile("^infix(l|r)[ \\t]+(([0-9]*[ \\t]+)?)(.*)",Pattern.DOTALL).matcher(line);
				if(mFix.find())
				{
					String[] ops = mFix.group(4 ).split("[ \t]*,[ \t]*"); //カンマで区切る
					Vector<TreeNode> tempVNode = new Vector<TreeNode>();
					int s = match.start(), e = match.end();
					int _s = s, _e = e;
					tempNode = new TreeNode( new HSElement("infix"+mFix.group(1)+" "+mFix.group(2), "", s, e));
					tempNode.setParent(parent);
					for(String op:ops)
					{
						Matcher opm =
							Pattern.compile
								(
									"^((\\w+([ \\t]+\\w+)*[ \\t]*(\\Q"+op+"\\E)[ \\t]*\\w+)|(\\(\\Q"+op+"\\E\\)[ \\t]*(::.*?)$))",
									Pattern.DOTALL|Pattern.MULTILINE
								)	.matcher(source);
						if(opm.find())
						{	//もう一回ソースを走査して, 演算子の型宣言が見つかったらそれを木に追加する
							s=opm.start(0);
							e=opm.end(0);
							op = opm.group();
						}
						else
						{	s=_s;	e=_e;	}
						tempCNode = new TreeNode( new HSElement(op, "", s, e) );
						tempCNode.setParent(tempNode);
						tempVNode.add(tempCNode);
					}
					TreeNode[] tempANode = new TreeNode[tempVNode.size()];
					tempVNode.toArray(tempANode);
					tempNode.setChildren(tempANode);
					tempTree.add(tempNode);
				}
			}
			else if(line.matches("^instance[ \\t].*"))
			{	//インスタンス宣言
//				Matcher mInst = Pattern.compile("^instance[ \\t]+(\\w+([ \\t]+\\w+)*)+([ \\t]+where)", Pattern.DOTALL).matcher(line);
				Matcher mInst = Pattern.compile("^(instance[ \\t]+.*?)[ \\t]+where", Pattern.DOTALL).matcher(line);
				if(mInst.find())
				{
					tempNode = new TreeNode( new HSElement(mInst.group(1), "function", match.start(), match.end()));
					tempNode.setParent(parent);
					tempTree.add(tempNode);
				}
			}

			else
			{	//上のどのパターンにもマッチしなかった場合
				if( line.matches("^(([a-z'_][\\w']*)[ \\t]*::(.*))[ \\t]*(--(.*))?") )
				{	//型宣言の行. このパターンにマッチするって事は新しい関数の宣言って事です.
					//じゃないとコンパイルエラーになるので同じ名前で複数描いてあった時のことは知りません.
					Matcher mFc = Pattern.compile("^([a-z'_][\\w']*)[ \\t]*::(?:.*)",Pattern.DOTALL).matcher(line.replaceFirst("--.*", ""));
					//コメントを落とした行に対して正規表現をマッチさせる.
					//0:行全体 1:関数名
					if(mFc.find())
					{
						if(tempFCVNode != null && tempFCVNode.size()!=0)
						{	//今まで関数宣言があったら
							TreeNode[] tempAFCNode = new TreeNode[tempFCVNode.size()];
							tempFCVNode.toArray(tempAFCNode);
							tempFNode.setChildren(tempAFCNode);
							tempTree.add(tempFNode);
							//関数の子ノードを配列に変換して登録
						}
						lastFuncName = mFc.group(1);
						tempFNode = new TreeNode( new HSElement(mFc.group(), "function", match.start(), match.end()));
						tempFCVNode = new Vector<TreeNode>();
							//関数名の登録, 親ノードの生成, 子ノードの一時変数の準備
					}
				}
				else if( line.matches("^([a-z'_][\\w']*).*") )
				{	//その他関数の宣言
					Matcher mFc = Pattern.compile("^(([a-z'_][\\w']*)([ \\t]+[^=]*)*)(=.*)?",Pattern.DOTALL).matcher(line);
						//0行全体, 1引数部まで, 2関数名のみ, 3引数部のみ, 4イコールから先
					if(mFc.find())
					{
						String fName = mFc.group(2);
						if(!lastFuncName.equals(fName))
						{
							if(tempFCVNode != null && tempFCVNode.size()!=0)
							{	//関数の子ノードが存在したら
								TreeNode[] tempAFCNode = new TreeNode[tempFCVNode.size()];
								tempFCVNode.toArray(tempAFCNode);
								if(tempAFCNode.length>1)tempFNode.setChildren(tempAFCNode);
								tempTree.add(tempFNode);
								//関数の子ノードを配列に変換して登録
							}

							lastFuncName = mFc.group(2);
							tempFNode = new TreeNode( new HSElement(mFc.group(2), "function", match.start(), match.end()));
							tempFCVNode = new Vector<TreeNode>();
							TreeNode tempFCNode = new TreeNode( new HSElement(mFc.group(1), "", match.start(), match.end()) );
							tempFCNode.setParent(tempFNode);
							tempFCVNode.add(tempFCNode);
							//新しい関数の親ノードを生成して子ノードを追加する
						}
						else
						{
							TreeNode tempFCNode = new TreeNode( new HSElement(mFc.group(1), "",match.start(), match.end()) );
							tempFCNode.setParent(tempFNode);
							tempFCVNode.add(tempFCNode);
						}
					}
				}
			}
		}
		if(tempFCVNode != null && tempFCVNode.size()!=0)
		{	//関数の子ノードが存在したら
			TreeNode[] tempAFCNode = new TreeNode[tempFCVNode.size()];
			tempFCVNode.toArray(tempAFCNode);
			if(tempAFCNode.length>1)tempFNode.setChildren(tempAFCNode);
			tempTree.add(tempFNode);
			//関数の子ノードを配列に変換して登録
		}

	}

	private void parseModuleDeclaration(String source, Vector<TreeNode> tree)
	{
		Matcher match = Pattern.compile("^module[ \\t]+([A-Z]\\w+)\\s*(\\([\\s\\w\\.,]*\\))*\\s*where", Pattern.DOTALL | Pattern.MULTILINE).matcher(source);
		TreeNode moduleName;
		if(match.find())
		{	//モジュール宣言が見つかったら名前の部分を作る
 			moduleName = new TreeNode( new HSElement("module "+ match.group(1), "elem", match.start(), match.start()+6 ));
		}
		else
		{
			moduleName = new TreeNode( new HSElement("module Main", "", 0, 0));
		}

		Vector<TreeNode> tempTree = new Vector<TreeNode>();
		parseImportDeclaration(source, moduleName, tempTree);
		parseOthersDeclaration(source, moduleName, tempTree);

		TreeNode[] tArr = new TreeNode[tempTree.size()];
		tempTree.toArray(tArr);
		moduleName.setChildren(tArr);

		tree.add(moduleName);
	}

}
