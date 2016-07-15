package com.kaylerrenslow.armaDialogCreator.expression;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/14/2016.
 */
public class ExpressionInterpreter {
	private static final ExpressionInterpreter INSTANCE = new ExpressionInterpreter();
	private static final ExpressionEvaluator evaluator = new ExpressionEvaluator();

	private static class ErrorStrategy extends DefaultErrorStrategy {

		public static final ANTLRErrorStrategy INSTANCE = new ErrorStrategy();

		@Override
		public void recover(Parser recognizer, RecognitionException e) {
			throw e;
		}

		@Override
		public void reportInputMismatch(Parser recognizer, InputMismatchException e) throws RecognitionException {
			String msg = "mismatched input " + getTokenErrorDisplay(e.getOffendingToken());
			msg += " expecting one of "+e.getExpectedTokens().toString(recognizer.getTokenNames());
			RecognitionException ex = new RecognitionException(msg, recognizer, recognizer.getInputStream(), recognizer.getContext());
			ex.initCause(e);
			throw ex;
		}

		@Override
		public void reportMissingToken(Parser recognizer) {
			beginErrorCondition(recognizer);
			Token t = recognizer.getCurrentToken();
			IntervalSet expecting = getExpectedTokens(recognizer);
			String msg = "missing "+expecting.toString(recognizer.getTokenNames()) + " at " + getTokenErrorDisplay(t);
			throw new RecognitionException(msg, recognizer, recognizer.getInputStream(), recognizer.getContext());
		}
	}

	private static class ErrorListener extends BaseErrorListener {

		public static final ErrorListener INSTANCE = new ErrorListener();
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) throws ParseCancellationException {
			throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
		}

	}

	private ExpressionInterpreter() {
	}

	public static ExpressionInterpreter getInstance() {
		return INSTANCE;
	}

	public Value evaluate(String s, Env env) {
		ExpressionLexer l = getLexer(s);
		ExpressionParser p = getParser(new CommonTokenStream(l));

		//prevent ANTLR printing to the console when the expression is invalid
		l.getErrorListeners().clear();
		p.getErrorListeners().clear();

		p.addErrorListener(ErrorListener.INSTANCE);
		p.setErrorHandler(ErrorStrategy.INSTANCE);
		l.addErrorListener(ErrorListener.INSTANCE);

		AST.Expr e;
		try{
			e = p.expression().ast;
		}catch (Exception ex){
			return null;
		}
		return evaluator.evaluate(e, env);
	}

	private ExpressionParser getParser(CommonTokenStream stream) {
		return new ExpressionParser(stream);
	}

	@NotNull
	private ExpressionLexer getLexer(String s) {
		return new ExpressionLexer(new ANTLRInputStream(s));
	}
}
