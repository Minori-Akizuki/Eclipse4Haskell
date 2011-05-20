package eclipse4haskell.editors;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import eclipse4haskell.HaskellKeywordList;

public class HSContentAssistProcessor implements IContentAssistProcessor
{
	private Pattern pt = Pattern.compile("[a-zA-Z_'][\\w']*", Pattern.DOTALL);

	public HSContentAssistProcessor()
	{

	}
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset)
	{
		String source = viewer.getDocument().get().substring(0, offset);
		String[] buf = source.split("\\s");	//単語ごとにスプリット
		String input = buf[buf.length-1];	//最後の単語(入力中の単語)を取り出す
		String inRex = input + ".*";		//正規表現用の文字列
		int iLength = input.length();		//入力中文字列長
		Vector<ICompletionProposal> _prop = new Vector<ICompletionProposal>();
		Vector<String> words = new Vector<String>();
			//補完候補用の一時変数

		HaskellKeywordList list;
		try
		{
			list = new HaskellKeywordList();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		for(String word : list.getList() )
		{
			if( word.matches(inRex) )
				words.add(word);
			//単語リストの中から入力中の文字列で始まる単語のみを一時変数に追加
		}

		source = viewer.getDocument().get();
		Matcher matcher = pt.matcher(source);
		//全ての単語にマッチする正規表現検索をセット

		while(matcher.find())
		{
			String word = matcher.group();
			if( word.matches(inRex) && !words.contains(word)) 	words.add(word);
			//まだ入ってない要素があれば入れる
		}

		for(String word : words)
		{
			_prop.add(new CompletionProposal(word, offset-iLength, iLength, word.length()));
		}

		ICompletionProposal[] prop = new ICompletionProposal[_prop.size()];
		_prop.toArray(prop);
		return prop;
		//ベクトルを配列にしてからリターン
	}

//このへんはインタフェースに必要なだけなのでトリアエズnullを帰すだけにしておく.

	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String getErrorMessage() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
