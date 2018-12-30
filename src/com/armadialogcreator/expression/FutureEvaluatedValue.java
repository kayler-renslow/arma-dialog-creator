package com.armadialogcreator.expression;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 This class serves as a wrapper around a Future created inside {@link ExpressionInterpreter#evaluate(String, Env)}
 and provides functionality to terminate evaluations.

 @author Kayler
 @since 5/28/2017 */
public class FutureEvaluatedValue implements Future<Value> {

	private final ExpressionInterpreter interpreter;
	private final ExpressionEvaluator evaluator;
	private final Future<Value> future;
	private final AtomicBoolean cancelled = new AtomicBoolean(false);

	/**
	 @param interpreter {@link ExpressionInterpreter} that created this instance
	 @param evaluator {@link ExpressionEvaluator} the evaluator running
	 @param future the future created by the interpreter
	 */
	protected FutureEvaluatedValue(@NotNull ExpressionInterpreter interpreter, @NotNull ExpressionEvaluator evaluator, @NotNull Future<Value> future) {
		this.interpreter = interpreter;
		this.evaluator = evaluator;
		this.future = future;
	}

	/** @return {@link ExpressionInterpreter} that created this value */
	@NotNull
	public ExpressionInterpreter getInterpreter() {
		return interpreter;
	}

	/**
	 Cancel/terminate the evaluation. Will just invoke {@link #cancel()}.

	 @param mayInterruptIfRunning can be any value, will be ignored
	 @return true
	 */
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		cancel();
		return true;
	}

	/**
	 Cancel/terminate the evaluation.
	 */
	public void cancel() {
		evaluator.terminate();
		cancelled.set(true);
		//don't cancel the future. the evaluator will throw an ExpressionEvaluationException from terminate
		//and thus inherently cancel the future
	}

	@Override
	public boolean isCancelled() {
		return cancelled.get();
	}


	@Override
	public boolean isDone() {
		return future.isDone();
	}

	/**
	 Waits if necessary for the computation to complete, and then
	 retrieves its result.

	 @return the computed result
	 @throws ExpressionEvaluationException if the expression couldn't be evaluated (includes if <code>exp==null or exp.trim().length()==0</code>)
	 or an {@link InterruptedException} happened
	 (happens if the current thread was interrupted while waiting)
	 @throws TerminateEvaluationException  if the evaluator created by this method is terminated via
	 {@link ExpressionInterpreter#terminateAll()} or {@link FutureEvaluatedValue#cancel()}
	 */
	@Override
	public Value get() {
		try {
			return future.get();
		} catch (Exception e) {
			if (e.getCause() instanceof ExpressionEvaluationException) {
				throw (ExpressionEvaluationException) e.getCause();
			}
			throw new ExpressionEvaluationException(null, e.getMessage(), e);
		}
	}

	@Override
	public Value get(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
		try {
			return future.get(timeout, unit);
		} catch (Exception e) {
			if (e.getCause() instanceof ExpressionEvaluationException) {
				throw (ExpressionEvaluationException) e.getCause();
			}
			if (e instanceof InterruptedException) {
				throw (InterruptedException) e;
			}
			throw new ExpressionEvaluationException(null, e.getMessage(), e);
		}
	}
}
