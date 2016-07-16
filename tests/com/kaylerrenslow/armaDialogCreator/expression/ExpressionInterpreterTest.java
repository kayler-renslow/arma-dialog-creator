package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 Test the interpreter's evaluator
 Created on 07/15/2016. */
public class ExpressionInterpreterTest {
	/** use this variable for identifier/variable tests */
	private static final int a = 51;

	private static class MyEnv implements Env {

		@Override
		public @Nullable Value getValue(String identifier) {
			return new Value.NumVal(a);
		}
	}

	private static MyEnv env = new MyEnv();

	@Test
	public void evaluate() throws Exception {
		double v = 1 + 1;
		String exp = "1 + 1";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate1() throws Exception {
		double v = 1 + (3 * 2);
		String exp = "1 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate2() throws Exception {
		double v = -1 + (3 * 2);
		String exp = "-1 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate3() throws Exception {
		double v = (3 * 2) * -1;
		String exp = "(3 * 2) * -1";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate4() throws Exception {
		double v = 2 * 2 * (10 * 2);
		String exp = "2 * 2 * (10 * 2)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate5() throws Exception {
		double v = 2 * 20 * -1;
		String exp = "2 * 20 * -1";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate6() throws Exception {
		double v = 100.0 * 5;
		String exp = "100.0 * 5";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate7() throws Exception {
		double v = 0x05 * 100;
		String exp = "0x05 * 100";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate8() throws Exception {
		double v = -0x677 + 5 * 2 - -(800);
		String exp = "-0x677 + 5 * 2 - -(800)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate9() throws Exception {
		double v = 1e111 + (3 * 2);
		String exp = "1e111 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate10() throws Exception {
		double v = -1e111 + (3 * 2);
		String exp = "-1e111 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate11() throws Exception {
		double v = 0.0 * 500 + (-9 - 9);
		String exp = "0.0 * 500 + (- 9 - 9)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate12() throws Exception {
		double v = 4567 + 1e1 * 0x00;
		String exp = "4567 + 1e1 * 0x00";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate13() throws Exception {
		double v = 1 + 1 + 1 * 1e1 - 0x0;
		String exp = "1 + 1 + 1 * 1e1 - 0x0";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate14() throws Exception {
		double v = 1;
		String exp = "1";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate15() throws Exception {
		double v = -0e1;
		String exp = "-0e1";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}


	@Test
	public void evaluate16() throws Exception {
		double v = -1;
		String exp = "-1";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}


	@Test
	public void evaluate17() throws Exception {
		double v = 500 * (1 + (-900 + (500)) * 90) + 1 * (-1);
		String exp = "500 * (1 + (-900 + (500)) * 90) + 1 * (-1)";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}


	@Test
	public void evaluate18() throws Exception {
		double v = (1 + (-(9 * (-(5 + 500 * 9 + 0x0000)))));
		String exp = "(1+(-(9*(-(5 + 500 * 9 + 0x0000)))))";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate19() throws Exception {
		double v = 500 * a + 540;
		String exp = "500 * a + 540";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}


	@Test
	public void evaluate20() throws Exception {
		double v = a * 0;
		String exp = "a * 0";
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", v, ret.v(), 0);
	}

	@Test
	public void evaluate_trick() throws Exception {
		String exp = "e134";//will return whatever the env returns since this isn't a valid number itself
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", a, ret.v(), 0);
	}

	@Test
	public void evaluate_trick1() throws Exception {
		String exp = "x10";//will return whatever the env returns since this isn't a valid number itself
		Value.NumVal ret = (Value.NumVal) ExpressionInterpreter.getInstance().evaluate(exp, env);
		assertEquals("", a, ret.v(), 0);
	}

	@Test
	public void evaluate_error() throws Exception {
		String exp = "(";
		try {
			ExpressionInterpreter.getInstance().evaluate(exp, env);
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}

	}

	@Test
	public void evaluate_error1() throws Exception {
		String exp = "()";
		try {
			ExpressionInterpreter.getInstance().evaluate(exp, env);
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}
	}

	@Test
	public void evaluate_error2() throws Exception {
		String exp = "..1";
		try {
			ExpressionInterpreter.getInstance().evaluate(exp, env);
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}
	}


}