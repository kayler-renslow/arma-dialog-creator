package com.kaylerrenslow.armaDialogCreator.expression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 Test the interpreter's evaluator
 Created on 05/26/2017. */
public class ExpressionInterpreterTest2 {

	@Test
	public void statement1() throws Exception {
		Value expected = Value.Void;
		String eval = "v=1";
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void statement2() throws Exception {
		Value expected = Value.Void;
		String eval = "v=1; b=1";
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void statement3() throws Exception {
		Value expected = Value.Void;
		String eval = "v=1; b=1;";
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void statement4() throws Exception {
		Value expected = new Value.NumVal(1 + 1);
		String eval = "v=1; 1+1;";
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void assignment1() throws Exception {
		Env e = new SimpleEnv();
		Value expected = new Value.NumVal(1);
		String eval = "v=1; 1+1;";
		ExpressionInterpreter.newInstance().evaluateStatements(eval, e);
		assertEquals("", expected, e.getValue("v"));
	}

	@Test
	public void assignment2() throws Exception {
		Env e = new SimpleEnv();
		Value expected = new Value.NumVal(1 + 9);
		String eval = "v=1+9;";
		ExpressionInterpreter.newInstance().evaluateStatements(eval, e);
		assertEquals("", expected, e.getValue("v"));
	}

	@Test
	public void forLoop1() throws Exception {
		String eval = String.join("\n", new String[]{
				"a=0;",
				"for \"_i\" from 0 to 10 do {a=_i;};",
				"a"
		});
		Value expected = new Value.NumVal(10);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void forLoop2() throws Exception {
		String eval = String.join("\n", new String[]{
				"a=0;",
				"for \"_i\" from 0 to 2 do {a=a + _i;};",
				"a"
		});
		Value expected = new Value.NumVal(3);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void forLoopStep1() throws Exception {
		String eval = String.join("\n", new String[]{
				"a=0;",
				"for \"_i\" from 0 to 9 step 5 do {a=_i;};",
				"a"
		});
		Value expected = new Value.NumVal(5);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void forLoopStep2() throws Exception {
		String eval = String.join("\n", new String[]{
				"a=0;",
				"for \"_i\" from 0 to 2 step 2 do {a=a + _i;};",
				"a"
		});
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

	@Test
	public void forLoopStep3() throws Exception {
		String eval = String.join("\n", new String[]{
				"a=0;",
				"for \"_i\" from 9 to 0 step -1 do {a=_i;};",
				"a"
		});
		Value expected = new Value.NumVal(0);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals("", expected, ret);
	}

}