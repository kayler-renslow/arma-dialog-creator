package com.armadialogcreator.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 05/26/2017 */
public class IndentedStringBuilderTest {

	@Test
	public void tabSizeInSpacesErrorTest() throws Exception {
		try {
			new IndentedStringBuilder(-1);
		} catch (IllegalArgumentException e) {
			assertEquals(true, true);
			return;
		}
		assertEquals(true, false);
	}

	@Test
	public void getTabSizeInSpaces() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		assertEquals(s.getTabSizeInSpaces(), 2);
	}


	@Test
	public void initialTabCount() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		assertEquals("", s.getCurrentTab());
	}

	@Test
	public void incrementTab() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		assertEquals("  ", s.getCurrentTab());
	}

	@Test
	public void decrementTab() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		s.incrementTabCount();
		s.decrementTabCount();
		assertEquals("  ", s.getCurrentTab());
	}

	@Test
	public void getBuilder() throws Exception {
		//check if doesn't throw NullPointerException
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		assertEquals(true, s.getBuilder() != null);
	}

	@Test
	public void appendChar1() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append('h');
		assertEquals("h", s.toString());
	}

	@Test
	public void appendChar2() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append('h');
		s.append('\n');
		assertEquals("h\n", s.toString());
	}

	@Test
	public void appendChar3() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append('h');
		s.append('\n');
		s.incrementTabCount();
		s.append('c');
		assertEquals("h\nc", s.toString());
	}

	@Test
	public void appendChar4() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		s.append('h');
		s.append('\n');
		s.append('c');
		assertEquals("h\n  c", s.toString());
	}

	@Test
	public void appendString1() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append("hello");
		assertEquals("hello", s.toString());
	}

	@Test
	public void appendString2() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append("hello\n");
		assertEquals("hello\n", s.toString());
	}

	@Test
	public void appendString3() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		s.append("hello\n");
		s.append('c');
		assertEquals("hello\n  c", s.toString());
	}

	@Test
	public void appendStringRange1() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append("helloo", 0, 5);
		assertEquals("hello", s.toString());
	}

	@Test
	public void appendStringRange2() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		s.append("hello\n\n", 0, 6);
		assertEquals("hello\n  ", s.toString());
	}

	@Test
	public void appendStringRange3() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		s.append("hhello\n\n", 1, 7);
		s.append('c');
		assertEquals("hello\n  c", s.toString());
	}

	@Test
	public void appendStringBuilder1() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append(new StringBuilder("hello"));
		assertEquals("hello", s.toString());
	}

	@Test
	public void appendStringBuilder2() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.append(new StringBuilder("hello\n"));
		assertEquals("hello\n", s.toString());
	}

	@Test
	public void appendStringBuilder3() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		s.append(new StringBuilder("hello\n"));
		s.append('c');
		assertEquals("hello\n  c", s.toString());
	}

	@Test
	public void generalTest() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2);
		s.incrementTabCount();
		s.append("{\nhello world!\nHi again!");
		s.decrementTabCount();
		s.append("\n}");
		assertEquals("{\n  hello world!\n  Hi again!\n}", s.toString());
	}

	@Test
	public void generalTestWithTab() throws Exception {
		IndentedStringBuilder s = new IndentedStringBuilder(2, true);
		s.incrementTabCount();
		s.append("{\nhello world!\nHi again!");
		s.decrementTabCount();
		s.append("\n}");
		assertEquals("{\n\thello world!\n\tHi again!\n}", s.toString());
	}

}
