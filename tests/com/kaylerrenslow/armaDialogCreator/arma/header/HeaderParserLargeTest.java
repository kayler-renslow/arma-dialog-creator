package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

import static junit.framework.Assert.assertEquals;

/**
 @author Kayler
 @since 04/28/2017 */
public class HeaderParserLargeTest {

	@Test
	public void parseHeaderTest() throws Exception {
		HeaderParser p = new HeaderParser(HeaderTestUtil.getFile("largeTestRoot.h"));
		HeaderFile headerFile = p.parse();

		HeaderClass expected = hClass("-root class", null,
				assign("author", w("K-Town")),
				assign("respawn", "BASE"),
				hClass("Header", null,
						assign("gameType", "RPG")
				),
				assign("wreckLimit", "5"),
				//#include "largeTestRoot_configs.h"
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

		//		System.out.println("EXPECTED:");
		//		System.out.println(expected.getAsString());
		//		System.out.println("----------------------");
		//		System.out.println("ACTUAL:");
		//		System.out.println(actual.getAsString());

		testEquivalence(headerFile, expected);

	}


	@Test
	public void parseHeaderTest1() throws Exception {
		HeaderParser p = new HeaderParser(HeaderTestUtil.getFile("largeTest2.h"));
		HeaderFile headerFile = p.parse();

		Function<String, HeaderAssignment> f = new Function<String, HeaderAssignment>() {
			@Override
			public HeaderAssignment apply(String s) {
				return assign("msg", s);
			}
		};

		HeaderClass expected = hClass("-root class", null,
				hClass("DevStuff", null,
						hClass("Errors", null,
								hClass("InvalidArgument", null, f.apply(w("Invalid Argument"))),
								hClass("IllegalState", null, f.apply(w("Illegal State"))),
								hClass("ClassNotFound", null, f.apply(w("Class Not Found")))
						)
				)
		);

		testEquivalence(headerFile, expected);
	}

	public static void testEquivalence(HeaderFile headerFile, HeaderClass expected) {
		Iterator<HeaderAssignment> iterAssigns = headerFile.getAssignments().iterator();
		for (HeaderAssignment expectedAssign : expected.getAssignments()) {
			if (!iterAssigns.hasNext()) {
				assertEquals("No more items in actual when there should be. Missing assignment: " + expectedAssign.getAsString(), expected.getAssignments().size(), headerFile.getAssignments().size());
			}
			HeaderAssignment actualAssign = iterAssigns.next();
			if (!expectedAssign.equalsAssignment(actualAssign)) {
				assertEquals(expectedAssign, actualAssign);
			}
		}

		Iterator<HeaderClass> iterClass = headerFile.getClasses().iterator();
		for (HeaderClass expectedClass : expected.getNestedClasses()) {
			if (!iterClass.hasNext()) {
				assertEquals("No more items in actual when there should be.  Missing class: " + expectedClass.getAsString(), expected.getNestedClasses().size(), headerFile.getClasses().size());
			}
			HeaderClass actualClass = iterClass.next();
			if (!expectedClass.equalsClass(actualClass)) {
				assertEquals(expectedClass, actualClass);
			}
		}
	}

	private static HeaderClass hClass(String name, String extend, Object... things) {
		ArrayList<HeaderAssignment> assignments = new ArrayList<>();
		ArrayList<HeaderClass> nestedClasses = new ArrayList<>();

		for (Object o : things) {
			if (o instanceof HeaderAssignment) {
				assignments.add((HeaderAssignment) o);
			} else if (o instanceof HeaderClass) {
				nestedClasses.add((HeaderClass) o);
			}
		}

		return hClass(name, extend, assignments, nestedClasses);
	}

	private static <T> ArrayList<T> toList(T... tArr) {
		ArrayList<T> ts = new ArrayList<T>();
		Collections.addAll(ts, tArr);
		return ts;
	}

	private static HeaderClass hClass(String name, String extend, ArrayList<HeaderAssignment> assignments, ArrayList<HeaderClass> nestedClasses) {
		return new AST.HeaderClassNode(name, extend, assignments, nestedClasses);
	}

	private static HeaderArray array(HeaderValue... values) {
		return (HeaderArray) arrItem(values);
	}

	private static HeaderArrayItem arrItem(HeaderValue... values) {
		if (values.length == 1) {
			return new AST.HeaderArrayItemNode(values[0]);
		}
		ArrayList<HeaderArrayItem> items = new ArrayList<>();
		AST.HeaderArrayNode arr = new AST.HeaderArrayNode(items);
		for (HeaderValue v : values) {
			items.add(new AST.HeaderArrayItemNode(v));
		}
		return arr;
	}

	private static HeaderArrayAssignment arr_assign(String vn, HeaderValue v, boolean concat) {
		return new AST.HeaderArrayAssignmentNode(vn, (HeaderArray) v, concat);
	}

	private static HeaderAssignment assign(String vn, String v) {
		return assign(vn, value(v));
	}

	private static HeaderAssignment assign(String vn, HeaderValue v) {
		return new AST.HeaderAssignmentNode(vn, v);
	}

	private static HeaderValue value(String s) {
		return new AST.HeaderValueNode(s);
	}

	private static String w(String s) {
		return "\"" + s + "\"";
	}
}
