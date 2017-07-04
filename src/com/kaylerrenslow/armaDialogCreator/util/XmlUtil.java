package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 Created by Kayler on 07/30/2016.
 */
public class XmlUtil {

	/**
	 Get all children with the given <code>tagName</code>. If <code>tagName.equals("*")</code> or <code>tagName == null</code>, will return all child elements.

	 @param element element to get children with tag name of
	 @param tagName tag name to search for, or null if doesn't matter (same as doing "*")
	 @return list containing elements with the tag name. Default implementation is {@link ArrayList}
	 */
	@NotNull
	public static List<Element> getChildElementsWithTagName(@NotNull Element element, @Nullable String tagName) {
		ArrayList<Element> elements = new ArrayList<>();
		getChildElementsWithTagName(element, tagName, elements);
		return elements;
	}

	/**
	 Get all children with the given <code>tagName</code>. If <code>tagName.equals("*")</code> or <code>tagName == null</code>, will return all child elements.

	 @param element element to get children with tag name of
	 @param tagName tag name to search for, or null if doesn't matter (same as doing "*")
	 @param addToMe list to add to
	 */
	public static void getChildElementsWithTagName(@NotNull Element element, @Nullable String tagName, @NotNull List<Element> addToMe) {
		NodeList list = element.getChildNodes();
		tagName = tagName(tagName);
		for (int i = 0; i < list.getLength(); i++) {
			testAdd(list.item(i), tagName, addToMe);
		}
	}

	@Nullable
	private static String tagName(String tagName) {
		return tagName != null && tagName.equals("*") ? null : tagName;
	}

	private static Element testAdd(Node node, @Nullable String tagName, @NotNull List<Element> addToMe) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			if (tagName == null || node.getNodeName().equals(tagName)) {
				addToMe.add(element);
				return element;
			}
		}
		return null;
	}

	/**
	 Get all descendant Element instances with the given <code>tagName</code>. If <code>tagName.equals("*")</code> or <code>tagName = null</code>, will return all descendant elements

	 @param element element to start at
	 @param tagName tag name to search for, or null if collect all elements
	 @return list containing elements with the tag name. Default implementation is {@link ArrayList}
	 */
	@NotNull
	public static List<Element> getDescendantElementsWithTagName(@NotNull Element element, @Nullable String tagName) {
		List<Element> elements = new ArrayList<>();
		getDescendantElementsWithTagName(element, tagName, elements);
		return elements;
	}

	/**
	 Get all descendant Element instances with the given <code>tagName</code>. If <code>tagName.equals("*")</code> or <code>tagName = null</code>, will return all descendant elements

	 @param element element to start at
	 @param tagName tag name to search
	 @param addToMe list to add to
	 */
	public static void getDescendantElementsWithTagName(@NotNull Element element, @Nullable String tagName, @NotNull List<Element> addToMe) {
		List<Element> elements = new LinkedList<>();
		getChildElementsWithTagName(element, tagName, elements);
		addToMe.addAll(elements);
		for (Element found : elements) {
			getDescendantElementsWithTagName(found, tagName, addToMe);
		}

	}

	/** Get the text content of the given node but not the node's descendant nodes' text concatenated with it. */
	@NotNull
	public static String getImmediateTextContent(@NotNull Node node) {
		NodeList list = node.getChildNodes();
		StringBuilder textContent = new StringBuilder();
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				String content = child.getTextContent();
				if (content == null) {
					continue;
				}
				textContent.append(child.getTextContent());
			}
		}
		return textContent.toString();
	}


}
