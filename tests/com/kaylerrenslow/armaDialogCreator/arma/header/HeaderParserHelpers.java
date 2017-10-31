package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.IndentedStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import static junit.framework.Assert.assertEquals;

/**
 @author Kayler
 @since 04/28/2017 */
public class HeaderParserHelpers {


	public static void assertPreprocessLine(@NotNull String expect, @NotNull File processFile, @Nullable HashMap<String, DefineMacroContent.DefineValue> toInsert) throws Exception {
		Preprocessor p = new Preprocessor(processFile, new HeaderParserContext(HeaderTestUtil.getTemporaryResultsFile()));
		if (toInsert != null) {
			p.defined.putAll(toInsert);
		}
		p.preprocessStack.add(new Preprocessor.PreprocessState(processFile));

		Preprocessor.PreprocessorInputStream stream = p.run();

		byte[] arr = new byte[stream.available()];
		stream.read(arr);
		assertEquals(expect.trim(), new String(arr).trim());
	}

	public static void testEquivalence(HeaderFile headerFile, HeaderClass expected) {
		Iterator<HeaderAssignment> iterAssigns = headerFile.getAssignments().iterator();
		for (HeaderAssignment expectedAssign : expected.getAssignments()) {
			if (!iterAssigns.hasNext()) {
				assertEquals(
						"No more items in actual when there should be. Missing assignment: "
								+ expectedAssign.getAsString(new IndentedStringBuilder(4)),
						expected.getAssignments().size(),
						headerFile.getAssignments().size()
				);
			}
			HeaderAssignment actualAssign = iterAssigns.next();
			if (!expectedAssign.equalsAssignment(actualAssign)) {
				assertEquals(expectedAssign, actualAssign);
			}
		}

		Iterator<HeaderClass> iterClass = headerFile.getClasses().iterator();
		for (HeaderClass expectedClass : expected.getNestedClasses()) {
			if (!iterClass.hasNext()) {
				assertEquals(
						"No more items in actual when there should be.  Missing class: "
								+ expectedClass.getAsString(new IndentedStringBuilder(4)),
						expected.getNestedClasses().size(),
						headerFile.getClasses().size()
				);
			}
			HeaderClass actualClass = iterClass.next();
			if (!expectedClass.equalsClass(actualClass)) {
				assertEquals(expectedClass, actualClass);
			}
		}
	}

	public static HeaderClass hClass(String name, String extend, Object... things) {
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

	public static <T> ArrayList<T> toList(T... tArr) {
		ArrayList<T> ts = new ArrayList<T>();
		Collections.addAll(ts, tArr);
		return ts;
	}

	public static HeaderClass hClass(String name, String extend, ArrayList<HeaderAssignment> assignments, ArrayList<HeaderClass> nestedClasses) {
		AST.HeaderClassNode c = new AST.HeaderClassNode((HeaderClass) null, assignments, nestedClasses);
		c.setClassName(name);
		c.setExtendClassName(extend);
		return c;
	}

	public static HeaderArray array(HeaderValue... values) {
		return (HeaderArray) arrItem(values);
	}

	public static HeaderArrayItem arrItem(HeaderValue... values) {
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

	public static HeaderArrayAssignment arr_assign(String vn, HeaderValue v, boolean concat) {
		return new AST.HeaderArrayAssignmentNode(vn, (HeaderArray) v, concat);
	}

	public static HeaderAssignment assign(String vn, String v) {
		return assign(vn, value(v));
	}

	public static HeaderAssignment assign(String vn, HeaderValue v) {
		return new AST.HeaderAssignmentNode(vn, v);
	}

	public static HeaderValue value(String s) {
		return new AST.HeaderValueNode(s);
	}

	public static String wrap(String s) {
		return "\"" + s + "\"";
	}
}
