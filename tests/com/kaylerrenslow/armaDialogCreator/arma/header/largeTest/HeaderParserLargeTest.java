package com.kaylerrenslow.armaDialogCreator.arma.header.largeTest;

import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderClass;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderFile;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParser;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderTestUtil;
import org.junit.Test;

import static com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserHelpers.*;

/**
 @author Kayler
 @since 04/28/2017 */
public class HeaderParserLargeTest {

	@Test
	public void parseHeaderTest1() throws Exception {
		HeaderFile headerFile = HeaderParser.parse(HeaderTestUtil.getFile("largeTest/test1Files/largeTestRoot.h"), HeaderTestUtil.getTemporaryResultsFile());

		HeaderClass expected = hClass("-root class", null,
				assign("author", wrap("K-Town")),
				assign("respawn", "BASE"),
				hClass("Header", null,
						assign("gameType", "RPG")
				),
				assign("wreckLimit", "5"),
				//#include "largeTestRoot_configs.h"
				//#include "dir/dirF.h"
				arr_assign("words", array(value(wrap("bird")), value(wrap("buh buh bird"))), false),
				hClass("Configuration", null,
						hClass("Reputation", null,
								assign("max", "500"),
								hClass("Earnings", null,
										assign("kill_inf", "1")
								),
								hClass("Penalties", null)
						)

				),
				hClass("RscTitles", null),
				//#include "largeTestRoot_functions.h"
				hClass("CfgFunctions", null,
						hClass("Client_Functions", null,
								assign("tag", wrap("capwar")),
								hClass("Util", null,
										assign("file", wrap("core\\util")),
										hClass("fetchVehInfo", null)
								)
						)
				),
				hClass("CfgSounds", null,
						arr_assign("sounds", array(), false)
				),
				hClass("CfgDebriefing", null,
						assign("novalue", wrap("")))

		);

		//System.out.println("EXPECTED:");
		//System.out.println(expected.getAsString());
		//System.out.println("----------------------");
		//System.out.println("ACTUAL:");
		//System.out.println(headerFile.getAsString());

		testEquivalence(headerFile, expected);

	}


	@Test
	public void parseHeaderTest2() throws Exception {
		HeaderFile headerFile = HeaderParser.parse(HeaderTestUtil.getFile("largeTest/test2Files/largeTest2.h"), HeaderTestUtil.getTemporaryResultsFile());

		HeaderClass expected = hClass("-root class", null,
				hClass("DevStuff", null,
						hClass("Errors", null,
								hClass("InvalidArgument", null, assign("msg", wrap("Invalid Argument"))),
								hClass("IllegalState", null, assign("msg", wrap("Illegal State"))),
								hClass("ClassNotFound", null, assign("msg", wrap("Class Not Found")))
						)
				)
		);

		testEquivalence(headerFile, expected);
	}

	@Test
	public void parseHeaderTest3() throws Exception {
		HeaderFile headerFile = HeaderParser.parse(HeaderTestUtil.getFile("largeTest/test3Files/largeTest3.h"), HeaderTestUtil.getTemporaryResultsFile());

		HeaderClass hClassOne = hClass(
				"Sound_Man_one",
				null,
				assign("name", wrap("")),
				arr_assign("sound", array(value(wrap("\\sounds\\Man\\one.ogg")), value("5"), value("1")), false),
				arr_assign("titles", array(value("0"), value(wrap("Man says 'one'"))), false)
		);
		HeaderClass hClassTwo = hClass(
				"Sound_Man_two",
				null,
				assign("name", wrap("")),
				arr_assign("sound", array(value(wrap("\\sounds\\Man\\two.ogg")), value("5"), value("1")), false),
				arr_assign("titles", array(value("0"), value(wrap("Man says 'two'"))), false)
		);

		HeaderClass expected = hClass("-root class", null,
				hClass("CfgSounds", null,
						hClassOne,
						hClassTwo
				)
		);

		testEquivalence(headerFile, expected);
	}

	@Test
	public void parseHeaderTest4() throws Exception {
		HeaderFile headerFile = HeaderParser.parse(HeaderTestUtil.getFile("largeTest/test4Files/largeTest4.h"), HeaderTestUtil.getTemporaryResultsFile());

		HeaderClass expected = hClass("-root class", null,
				hClass(
						"EmptyClass",
						null
				)
		);

		testEquivalence(headerFile, expected);
	}


}
