package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 Test the interpreter's evaluator
 Created on 07/15/2016. */
public class ExpressionInterpreterTest {
	/** use this variable for identifier/variable tests */
	private static final int a = 51;

	private static class MyEnv extends SimpleEnv {

		@Override
		public @Nullable Value getValue(@NotNull String identifier) {
			return new Value.NumVal(a);
		}
	}

	private static MyEnv env = new MyEnv();


	@Test
	public void evaluate() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 1 + 1;
		String eval = "1 + 1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 1 + (3 * 2);
		String eval = "1 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = -1 + (3 * 2);
		String eval = "-1 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate3() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = (3 * 2) * -1;
		String eval = "(3 * 2) * -1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate4() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 2 * 2 * (10 * 2);
		String eval = "2 * 2 * (10 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate5() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 2 * 20 * -1;
		String eval = "2 * 20 * -1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate6() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 100.0 * 5;
		String eval = "100.0 * 5";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate7() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 0x05 * 100;
		String eval = "0x05 * 100";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate8() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = -0x677 + 5 * 2 - -(800);
		String eval = "-0x677 + 5 * 2 - -(800)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate9() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 1e111 + (3 * 2);
		String eval = "1e111 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate10() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = -1e111 + (3 * 2);
		String eval = "-1e111 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate11() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 0.0 * 500 + (-9 - 9);
		String eval = "0.0 * 500 + (- 9 - 9)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate12() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 4567 + 1e1 * 0x00;
		String eval = "4567 + 1e1 * 0x00";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate13() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 1 + 1 + 1 * 1e1 - 0x0;
		String eval = "1 + 1 + 1 * 1e1 - 0x0";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate14() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 1;
		String eval = "1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate15() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = -0e1;
		String eval = "-0e1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}


	@Test
	public void evaluate16() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = -1;
		String eval = "-1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}


	@Test
	public void evaluate17() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 500 * (1 + (-900 + (500)) * 90) + 1 * (-1);
		String eval = "500 * (1 + (-900 + (500)) * 90) + 1 * (-1)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}


	@Test
	public void evaluate18() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = (1 + (-(9 * (-(5 + 500 * 9 + 0x0000)))));
		String eval = "(1+(-(9*(-(5 + 500 * 9 + 0x0000)))))";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate19() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 500 * a + 540;
		String eval = "500 * a + 540";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}


	@Test
	public void evaluate20() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = a * 0;
		String eval = "a * 0";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluateString3() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String expected = "\"hello\"";
		String eval = "'hello'";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals(expected, ret.toString());
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluateString4() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String expected = "\"hello\"";
		String eval = "'''hello'''";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals(expected, ret.getValue());
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluateString5() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String expected = "\"\"\"hello\"\"\"";
		String eval = "\"\"\"hello\"\"\"";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.toString());
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluateString6() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String expected = "\"hello world\"";
		String eval = "\"hello \" + \"world\"";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.toString());
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate_trick() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "e134";//will return whatever the env returns since this isn't a valid number itself
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", a, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate_trick1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "x10";//will return whatever the env returns since this isn't a valid number itself
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", a, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void evaluate_error() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "(";
		try {
			interpreter.evaluate(eval, env).get();
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}
		interpreter.shutdownAndDisable();

	}

	@Test
	public void evaluate_error1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "()";
		try {
			interpreter.evaluate(eval, env).get();
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}
		interpreter.shutdownAndDisable();

	}

	@Test
	public void evaluate_error2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "..1";
		try {
			interpreter.evaluate(eval, env).get();
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}
		interpreter.shutdownAndDisable();
	}

	@Test
	public void min1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 1 + 2 * 0 + Math.min(5, 1 + 8 * 8) * 5;
		String eval = "1 + 2 * 0 + (5 min (1 + 8 * 8)) * 5";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void min2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = Math.min(5, 9);
		String eval = "5 min 9";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void max1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = 1 + 2 * 0 + Math.max(5, 1 + 8 * 8) * 5;
		String eval = "1 + 2 * 0 + (5 max (1 + 8 * 8)) * 5";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void max2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		double expected = Math.max(5, 9);
		String eval = "5 max 9";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void stringConcatFail1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "25 + \"hello\"";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void stringConcatFail2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "\"hello\" + 25";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void stringConcat() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "\"Hello \" + \"World\"";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals("\"Hello World\"", ret.toString());
		interpreter.shutdownAndDisable();
	}

	@Test
	public void arrayConcat() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "[\"Hello\"] + [\"World\"]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(new Value.Array(Arrays.asList(new Value.StringLiteral("Hello"), new Value.StringLiteral("World"))), ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void arraySubtract() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "[\"Hello\",\"World\"] - [\"World\"]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(new Value.Array(Collections.singletonList(new Value.StringLiteral("Hello"))), ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void arrayConcatFail1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "25 + [0]";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void arrayConcatFail2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "[0] + 25";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void equalsOperatorTrue() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1==1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void equalsOperatorFalse() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1==2";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void notEqualsOperatorTrue() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1!=[]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void notEqualsOperatorFalse() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1 != 1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void lessThanOperatorTrue() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1<2";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void lessThanOperatorFalse() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1 <1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void lessThanOrEqualOperatorTrue() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1<=2";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void lessThanOrEqualOperatorFalse() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1 <=0";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void greaterThanOperatorTrue() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "2 > 1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void greaterThanOperatorFalse() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1 > 1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void greaterThanOrEqualOperatorTrue() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1>=1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void greaterThanOrEqualOperatorFalse() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "-1 >=0";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void trueCommand() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void falseCommand() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void emptyArray() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "[]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(new Value.Array(), ret);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void emptyCode() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "{}";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(true, ret instanceof Value.Code && ((Value.Code) ret).getStatements().size() == 0);
		interpreter.shutdownAndDisable();
	}

	@Test
	public void mod() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1 % 2";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals(true, ArmaPrecision.isEqualTo(1 % 2, ret.v()));
		interpreter.shutdownAndDisable();
	}

	@Test
	public void exponent1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1 ^ 2";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();

		assertEquals(true, ArmaPrecision.isEqualTo(1, ret.v()));
		interpreter.shutdownAndDisable();
	}

	@Test
	public void exponent2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "1 ^ 2 ^ 3";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals(true, ArmaPrecision.isEqualTo(1, ret.v()));
		interpreter.shutdownAndDisable();
	}

	@Test
	public void exponent3() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "5 ^ 2 ^ 3";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals(true, ArmaPrecision.isEqualTo(Math.pow(Math.pow(5, 2), 3), ret.v()));
		interpreter.shutdownAndDisable();
	}

	@Test
	public void str1() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "str 1";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("\"1\"", ret.toString());
		interpreter.shutdownAndDisable();
	}

	@Test
	public void str2() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "str [1,1]";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("\"[1, 1]\"", ret.toString());
		interpreter.shutdownAndDisable();
	}

	@Test
	public void str3() throws Exception {
		ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();
		String eval = "str \"hello\"";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("\"hello\"", ret.toString());
		interpreter.shutdownAndDisable();
	}

}