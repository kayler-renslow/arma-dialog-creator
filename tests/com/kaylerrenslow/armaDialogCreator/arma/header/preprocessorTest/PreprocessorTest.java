package com.kaylerrenslow.armaDialogCreator.arma.header.preprocessorTest;

import com.kaylerrenslow.armaDialogCreator.arma.header.DefineMacroContent;
import com.kaylerrenslow.armaDialogCreator.arma.header.DefineMacroContent.DefineValue;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserHelpers;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderTestUtil;
import com.kaylerrenslow.armaDialogCreator.util.UTF8FileWriter;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;

/**
 @author Kayler
 @since 03/22/2017 */
public class PreprocessorTest {
	private static AtomicInteger createdFileCount = new AtomicInteger(0);

	private static <T> T[] array(T... ts) {
		return ts;
	}

	private static HashMap<String, DefineValue> map(@NotNull String[] toMatch, @NotNull String[] replace) {
		if (toMatch.length != replace.length) {
			throw new IllegalArgumentException();
		}
		HashMap<String, DefineValue> map = new HashMap<>(toMatch.length);
		int i = 0;
		for (String key : toMatch) {
			map.put(key, new DefineMacroContent.StringDefineValue(replace[i]));
			i++;
		}
		return map;
	}

	private static HashMap<String, DefineValue> mapParams(@NotNull String macroKey, @NotNull String[] params, @NotNull String macroValue) {
		HashMap<String, DefineValue> map = new HashMap<>(1);
		map.put(macroKey, new DefineMacroContent.ParameterDefineValue(params, macroValue));
		return map;
	}

	@NotNull
	@SafeVarargs
	private static HashMap<String, DefineValue> mergeIntoFirst(@NotNull HashMap<String, DefineValue> first, @NotNull HashMap<String, DefineValue>... others) {
		for (HashMap<String, DefineValue> map : others) {
			first.putAll(map);
		}
		return first;
	}


	@Test
	public void replace() throws Exception {
		String base = "ARG = 1 + ARG2 + ARG3";
		String expect = "a = 1 + b + c";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				map(
						array("ARG", "ARG2", "ARG3"),
						array("a", "b", "c")
				)
		);
	}

	@Test
	public void replace2() throws Exception {
		String base = "ARG = 1 + ARG##2 + ARG3";
		String expect = "a = 1 + a2 + c";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				map(
						array("ARG", "ARG2", "ARG3"),
						array("a", "b", "c")
				)
		);
	}

	@Test
	public void replace3() throws Exception {
		String[] toMatch = {"e", "oo"};
		String[] replace = {"a", "a"};

		String base = "The cow jumped over the moon!";
		String expect = "The cow jumped over the moon!";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				map(toMatch, replace)
		);
	}

	@Test
	public void replace4() throws Exception {
		/*
		#define TWO 2
		#define twenty ##TWO##0
		hint str twenty; // outputs "20"
		*/
		String[] toMatch = {"TWO", "twenty"};
		String[] replace = {"2", "##TWO##0"};

		String base = "hint str twenty;";
		String expect = "hint str 20;";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				map(toMatch, replace)
		);
	}

	@Test
	public void replace5() throws Exception {
		// #define ARG 1
		// #define PARAM(A) A#RG
		// f=PARAM(A)

		String base = "f=PARAM(A)";
		String expect = "f=1";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mergeIntoFirst(
						map(array("ARG"), array("1")),
						mapParams("PARAM", array("A"), "A#RG")
				)
		);
	}


	@Test
	public void replaceParameter() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)word";
		String expect = base; //can't insert before word without ##

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("N", new String[]{"NUMBER"}, "number NUMBER")
		);
	}

	@Test
	public void replaceParameter2() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)";
		String expect = "Hello number 0";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("N", new String[]{"NUMBER"}, "number NUMBER")
		);
	}

	@Test
	public void replaceParameter3() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)##word";
		String expect = "Hello number 0word";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("N", new String[]{"NUMBER"}, "number NUMBER")
		);
	}

	@Test
	public void replaceParameter4() throws Exception {
		//#define BLASTOFF(UNIT,RATE) UNIT setVelocity [0,0,RATE];
		String base = "disableSerialization; BLASTOFF(car,5)";
		String expect = "disableSerialization; car setVelocity [0,0,5];";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("BLASTOFF", new String[]{"UNIT", "RATE"}, "UNIT setVelocity [0,0,RATE];")
		);
	}


	@Test
	public void replaceParameterGlue() throws Exception {
		//#define GLUE(g1,g2) g1##g2
		String base = "GLUE(123,456)";
		String expect = "123456";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("GLUE", new String[]{"g1", "g2"}, "g1##g2")
		);
	}

	@Test
	public void replaceParameterGlue2() throws Exception {
		//#define GLUE(g1,g2) g1##g2
		String base = "model = \\OFP2\\Structures\\Various\\##FOLDER##\\##FOLDER;";
		String expect = "model = \\OFP2\\Structures\\Various\\myFolder\\myFolder;";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				map(array("FOLDER"), array("myFolder"))
		);
	}

	@Test
	public void replaceParameterQuote() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		* #define FOO 456
		* test1 = STRINGIFY(123); //test1 = "123";
		* test2 = STRINGIFY(FOO); //test2 = "456";
		*/
		String base = "test1 = STRINGIFY(123);test2 = STRINGIFY(FOO);";
		String expect = "test1 = \"123\";test2 = \"456\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mergeIntoFirst(
						map(array("FOO"), array("456")),
						mapParams("STRINGIFY", array("s"), "#s")
				)
		);
	}

	@Test
	public void replaceParameterQuote2() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		*/
		String base = "STRINGIFY(quote)";
		String expect = "\"quote\"";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("STRINGIFY", array("s"), "#s")
		);
	}

	@Test
	public void replaceParameterQuote3() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		*/
		String base = "STRINGIFY(quote);";
		String expect = "\"quote\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("STRINGIFY", array("s"), "#s")
		);
	}

	@Test
	public void replaceParameterQuote4() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		*/
		String base = "STRINGIFY(quote)fail";
		String expect = base;

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("STRINGIFY", array("s"), "#s")
		);
	}

	@Test
	public void replaceParameterQuoteLiteral() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "TEST(\"CAR\")";
		String expect = "\"CAR\"";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void replaceParameterQuoteLiteralSemicolon() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "TEST(\"CAR\");";
		String expect = "\"CAR\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void replaceParameterQuoteLiteralAssignment() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "msg=TEST(\"CAR\");";
		String expect = "msg=\"CAR\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void replaceParameterQuoteLiteralAssignment2() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "msg=TEST(\"CAR VROOM VROOM\");";
		String expect = "msg=\"CAR VROOM VROOM\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void __evalTest() throws Exception {
		//#define REP_LAND_ARMOR 300
		//#define VEH(PRICE, REPUTATION, SIDE) price[]=PRICE; reputation=REPUTATION; side=SIDE
		//#define BLUFOR 1
		String base = "VEH({300000}, __EVAL(100 + REP_LAND_ARMOR), BLUFOR);";
		String expect = String.format("price[]=%s; reputation=%s; side=%s;", "{300000}", "400", "1");

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mergeIntoFirst(
						mapParams("VEH", array("PRICE", "REPUTATION", "SIDE"), "price[]=PRICE; reputation=REPUTATION; side=SIDE"),
						map(
								array("REP_LAND_ARMOR", "BLUFOR"),
								array("300", "1")
						)
				)
		);
	}

	@Test
	public void __evalTest2() throws Exception {
		String base = "__EVAL(100 + 2)";
		String expect = "102";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				null
		);
	}

	@Test
	public void commentTest() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "//msg=TEST(\"CAR VROOM VROOM\");";
		String expect = base; //doesn't preprocess comments

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "/*msg=TEST(\"CAR VROOM VROOM\");*/";
		String expect = base; //doesn't preprocess comments

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest2() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "/*msg=TEST(\"CAR fdsfdsfsdsfsVROOM VROOM\");*/";
		String expect = base; //doesn't preprocess comments

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest3() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "/*msg=TEST(\"CAR fdsfdsfsdsfsVROOM VROOM\");*/hello=1;";
		String expect = base; //doesn't preprocess comments

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest4() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "hello=1;/*msg=TEST(\"CAR fdsfdsfsdsfsVROOM VROOM\");*/hello=1;";
		String expect = base; //doesn't preprocess comments

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest5() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "hello=/*msg=TEST(\"CAR fdsfdsfsdsfsVROOM VROOM\");*/ TEST(\"CAR\");";
		String expect = "hello=/*msg=TEST(\"CAR fdsfdsfsdsfsVROOM VROOM\");*/ \"CAR\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest6() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "hello=/*msg=TEST(\"CAR fdsfdsfsdsfsVROOM VROOM\");*/ TEST(\"CAR\");";
		String expect = "hello=/*msg=TEST(\"CAR fdsfdsfsdsfsVROOM VROOM\");*/ \"CAR\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest7() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "hello=/*\n\n\n*/ TEST(\"CAR\");";
		String expect = "hello=/*\n\n\n*/ \"CAR\";";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				mapParams("TEST", array("s"), "s")
		);
	}

	@Test
	public void blockCommentTest8() throws Exception {
		/*
		* #define TEST(s) s;
		*/
		String base = "/**/";
		String expect = "/**/";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				null
		);
	}

	@Test
	public void blockCommentTest9() throws Exception {
		//here, we are checking comment priority. The block comment should take
		//priority since it precedes the line comment
		File f = HeaderTestUtil.getFile("preprocessorTest/blockCommentTest9.h");

		HeaderParserHelpers.assertPreprocessLine(
				getStringFromFile(f),
				f,
				null
		);
	}

	@Test
	public void fullTest() throws Exception {
		String expected = getStringFromFile(HeaderTestUtil.getFile("preprocessorTest/preprocessTestExpected.h"));

		HeaderParserHelpers.assertPreprocessLine(expected, HeaderTestUtil.getFile("preprocessorTest/preprocessTest.h"), null);
	}

	@Test
	public void stringTest() throws Exception {
		//#define SLASH "/"
		//#define URL __EVAL("http:" + SLASH + SLASH + "www.vbs2.com")
		String base = "URL";
		String expect = "\"http://www.vbs2.com\"";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				map(array("SLASH", "URL"), array("\"/\"", "__EVAL(\"http:\" + SLASH + SLASH + \"www.vbs2.com\")"))
		);
	}

	@Test
	public void unknownMacro() throws Exception {
		String base = "#fakeMacro_blah_blah";
		String expect = "";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				null
		);
	}

	@Test
	public void __evalInfiniteLoop() throws Exception {
		//make sure that the preprocessor terminates automatically after a while in an infinite loop
		String base = "__EVAL(for[{},{true},{}] do{})";
		String expect = "";

		BlockingQueue<Boolean> done = new LinkedBlockingDeque<>(1);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HeaderParserHelpers.assertPreprocessLine(
							expect,
							createFileFromText(base),
							null
					);
				} catch (Exception e) {
					done.add(true);
				}
			}
		});

		t.setDaemon(true);
		t.start();
		Boolean success = done.poll(14, TimeUnit.SECONDS);
		assertEquals("Expected infinite loop to run forever and automatically terminate ", Boolean.TRUE, success);
	}

	@Test
	public void __exec1() throws Exception {
		String base = "__EXEC(cat = 5 + 1; lev = 0)";
		String expect = "";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				null
		);
	}

	@Test
	public void __exec2() throws Exception {
		String base = "__EXEC(testVar = 1)";
		String expect = "";

		HeaderParserHelpers.assertPreprocessLine(
				expect,
				createFileFromText(base),
				null
		);
	}

	public static String getStringFromFile(@NotNull File f) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		byte[] arr = new byte[fis.available()];
		int read = fis.read(arr);
		fis.close();

		return new String(arr).replaceAll("\r\n", "\n");
	}

	@NotNull
	private static File createFileFromText(@NotNull String textToWrite) throws Exception {
		File f = HeaderTestUtil.getFile("preprocessorTest/createdFiles/test" + createdFileCount.incrementAndGet() + ".h");
		UTF8FileWriter fos = new UTF8FileWriter(f);
		fos.write(textToWrite);
		fos.flush();
		fos.close();
		return f;
	}

}