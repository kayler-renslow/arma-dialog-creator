package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.main.ADCExecutors;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 Evaluates simple mathematical expressions and some things of Arma 3's scripting language SQF.
 Order of operations is supported as well as identifier lookup.<br>
 All evaluations are done inside a thread pool and in new evaluators.

 @author Kayler
 @since 07/14/2016. */
public class ExpressionInterpreter {

	private static final String[] supportedCommands = new String[]{
			"min", "max", "if", "then", "exitWith", "do", "true", "false",
			"select", "for", "from", "to", "step", "count", "str",
			"safeZoneX", "safeZoneY", "safeZoneW", "safeZoneH", "safeZoneXAbs", "safeZoneWAbs", "getResolution",
			"not", "or", "and", "abs", "format"
	};

	static {
		Arrays.sort(supportedCommands);
	}

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

	/** Thread pool with all executing evaluators */
	private final ExecutorService threadPool = Executors.newFixedThreadPool(2);

	/** Queue of all running/queued {@link ExpressionEvaluator} instances */
	private final LinkedBlockingQueue<ExpressionEvaluator> evaluatorsQ = new LinkedBlockingQueue<>();

	public ExpressionInterpreter() {
		ADCExecutors.registerExecutorService(threadPool);
	}

	/**
	 Shutdown this interpreter and prevent reuse. Be sure to shutdown interpreters that aren't being used anymore!
	 If you don't, the application will take serious performance hits over time. This method will also invoke {@link #terminateAll()}
	 */
	public synchronized void shutdownAndDisable() {
		terminateAll();
		threadPool.shutdownNow();
	}

	/**
	 Terminate all running evaluators for this interpreter. This method is thread-safe.
	 <br>
	 For each evaluator:
	 <ul>
	 <li>If the evaluator is running and this is invoked, the {@link FutureEvaluatedValue#get()} method will throw a {@link TerminateEvaluationException}.</li>
	 <li>If the evaluator is no longer running, nothing will happen.</li>
	 </ul>
	 */
	public synchronized void terminateAll() {
		//keep this method synchronized so that evaluate() and evaluateStatements()
		//can't run at the same time as this method

		for (ExpressionEvaluator evaluator : evaluatorsQ) {
			evaluator.terminate();
		}
		evaluatorsQ.clear();
	}


	/**
	 Evaluate the given expression String in the given environment. This method will create a new evaluator.
	 This method will throw errors if the given string contains assignments
	 or multiple expressions separated by semicolons.<br><br>
	 This method is a non-blocking method and is executed on the interpreter's thread pool.

	 @param exp expression text to evaluate
	 @param env environment that holds information on all identifiers. Please consider thread safety of this environment
	 as the evaluation will run on a different thread
	 @return a {@link Future} class that contains the resulted {@link Value} instance
	 */
	@NotNull
	public synchronized FutureEvaluatedValue evaluate(@Nullable String exp, @NotNull Env env) {
		//this method is synchronized so that no new evaluators can be created if terminateAll() is invoked

		if (threadPool.isShutdown()) {
			throw new IllegalStateException("can't use a shutdown interpreter");
		}

		ExpressionEvaluator evaluator = new ExpressionEvaluator();

		evaluatorsQ.add(evaluator);

		Future<Value> future = threadPool.submit(new Callable<Value>() {
			@Override
			public Value call() throws Exception {
				if (exp == null || exp.trim().length() == 0) {
					throw new ExpressionEvaluationException(null, Lang.ApplicationBundle().getString("Expression.error_no_input"));
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
					throw new ExpressionEvaluationException(null, ex.getMessage(), ex);
				}
				Value v = evaluator.evaluate(e, env);
				evaluatorsQ.remove(evaluator);

				return v;
			}
		});
		return new FutureEvaluatedValue(this, evaluator, future);
	}

	/**
	 Evaluate the given statements as a String in the given environment.
	 This method will create a new evaluator.<br><br>
	 This method is a blocking method.

	 @param statements statements text to evaluate
	 @param env environment that holds information on all identifiers. Please consider thread safety of this environment
	 as the evaluation will run on a different thread
	 @return a {@link Future} like class that contains the resulted {@link Value} instance from the last {@link AST.Statement}
	 {@link #terminateAll()} or {@link FutureEvaluatedValue#cancel(boolean)}
	 */
	@NotNull
	public synchronized FutureEvaluatedValue evaluateStatements(@Nullable String statements, @NotNull Env env) {
		//this method is synchronized so that no new evaluators can be created if terminateAll() is invoked

		if (threadPool.isShutdown()) {
			throw new IllegalStateException("can't use a shutdown interpreter");
		}

		ExpressionEvaluator evaluator = new ExpressionEvaluator();
		evaluatorsQ.add(evaluator);
		Future<Value> future = threadPool.submit(new Callable<Value>() {
			@Override
			public Value call() throws Exception {
				if (statements == null || statements.trim().length() == 0) {
					throw new ExpressionEvaluationException(null, Lang.ApplicationBundle().getString("Expression.error_no_input"));
				}
				ExpressionLexer l = getLexer(statements);
				ExpressionParser p = getParser(new CommonTokenStream(l));

				//prevent ANTLR printing to the console when the expression is invalid
				l.getErrorListeners().clear();
				p.getErrorListeners().clear();

				p.addErrorListener(ErrorListener.INSTANCE);
				p.setErrorHandler(ErrorStrategy.INSTANCE);
				l.addErrorListener(ErrorListener.INSTANCE);


				try {
					List<AST.Statement> statementList = p.statements().lst;
					Value v = evaluateStatements(statementList, env, evaluator);
					evaluatorsQ.remove(evaluator);
					return v;
				} catch (Exception e) {
					if (e instanceof ExpressionEvaluationException) {
						throw e;
					}
					throw new ExpressionEvaluationException(null, e.getMessage(), e);
				}
			}
		});
		return new FutureEvaluatedValue(this, evaluator, future);
	}

	/**
	 Evaluate the given statements as a list of {@link AST.Statement} in the given environment.<br>
	 This will not be executed on a new thread and thus will be a blocking call.

	 @param statements statements to evaluate
	 @param env environment that holds information on all identifiers
	 @param evaluator the {@link ExpressionEvaluator} instance to use.
	 @return resulted {@link Value} instance from the last {@link AST.Statement}
	 @throws ExpressionEvaluationException if the expression couldn't be evaluated
	 */
	@NotNull
	protected Value evaluateStatements(@NotNull List<AST.Statement> statements, @NotNull Env env, @NotNull ExpressionEvaluator evaluator) {
		try {
			return evaluator.evaluate(statements, env);
		} catch (Exception ex) {
			if (ex instanceof ExpressionEvaluationException) {
				throw ex;
			}
			throw new ExpressionEvaluationException(null, ex.getMessage(), ex);
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
