/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Kayler on 07/30/2016.
 */
public class XmlUtil {
	
	/**
	 Get all children with the given tagName. If tagName.equals("*"), will return all child elements.
	 
	 @param element element to get children with tag name of
	 @param tagName tag name to search for
	 @return NodeList containing nodes with the tag name
	 */
	public static List<Element> getChildElementsWithTagName(Element element, String tagName) {
		ArrayList<Element> elements = new ArrayList<>();
		if (tagName.equals("*")) {
			NodeList list = element.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					elements.add((Element) list.item(i));
				}
			}
		} else {
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().equals(tagName)) {
						elements.add((Element) node);
					}
				}
			}
		}
		return elements;
	}
	
	/** Get the text content of the given node but not the node's descendant nodes' text concatenated with it. */
	public static String getImmediateTextContent(Node node) {
		NodeList list = node.getChildNodes();
		StringBuilder textContent = new StringBuilder();
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				textContent.append(child.getTextContent());
			}
		}
		return textContent.toString();
	}
	
	private static class SimpleNodeList implements NodeList {
		
		private final List<Node> nodes;
		
		public SimpleNodeList(List<Node> nodes) {
			this.nodes = nodes;
		}
		
		@Override
		public Node item(int index) {
			return nodes.get(index);
		}
		
		@Override
		public int getLength() {
			return nodes.size();
		}
	}
}
