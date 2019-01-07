package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 Created by Kayler on 07/30/2016.
 */
public class XmlUtil {

	/**
	 Get all children with the given <code>tagName</code> as a stream (if tagName is null, matches all nodes).
	 */
	@NotNull
	public static Iterable<Element> getChildElementsWithTagNameIterable(@NotNull Element element, @Nullable String tagName) {
		return new Iterable<>() {
			@NotNull
			@Override
			public Iterator<Element> iterator() {
				return new Iterator<>() {
					NodeList list = element.getChildNodes();
					int i = 0;
					int lastCheckI = -1;

					@Override
					public boolean hasNext() {
						while (lastCheckI != i && i < list.getLength() && list.item(i).getNodeType() != Node.ELEMENT_NODE) {
							i++;
						}
						lastCheckI = i;
						return i < list.getLength();
					}

					@Override
					public Element next() {
						return (Element) list.item(i++);
					}
				};
			}
		};
	}

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
			Node item = list.item(i);
			if (testEquals(item, tagName)) {
				addToMe.add((Element) item);
			}
		}
	}

	@Nullable
	private static String tagName(String tagName) {
		return tagName != null && tagName.equals("*") ? null : tagName;
	}

	private static boolean testEquals(Node node, @Nullable String tagName) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return tagName == null || node.getNodeName().equals(tagName);
		}
		return false;
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

	@NotNull
	public static Iterable<Attr> iterateAttributes(@NotNull NamedNodeMap attributes) {
		return new Iterable<>() {
			@NotNull
			@Override
			public Iterator<Attr> iterator() {
				return new Iterator<>() {
					int i = 0;

					@Override
					public boolean hasNext() {
						return i < attributes.getLength();
					}

					@Override
					public Attr next() {
						return (Attr) attributes.item(i++);
					}
				};
			}
		};
	}
}
