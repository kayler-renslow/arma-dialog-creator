package com.kaylerrenslow.armaDialogCreator.expression;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

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
		assertEquals(expected, ret);
	}

	@Test
	public void statement2() throws Exception {
		Value expected = Value.Void;
		String eval = "v=1; b=1";
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void statement3() throws Exception {
		Value expected = Value.Void;
		String eval = "v=1; b=1;";
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void statement4() throws Exception {
		Value expected = new Value.NumVal(1 + 1);
		String eval = "v=1; 1+1;";
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void assignment1() throws Exception {
		Env e = new SimpleEnv();
		Value expected = new Value.NumVal(1);
		String eval = "v=1; 1+1;";
		ExpressionInterpreter.newInstance().evaluateStatements(eval, e);
		assertEquals(expected, e.getValue("v"));
	}

	@Test
	public void assignment2() throws Exception {
		Env e = new SimpleEnv();
		Value expected = new Value.NumVal(1 + 9);
		String eval = "v=1+9;";
		ExpressionInterpreter.newInstance().evaluateStatements(eval, e);
		assertEquals(expected, e.getValue("v"));
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
		assertEquals(expected, ret);
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
		assertEquals(expected, ret);
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
		assertEquals(expected, ret);
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
		assertEquals(expected, ret);
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
		assertEquals(expected, ret);
	}

	@Test
	public void forLoopArray1() throws Exception {
		Env env = new SimpleEnv();
		String eval = "for [{_a = 0; _b = 1},{_a <= 10},{_a = _a + 1; _b = _b + _b}] do {};";
		ExpressionInterpreter.newInstance().evaluateStatements(eval, env);
		Value a = env.getValue("_a");
		Value b = env.getValue("_b");
		if (!a.equals(new Value.NumVal(11))) {
			assertEquals("_a is not 11, it is %s" + a.toString(), true, false);
		}
		if (!b.equals(new Value.NumVal(2048))) {
			assertEquals("_b is not 2048, it is %s" + b.toString(), true, false);
		}
	}

	@Test
	public void countWithCondition1() throws Exception {
		String eval = "{true} count []";
		Value expected = new Value.NumVal(0);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void countWithCondition2() throws Exception {
		String eval = "{true} count [1,2]";
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void countWithCondition3() throws Exception {
		String eval = "{false} count [1,2]";
		Value expected = new Value.NumVal(0);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void countArray() throws Exception {
		String eval = "count [1,2]";
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void countString() throws Exception {
		String eval = "count \"hello\"";
		Value expected = new Value.NumVal(5);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void countWithSubtract() throws Exception {
		String eval = "count \"hello\" - 1";
		Value expected = new Value.NumVal(4);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectWithCondition1() throws Exception {
		String eval = "[1,2] select {true}";
		Value expected = new Value.Array(Arrays.asList(new Value.NumVal(1), new Value.NumVal(2)));
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectWithCondition2() throws Exception {
		String eval = "[1,2] select {false}";
		Value expected = new Value.Array();
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectWithBoolTrue() throws Exception {
		String eval = "[1,2] select true";
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectWithBoolFalse() throws Exception {
		String eval = "[1,2] select false";
		Value expected = new Value.NumVal(1);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectSubArray1() throws Exception {
		String eval = "[1,2] select [0,5]";
		Value expected = new Value.Array(Arrays.asList(new Value.NumVal(1), new Value.NumVal(2)));
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectSubArray2() throws Exception {
		String eval = "[1,2] select [0,1]";
		Value expected = new Value.Array(Collections.singletonList(new Value.NumVal(1)));
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectSubString1() throws Exception {
		String eval = "\"japa is the man!\" select [8]";
		Value expected = new Value.StringLiteral("the man!");
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void selectSubString2() throws Exception {
		String eval = "\"japa is the man!\" select [0,7]";
		Value expected = new Value.StringLiteral("japa is");
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifThen1() throws Exception {
		String eval = "a=0;if true then {a=1};a";
		Value expected = new Value.NumVal(1);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifThen2() throws Exception {
		String eval = "a=0;if false then {a=1};a";
		Value expected = new Value.NumVal(0);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifThen3() throws Exception {
		String eval = "a=if false then {1};a";
		Value expected = Value.Void;
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifThen4() throws Exception {
		String eval = "a=if true then {1};a";
		Value expected = new Value.NumVal(1);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifThenElse1() throws Exception {
		String eval = "a=0;if true then {a=1} else {a=2};a";
		Value expected = new Value.NumVal(1);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifThenElse2() throws Exception {
		String eval = "a=0;if false then {a=1} else {a=2};a";
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifThenElse3() throws Exception {
		String eval = "a=if false then {1} else {2};a";
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifArr1() throws Exception {
		String eval = "a=0;if true then [{a=1},{a=2}];a";
		Value expected = new Value.NumVal(1);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifArr2() throws Exception {
		String eval = "a=0;if false then [{a=1},{a=2}];a";
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

	@Test
	public void ifArr3() throws Exception {
		String eval = "a=if false then [{1},{2}];a";
		Value expected = new Value.NumVal(2);
		Value ret = ExpressionInterpreter.newInstance().evaluateStatements(eval, new SimpleEnv());
		assertEquals(expected, ret);
	}

}