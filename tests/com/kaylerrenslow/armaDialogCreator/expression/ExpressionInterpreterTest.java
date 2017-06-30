package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Before;
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
	private static ExpressionInterpreter interpreter;

	@After
	public void tearDown() throws Exception {
		interpreter.shutdownAndDisable();
	}

	@Before
	public void setUp() throws Exception {
		interpreter = ExpressionInterpreter.newInstance();
	}

	@Test
	public void evaluate() throws Exception {

		double expected = 1 + 1;
		String eval = "1 + 1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate1() throws Exception {
		double expected = 1 + (3 * 2);
		String eval = "1 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate2() throws Exception {
		double expected = -1 + (3 * 2);
		String eval = "-1 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate3() throws Exception {
		double expected = (3 * 2) * -1;
		String eval = "(3 * 2) * -1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate4() throws Exception {
		double expected = 2 * 2 * (10 * 2);
		String eval = "2 * 2 * (10 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate5() throws Exception {
		double expected = 2 * 20 * -1;
		String eval = "2 * 20 * -1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate6() throws Exception {
		double expected = 100.0 * 5;
		String eval = "100.0 * 5";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate7() throws Exception {
		double expected = 0x05 * 100;
		String eval = "0x05 * 100";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate8() throws Exception {
		double expected = -0x677 + 5 * 2 - -(800);
		String eval = "-0x677 + 5 * 2 - -(800)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate9() throws Exception {
		double expected = 1e111 + (3 * 2);
		String eval = "1e111 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate10() throws Exception {
		double expected = -1e111 + (3 * 2);
		String eval = "-1e111 + (3 * 2)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate11() throws Exception {
		double expected = 0.0 * 500 + (-9 - 9);
		String eval = "0.0 * 500 + (- 9 - 9)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate12() throws Exception {
		double expected = 4567 + 1e1 * 0x00;
		String eval = "4567 + 1e1 * 0x00";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate13() throws Exception {
		double expected = 1 + 1 + 1 * 1e1 - 0x0;
		String eval = "1 + 1 + 1 * 1e1 - 0x0";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate14() throws Exception {
		double expected = 1;
		String eval = "1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate15() throws Exception {
		double expected = -0e1;
		String eval = "-0e1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}


	@Test
	public void evaluate16() throws Exception {
		double expected = -1;
		String eval = "-1";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}


	@Test
	public void evaluate17() throws Exception {
		double expected = 500 * (1 + (-900 + (500)) * 90) + 1 * (-1);
		String eval = "500 * (1 + (-900 + (500)) * 90) + 1 * (-1)";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}


	@Test
	public void evaluate18() throws Exception {
		double expected = (1 + (-(9 * (-(5 + 500 * 9 + 0x0000)))));
		String eval = "(1+(-(9*(-(5 + 500 * 9 + 0x0000)))))";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluate19() throws Exception {
		double expected = 500 * a + 540;
		String eval = "500 * a + 540";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}


	@Test
	public void evaluate20() throws Exception {
		double expected = a * 0;
		String eval = "a * 0";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void evaluateString3() throws Exception {
		String expected = "\"hello\"";
		String eval = "'hello'";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals(expected, ret.toString());
	}

	@Test
	public void evaluateString4() throws Exception {
		String expected = "\"hello\"";
		String eval = "'''hello'''";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals(expected, ret.getValue());
	}

	@Test
	public void evaluateString5() throws Exception {
		String expected = "\"\"\"hello\"\"\"";
		String eval = "\"\"\"hello\"\"\"";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.toString());
	}

	@Test
	public void evaluateString6() throws Exception {
		String expected = "\"hello world\"";
		String eval = "\"hello \" + \"world\"";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.toString());
	}

	@Test
	public void evaluate_trick() throws Exception {
		String eval = "e134";//will return whatever the env returns since this isn't a valid number itself
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", a, ret.v(), 0);
	}

	@Test
	public void evaluate_trick1() throws Exception {
		String eval = "x10";//will return whatever the env returns since this isn't a valid number itself
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", a, ret.v(), 0);
	}

	@Test
	public void evaluate_error() throws Exception {
		String eval = "(";
		try {
			interpreter.evaluate(eval, env).get();
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}

	}

	@Test
	public void evaluate_error1() throws Exception {
		String eval = "()";
		try {
			interpreter.evaluate(eval, env).get();
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}

	}

	@Test
	public void evaluate_error2() throws Exception {
		String eval = "..1";
		try {
			interpreter.evaluate(eval, env).get();
			assertEquals("Should have failed", false, true);
		} catch (ExpressionEvaluationException e) {
			assertEquals("Succeed", true, true);
		}
	}

	@Test
	public void min1() throws Exception {
		double expected = 1 + 2 * 0 + Math.min(5, 1 + 8 * 8) * 5;
		String eval = "1 + 2 * 0 + (5 min (1 + 8 * 8)) * 5";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void min2() throws Exception {
		double expected = Math.min(5, 9);
		String eval = "5 min 9";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void max1() throws Exception {
		double expected = 1 + 2 * 0 + Math.max(5, 1 + 8 * 8) * 5;
		String eval = "1 + 2 * 0 + (5 max (1 + 8 * 8)) * 5";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void max2() throws Exception {
		double expected = Math.max(5, 9);
		String eval = "5 max 9";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals("", expected, ret.v(), 0);
	}

	@Test
	public void stringConcatFail1() throws Exception {
		String eval = "25 + \"hello\"";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
	}

	@Test
	public void stringConcatFail2() throws Exception {
		String eval = "\"hello\" + 25";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
	}

	@Test
	public void stringConcat() throws Exception {
		String eval = "\"Hello \" + \"World\"";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals("\"Hello World\"", ret.toString());
	}

	@Test
	public void arrayConcat() throws Exception {
		String eval = "[\"Hello\"] + [\"World\"]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(new Value.Array(Arrays.asList(new Value.StringLiteral("Hello"), new Value.StringLiteral("World"))), ret);
	}

	@Test
	public void arraySubtract() throws Exception {
		String eval = "[\"Hello\",\"World\"] - [\"World\"]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(new Value.Array(Collections.singletonList(new Value.StringLiteral("Hello"))), ret);
	}

	@Test
	public void arrayConcatFail1() throws Exception {
		String eval = "25 + [0]";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
	}

	@Test
	public void arrayConcatFail2() throws Exception {
		String eval = "[0] + 25";
		try {
			interpreter.evaluate(eval, env).get();
		} catch (ExpressionEvaluationException e) {
			assertEquals(true, true);
			interpreter.shutdownAndDisable();
			return;
		}
		assertEquals(false, true);
	}

	@Test
	public void equalsOperatorTrue() throws Exception {
		String eval = "1==1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void equalsOperatorFalse() throws Exception {
		String eval = "1==2";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void notEqualsOperatorTrue() throws Exception {
		String eval = "1!=[]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void notEqualsOperatorFalse() throws Exception {
		String eval = "1 != 1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void lessThanOperatorTrue() throws Exception {
		String eval = "1<2";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void lessThanOperatorFalse() throws Exception {
		String eval = "1 <1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void lessThanOrEqualOperatorTrue() throws Exception {
		String eval = "1<=2";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void lessThanOrEqualOperatorFalse() throws Exception {
		String eval = "1 <=0";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void greaterThanOperatorTrue() throws Exception {
		String eval = "2 > 1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void greaterThanOperatorFalse() throws Exception {
		String eval = "1 > 1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void greaterThanOrEqualOperatorTrue() throws Exception {
		String eval = "1>=1";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void greaterThanOrEqualOperatorFalse() throws Exception {
		String eval = "-1 >=0";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void trueCommand() throws Exception {
		String eval = "true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void falseCommand() throws Exception {
		String eval = "false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void emptyArray() throws Exception {
		String eval = "[]";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(new Value.Array(), ret);
	}

	@Test
	public void emptyCode() throws Exception {
		String eval = "{}";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(true, ret instanceof Value.Code && ((Value.Code) ret).getStatements().size() == 0);
	}

	@Test
	public void mod() throws Exception {
		String eval = "1 % 2";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals(true, ArmaPrecision.isEqualTo(1 % 2, ret.v()));
	}

	@Test
	public void exponent1() throws Exception {
		String eval = "1 ^ 2";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();

		assertEquals(true, ArmaPrecision.isEqualTo(1, ret.v()));
	}

	@Test
	public void exponent2() throws Exception {
		String eval = "1 ^ 2 ^ 3";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals(true, ArmaPrecision.isEqualTo(1, ret.v()));
	}

	@Test
	public void exponent3() throws Exception {
		String eval = "5 ^ 2 ^ 3";
		Value.NumVal ret = (Value.NumVal) interpreter.evaluate(eval, env).get();
		assertEquals(true, ArmaPrecision.isEqualTo(Math.pow(Math.pow(5, 2), 3), ret.v()));
	}

	@Test
	public void str1() throws Exception {
		String eval = "str 1";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("\"1\"", ret.toString());
	}

	@Test
	public void str2() throws Exception {
		String eval = "str [1,1]";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("\"[1, 1]\"", ret.toString());
	}

	@Test
	public void str3() throws Exception {
		String eval = "str \"hello\"";
		Value.StringLiteral ret = (Value.StringLiteral) interpreter.evaluate(eval, env).get();
		assertEquals("\"hello\"", ret.toString());
	}

	@Test
	public void logicalAnd_operator_true() throws Exception {
		String eval = "true && true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalAnd_operator_false() throws Exception {
		String eval = "true && false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void logicalAnd_command_true() throws Exception {
		String eval = "true and true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalAnd_command_false() throws Exception {
		String eval = "true and false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void logicalAnd_code() throws Exception {
		String eval = "true && {true}";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalOr_operator_true1() throws Exception {
		String eval = "true || true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalOr_operator_true2() throws Exception {
		String eval = "true || false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalOr_operator_false() throws Exception {
		String eval = "false || false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void logicalOr_command_true() throws Exception {
		String eval = "true or true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalOr_command_false() throws Exception {
		String eval = "false or false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void logicalOr_code() throws Exception {
		String eval = "true or {true}";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalOr_code_shortCircuitTest_ignore() throws Exception {
		Env env = new SimpleEnv();
		String eval = "true or {a=1; true}";
		Value value = interpreter.evaluate(eval, env).get();
		assertEquals("The code should not be evaluated due to short circuit", null, env.getValue("a"));
		assertEquals(Value.True, value);
	}

	@Test
	public void logicalOr_code_shortCircuitTest_execute() throws Exception {
		Env env = new SimpleEnv();
		String eval = "false or {a=1; true}";
		Value value = interpreter.evaluate(eval, env).get();
		assertEquals("The code should be evaluated", new Value.NumVal(1), env.getValue("a"));
		assertEquals(Value.True, value);
	}

	@Test
	public void logicalAnd_code_shortCircuitTest_ignore() throws Exception {
		Env env = new SimpleEnv();
		String eval = "false and {a=1; true}";
		Value value = interpreter.evaluate(eval, env).get();
		assertEquals("The code should not be evaluated due to short circuit", null, env.getValue("a"));
		assertEquals(Value.False, value);
	}

	@Test
	public void logicalAnd_code_shortCircuitTest_execute() throws Exception {
		Env env = new SimpleEnv();
		String eval = "true and {a=1; true}";
		Value value = interpreter.evaluate(eval, env).get();
		assertEquals("The code should be executed", new Value.NumVal(1), env.getValue("a"));
		assertEquals(Value.True, value);
	}

	@Test
	public void logicalCombination_true1() throws Exception {
		String eval = "true && true || false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalCombination_true2() throws Exception {
		String eval = "true || false && false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalCombination_false() throws Exception {
		String eval = "false && true || false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void logicalNot_operator_true() throws Exception {
		String eval = "!false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalNot_operator_false() throws Exception {
		String eval = "!true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}

	@Test
	public void logicalNot_command_true() throws Exception {
		String eval = "not false";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.True, ret);
	}

	@Test
	public void logicalNot_command_false() throws Exception {
		String eval = "not true";
		Value ret = interpreter.evaluate(eval, env).get();
		assertEquals(Value.False, ret);
	}
}