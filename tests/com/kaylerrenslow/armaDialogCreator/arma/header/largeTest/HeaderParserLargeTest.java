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
		HeaderFile headerFile = HeaderParser.parse(HeaderTestUtil.getFile("largeTest/test1Files/largeTestRoot.h"));

		HeaderClass expected = hClass("-root class", null,
				assign("author", w("K-Town")),
				assign("respawn", "BASE"),
				hClass("Header", null,
						assign("gameType", "RPG")
				),
				assign("wreckLimit", "5"),
				//#include "largeTestRoot_configs.h"
				//#include "dir/dirF.h"
				arr_assign("words", array(value(w("bird")), value(w("buh buh bird"))), false),
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
								assign("tag", w("capwar")),
								hClass("Util", null,
										assign("file", w("core\\util")),
										hClass("fetchVehInfo", null)
								)
						)
				),
				hClass("CfgSounds", null,
						arr_assign("sounds", array(), false)
				),
				hClass("CfgDebriefing", null)

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
		HeaderFile headerFile = HeaderParser.parse(HeaderTestUtil.getFile("largeTest/test2Files/largeTest2.h"));

		HeaderClass expected = hClass("-root class", null,
				hClass("DevStuff", null,
						hClass("Errors", null,
								hClass("InvalidArgument", null, assign("msg", w("Invalid Argument"))),
								hClass("IllegalState", null, assign("msg", w("Illegal State"))),
								hClass("ClassNotFound", null, assign("msg", w("Class Not Found")))
						)
				)
		);

		testEquivalence(headerFile, expected);
	}

	@Test
	public void parseHeaderTest3() throws Exception {
		HeaderFile headerFile = HeaderParser.parse(HeaderTestUtil.getFile("largeTest/test3Files/largeTest3.h"));

		HeaderClass hClassOne = hClass(
				"Sound_Man_one",
				null,
				assign("name", w("")),
				arr_assign("sound", array(value(w("\\sounds\\Man\\one.ogg")), value("5"), value("1")), false),
				arr_assign("titles", array(value("0"), value(w("Man says 'one'"))), false)
		);
		HeaderClass hClassTwo = hClass(
				"Sound_Man_two",
				null,
				assign("name", w("")),
				arr_assign("sound", array(value(w("\\sounds\\Man\\two.ogg")), value("5"), value("1")), false),
				arr_assign("titles", array(value("0"), value(w("Man says 'two'"))), false)
		);

		HeaderClass expected = hClass("-root class", null,
				hClass("CfgSounds", null,
						hClassOne,
						hClassTwo
				)
		);

		testEquivalence(headerFile, expected);
	}

}
