package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 Evaluates simple mathematical expressions.
 Order of operations is supported as well as identifier lookup.

 @author Kayler
 @since 07/14/2016. */
public class ExpressionInterpreter {

	private static final String[] supportedCommands = new String[]{
			"min", "max", "if", "true", "false", "then", "exitWith", "select",
			"for", "from", "to", "step", "do"
	};

	/**
	 Will return an array of all supported commands. This array is used to make sure the user isn't
	 assigning values to these commands through {@link Env#put(String, Value)}.
	 */
	@NotNull
	public static String[] getSupportedCommands() {
		return supportedCommands;
	}

	/** Get a new instance of the interpreter */
	@NotNull
	public static ExpressionInterpreter newInstance() {
		return new ExpressionInterpreter();
	}

	/** All running evaluators */
	private final ConcurrentLinkedQueue<ExpressionEvaluator> q = new ConcurrentLinkedQueue<>();

	/** Terminate all running evaluators for this interpreter */
	public synchronized void terminateAll() {
		for (ExpressionEvaluator e : q) {
			e.terminate();
		}
	}


	/**
	 Evaluate the given expression String in the given environment. This method will throw errors if the given string contains assignments
	 or multiple expressions separated by semicolons.

	 @param exp expression text to evaluate
	 @param env environment that holds information on all identifiers
	 @return resulted {@link Value} instance
	 @throws ExpressionEvaluationException if the expression couldn't be evaluated (includes if <code>exp==null or exp.trim().length()==0</code>)
	 */
	@NotNull
	public Value evaluate(@Nullable String exp, @NotNull Env env) throws ExpressionEvaluationException {
		if (exp == null || exp.trim().length() == 0) {
			throw new ExpressionEvaluationException(Lang.ApplicationBundle().getString("Expression.error_no_input"));
		}
		ExpressionLexer l = getLexer(exp);
		ExpressionParser p = getParser(new CommonTokenStream(l));

		//prevent ANTLR printing to the console when the expression is invalid
		l.getErrorListeners().clear();
		p.getErrorListeners().clear();

		p.addErrorListener(ErrorListener.INSTANCE);
		p.setErrorHandler(ErrorStrategy.INSTANCE);
		l.addErrorListener(ErrorListener.INSTANCE);

		AST.Expr e;
		try {
			e = p.expression().ast;
		} catch (Exception ex) {
			if (ex instanceof ExpressionEvaluationException) {
				throw ex;
			}
			throw new ExpressionEvaluationException(ex.getMessage(), ex);
		}
		ExpressionEvaluator evaluator = new ExpressionEvaluator(this);
		q.add(evaluator);
		Value ret = evaluator.evaluate(e, env);
		q.remove(evaluator);
		return ret;
	}

	/**
	 Evaluate the given statements as a String in the given environment.

	 @param statements statements text to evaluate
	 @param env environment that holds information on all identifiers
	 @return resulted {@link Value} instance from the last {@link AST.Statement}
	 @throws ExpressionEvaluationException if the expression couldn't be evaluated
	 */
	@NotNull
	public Value evaluateStatements(@Nullable String statements, @NotNull Env env) throws ExpressionEvaluationException {
		if (statements == null || statements.trim().length() == 0) {
			throw new ExpressionEvaluationException(Lang.ApplicationBundle().getString("Expression.error_no_input"));
		}
		ExpressionLexer l = getLexer(statements);
		ExpressionParser p = getParser(new CommonTokenStream(l));

		//prevent ANTLR printing to the console when the expression is invalid
		l.getErrorListeners().clear();
		p.getErrorListeners().clear();

		p.addErrorListener(ErrorListener.INSTANCE);
		p.setErrorHandler(ErrorStrategy.INSTANCE);
		l.addErrorListener(ErrorListener.INSTANCE);

		ExpressionEvaluator evaluator = new ExpressionEvaluator(this);
		try {
			q.add(evaluator);
			Value ret = evaluateStatements(p.statements().lst, env, evaluator);
			q.remove(evaluator);
			return ret;
		} catch (Exception e) {
			if (e instanceof ExpressionEvaluationException) {
				throw e;
			}
			throw new ExpressionEvaluationException(e.getMessage(), e);
		}
	}

	/**
	 Evaluate the given statements as a list of {@link AST.Statement} in the given environment.

	 @param statements statements to evaluate
	 @param env environment that holds information on all identifiers
	 @param evaluator the {@link ExpressionEvaluator} instance to use.
	 @return resulted {@link Value} instance from the last {@link AST.Statement}
	 @throws ExpressionEvaluationException if the expression couldn't be evaluated
	 */
	@NotNull
	protected Value evaluateStatements(@NotNull List<AST.Statement> statements, @NotNull Env env, @NotNull ExpressionEvaluator evaluator) throws ExpressionEvaluationException {
		try {
			return evaluator.evaluate(statements, env);
		} catch (Exception ex) {
			if (ex instanceof ExpressionEvaluationException) {
				throw ex;
			}
			throw new ExpressionEvaluationException(ex.getMessage(), ex);
		}
	}

	@NotNull
	private ExpressionParser getParser(CommonTokenStream stream) {
		return new ExpressionParser(stream);
	}

	@NotNull
	private ExpressionLexer getLexer(String s) {
		return new ExpressionLexer(new ANTLRInputStream(s));
	}


	private static class ErrorStrategy extends DefaultErrorStrategy {

		public static final ANTLRErrorStrategy INSTANCE = new ErrorStrategy();

		@Override
		public void recover(Parser recognizer, RecognitionException e) {
			throw e;
		}

		@Override
		public void reportInputMismatch(Parser recognizer, InputMismatchException e) throws RecognitionException {
			String msg = "mismatched input " + getTokenErrorDisplay(e.getOffendingToken());
			msg += " expecting one of " + e.getExpectedTokens().toString(recognizer.getTokenNames());
			RecognitionException ex = new RecognitionException(msg, recognizer, recognizer.getInputStream(), recognizer.getContext());
			ex.initCause(e);
			throw ex;
		}

		@Override
		public void reportMissingToken(Parser recognizer) {
			beginErrorCondition(recognizer);
			Token t = recognizer.getCurrentToken();
			IntervalSet expecting = getExpectedTokens(recognizer);
			String msg = "missing " + expecting.toString(recognizer.getTokenNames()) + " at " + getTokenErrorDisplay(t);
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

}
