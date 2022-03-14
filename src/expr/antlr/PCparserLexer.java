package expr.antlr;

// Generated from PCparser.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PCparserLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, TRUE=3, FALSE=4, NOT=5, AND=6, OR=7, ID=8, WS=9;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "TRUE", "FALSE", "NOT", "AND", "OR", "ID", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", null, null, "'!'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "TRUE", "FALSE", "NOT", "AND", "OR", "ID", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public PCparserLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PCparser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\13X\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4&\n\4\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\67\n\5\3"+
		"\6\3\6\3\7\3\7\3\7\3\7\5\7?\n\7\3\b\3\b\3\b\3\b\5\bE\n\b\3\t\6\tH\n\t"+
		"\r\t\16\tI\3\t\3\t\6\tN\n\t\r\t\16\tO\3\n\6\nS\n\n\r\n\16\nT\3\n\3\n\2"+
		"\2\13\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\3\2\4\6\2\62;C\\aac|\4"+
		"\2\13\f\"\"\2a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\3\25\3\2\2\2"+
		"\5\27\3\2\2\2\7%\3\2\2\2\t\66\3\2\2\2\138\3\2\2\2\r>\3\2\2\2\17D\3\2\2"+
		"\2\21M\3\2\2\2\23R\3\2\2\2\25\26\7*\2\2\26\4\3\2\2\2\27\30\7+\2\2\30\6"+
		"\3\2\2\2\31\32\7v\2\2\32\33\7t\2\2\33\34\7w\2\2\34&\7g\2\2\35\36\7V\2"+
		"\2\36\37\7T\2\2\37 \7W\2\2 &\7G\2\2!\"\7V\2\2\"#\7t\2\2#$\7w\2\2$&\7g"+
		"\2\2%\31\3\2\2\2%\35\3\2\2\2%!\3\2\2\2&\b\3\2\2\2\'(\7h\2\2()\7c\2\2)"+
		"*\7n\2\2*+\7u\2\2+\67\7g\2\2,-\7H\2\2-.\7C\2\2./\7N\2\2/\60\7U\2\2\60"+
		"\67\7G\2\2\61\62\7H\2\2\62\63\7c\2\2\63\64\7n\2\2\64\65\7u\2\2\65\67\7"+
		"g\2\2\66\'\3\2\2\2\66,\3\2\2\2\66\61\3\2\2\2\67\n\3\2\2\289\7#\2\29\f"+
		"\3\2\2\2:;\7(\2\2;?\7(\2\2<=\7\61\2\2=?\7^\2\2>:\3\2\2\2><\3\2\2\2?\16"+
		"\3\2\2\2@A\7~\2\2AE\7~\2\2BC\7^\2\2CE\7\61\2\2D@\3\2\2\2DB\3\2\2\2E\20"+
		"\3\2\2\2FH\t\2\2\2GF\3\2\2\2HI\3\2\2\2IG\3\2\2\2IJ\3\2\2\2JN\3\2\2\2K"+
		"L\7*\2\2LN\7+\2\2MG\3\2\2\2MK\3\2\2\2NO\3\2\2\2OM\3\2\2\2OP\3\2\2\2P\22"+
		"\3\2\2\2QS\t\3\2\2RQ\3\2\2\2ST\3\2\2\2TR\3\2\2\2TU\3\2\2\2UV\3\2\2\2V"+
		"W\b\n\2\2W\24\3\2\2\2\13\2%\66>DIMOT\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}