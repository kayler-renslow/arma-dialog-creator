package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 Created by Kayler on 07/30/2016.
 */
public class XmlUtil {

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

	@NotNull
	public static Iterable<Element> iterateChildElements(@NotNull Element element) {
		return new Iterable<>() {
			@NotNull
			@Override
			public Iterator<Element> iterator() {
				return new Iterator<>() {
					NodeList list = element.getChildNodes();
					int i = 0;
					boolean checked = false;

					@Override
					public boolean hasNext() {
						if (!checked) {
							checked = true;
							while (i < list.getLength() && list.item(i).getNodeType() != Node.ELEMENT_NODE) {
								i++;
							}
						}
						return i < list.getLength();
					}

					@Override
					public Element next() {
						checked = false;
						return (Element) list.item(i++);
					}
				};
			}
		};
	}

	@NotNull
	public static List<Element> getChildElementsWithTagName(@NotNull Element element, @NotNull String tagName) {
		List<Element> childs = new ArrayList<>();
		if (tagName.equals("*")) {
			for (Element e : iterateChildElements(element)) {
				childs.add(e);
			}
		} else {
			for (Element child : iterateChildElements(element)) {
				if (child.getTagName().equals(tagName)) {
					childs.add(child);
				}
			}
		}
		return childs;
	}
}
