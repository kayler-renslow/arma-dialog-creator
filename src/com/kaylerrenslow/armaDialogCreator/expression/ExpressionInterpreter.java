package com.kaylerrenslow.armaDialogCreator.expression;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/14/2016.
 */
public class ExpressionInterpreter {
	private static final ExpressionInterpreter INSTANCE = new ExpressionInterpreter();
	private static final ExpressionEvaluator evaluator = new ExpressionEvaluator();

	private ExpressionInterpreter() {
	}

	public static ExpressionInterpreter getInstance() {
		return INSTANCE;
	}

	public Value evaluate(String s, Env env) {
		ExpressionLexer l = getLexer(s);
		ExpressionParser p = getParser(new CommonTokenStream(l));
		AST.Expr e = p.expression().ast;
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
