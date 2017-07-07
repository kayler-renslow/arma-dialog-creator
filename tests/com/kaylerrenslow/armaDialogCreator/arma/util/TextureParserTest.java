package com.kaylerrenslow.armaDialogCreator.arma.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 07/05/2017 */
public class TextureParserTest {
	@Test
	public void color_rgb1() throws Exception {
		TextureParser parser = new TextureParser("#(rgb,8,8,3)color(1,0,0,1)");
		Texture.Color parse = (Texture.Color) parser.parse();
		assertEquals("rgb", parse.getFormat());
		assertEquals(8, parse.getWidth());
		assertEquals(8, parse.getHeight());
		assertEquals(3, parse.getNumberOfMipMaps());
		assertEquals(1, parse.getR(), 0);
		assertEquals(0, parse.getG(), 0);
		assertEquals(0, parse.getB(), 0);
		assertEquals(1, parse.getA(), 0);
	}

	@Test
	public void color_rgb2() throws Exception {
		TextureParser parser = new TextureParser("#(rgb,8,8,3)color(1,0,0.5,1)");
		Texture.Color parse = (Texture.Color) parser.parse();
		assertEquals("rgb", parse.getFormat());
		assertEquals(8, parse.getWidth());
		assertEquals(8, parse.getHeight());
		assertEquals(3, parse.getNumberOfMipMaps());
		assertEquals(1, parse.getR(), 0);
		assertEquals(0, parse.getG(), 0);
		assertEquals(0.5, parse.getB(), 0);
		assertEquals(1, parse.getA(), 0);
	}

	@Test
	public void color_argb1() throws Exception {
		TextureParser parser = new TextureParser("#(argb,8,8,3)color(1,0,0.5,0.2)");
		Texture.Color parse = (Texture.Color) parser.parse();
		assertEquals("argb", parse.getFormat());
		assertEquals(8, parse.getWidth());
		assertEquals(8, parse.getHeight());
		assertEquals(3, parse.getNumberOfMipMaps());
		assertEquals(1, parse.getR(), 0);
		assertEquals(0, parse.getG(), 0);
		assertEquals(0.5, parse.getB(), 0);
		assertEquals(0.2, parse.getA(), 0);
	}

	@Test
	public void color_argb2() throws Exception {
		TextureParser parser = new TextureParser("#(argb,8,8,4)color(1,0,0.5,0.2)");
		Texture.Color parse = (Texture.Color) parser.parse();
		assertEquals("argb", parse.getFormat());
		assertEquals(8, parse.getWidth());
		assertEquals(8, parse.getHeight());
		assertEquals(4, parse.getNumberOfMipMaps());
		assertEquals(1, parse.getR(), 0);
		assertEquals(0, parse.getG(), 0);
		assertEquals(0.5, parse.getB(), 0);
		assertEquals(0.2, parse.getA(), 0);
	}
}