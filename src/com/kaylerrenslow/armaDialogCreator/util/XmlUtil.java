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
