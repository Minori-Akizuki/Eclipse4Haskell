package eclipse4haskell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class HaskellKeywordList
{

	static private Vector<String> list;
	static private Boolean FLAG = false;

	public final static String[] _list =
	{
		//TODO 外部ファイルに移してそこから読み込む様にする
		//予約語
		"case", "class","data", "default", "deriving", "do", "else", "if",
		"import", "in", "infix", "infixl", "infir", "instance", "let", "module",
		"newtype", "of", "then", "type", "where",
	    "Bool", "False", "True", "Maybe", "Nothing", "Just",
	    "Either", "Left", "Right",
	    "Ordering", "LT", "EQ", "GT",
	    "Char", "String", "Int", "Integer", "Float", "Double", "Rational", "IO",
	    "Eq",
	    "Ord", "compare","max", "min",
	    "Enum", "succ", "pred", "toEnum", "fromEnum", "enumFrom", "enumFromThen",
	    "enumFromTo", "enumFromThenTo",
	    "Bounded", "minBound", "maxBound",
	    "Num", "negate", "abs", "signum", "fromInteger",
	    "Real", "toRational",
	    "Integral", "quot", "rem", "div", "mod", "quotRem", "divMod", "toInteger",
	    "Fractional", "recip", "fromRational",
	    "Floating", "pi", "exp", "log", "sqrt2", "logBase", "sin", "cos", "tan",
	             "asin", "acos", "atan", "sinh", "cosh", "tanh", "asinh", "acosh", "atanh",
	    "RealFrac", "properFraction", "truncate", "round", "ceiling", "floor",
	    "RealFloat", "floatRadix", "floatDigits", "floatRange", "decodeFloat",
	    "encodeFloat", "exponent", "significand", "scaleFloat", "isNaN",
	    "isInfinite", "isDenormalized", "isIEEE", "isNegativeZero", "atan2",
	    "Monad", "return", "fail",
	    "Functor", "fmap",
	    "mapM", "mapM_", "sequence", "sequence_",
	    "maybe", "either",
	    "not", "otherwise",
	    "subtract", "even", "odd", "gcd", "lcm",
	    "fromIntegral", "realToFrac",
	    "fst", "snd", "curry", "uncurry", "id", "const", "flip", "until",
	    "asTypeOf", "error", "undefined",
	    "seq"
	};

	private void makeList(String[] wList) throws IOException
	{
		BufferedReader reader = new BufferedReader( new FileReader("HS_K_WORD.txt") );

		Vector<String> _wordsBuf = new Vector<String>();

		String wordBuf;
		while( (wordBuf = reader.readLine()) != null)
		{
			_wordsBuf.add(wordBuf);
		}
		reader.close();

		if(wList!=null)
		{
			for(String word : wList)
			{
				if( !_wordsBuf.contains(word) )	_wordsBuf.add(word);
			}
		}

		list = _wordsBuf;
	}

	public void addList(String[] ws)
	{
		for(String wd: ws)
		{
			if(list.contains(wd))
				list.add(wd);
		}
	}

	public HaskellKeywordList() throws IOException
	{
		if(list==null) makeList(null);
	}

	public String[] getList()
	{
		String[] wList = new String[list.size()];
		list.toArray(wList);
		java.util.Arrays.sort(wList);
		return (String[]) wList;
	}

}
