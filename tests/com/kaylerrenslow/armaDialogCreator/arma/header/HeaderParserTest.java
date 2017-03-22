package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 @author Kayler
 @since 03/20/2017 */
public class HeaderParserTest {

	@Test
	public void parseHeaderTest1() throws Exception {
		HeaderParser p = new HeaderParser(new File("tests/com/kaylerrenslow/armaDialogCreator/arma/header/headerTest1.h"), null);
		HeaderParserContext c = p.getParserContext();
		p.parse();

		HeaderMacro[] macros = {
				new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent("defined", new DefineMacroContent.StringDefineValue("100"))),
				new HeaderMacro(HeaderMacro.MacroType.Undefine, new UndefineMacroContent("undefined")),
				new HeaderMacro(HeaderMacro.MacroType.IfDef, new ConditionalMacroContent("ifdef")),
				new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent("GLUE", new DefineMacroContent.ParameterDefineValue(
						new String[]{"g1", "g2"}, "g1##g2"
				))),
				new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent("FOO", new DefineMacroContent.StringDefineValue("123"))),
				new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent("BAR", new DefineMacroContent.StringDefineValue("456")))
		};
		for (HeaderMacro toMatch : macros) {
			if (!c.getMacros().contains(toMatch)) {
				System.out.println("parsed macros: " + c.getMacros());
				assertTrue("missing macro: " + toMatch, false);
			}
		}
	}

}