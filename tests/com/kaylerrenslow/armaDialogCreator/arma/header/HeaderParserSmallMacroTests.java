package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 @author Kayler
 @since 03/20/2017 */
public class HeaderParserSmallMacroTests {

	@Test
	public void parseHeaderTest() throws Exception {
		HeaderParser p = new HeaderParser(HeaderTestUtil.getFile("headerTest1.h"), HeaderTestUtil.getTemporaryResultsFile());
		p.parse();

		HeaderMacro[] macros = {
				Define("defined", "100"),
				Undefine("undefined"),
				If(true, "ifdef"),
				ParamDefine("GLUE", new String[]{"g1", "g2"}, "g1##g2"),
				Define("FOO", "123"),
				Define("BAR", "456")
		};
		for (HeaderMacro toMatch : macros) {
			if (!p.getMacros().contains(toMatch)) {
				System.out.println("parsed macros: " + p.getMacros());
				assertTrue("missing macro: " + toMatch, false);
			}
		}
	}

	private static HeaderMacro If(boolean ifdef, @NotNull String v) {
		return new HeaderMacro(ifdef ? HeaderMacro.MacroType.IfDef : HeaderMacro.MacroType.IfNDef, new ConditionalMacroContent(v));
	}

	private static HeaderMacro Undefine(@NotNull String v) {
		return new HeaderMacro(HeaderMacro.MacroType.Undefine, new UndefineMacroContent(v));
	}

	private static HeaderMacro Define(@NotNull String defineName, @NotNull String value) {
		return new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent(defineName, new DefineMacroContent.StringDefineValue(value)));
	}

	private static HeaderMacro ParamDefine(@NotNull String defineName, @NotNull String[] params, @NotNull String output) {
		return new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent(defineName, new DefineMacroContent.ParameterDefineValue(params, output)));
	}


}