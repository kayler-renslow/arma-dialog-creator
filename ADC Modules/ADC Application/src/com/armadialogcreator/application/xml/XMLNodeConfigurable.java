package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.util.KeyValueString;
import com.armadialogcreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.Iterator;

/**
 @author K
 @since 01/06/2019 */
public class XMLNodeConfigurable implements Configurable {

	private final Element element;

	public XMLNodeConfigurable(@NotNull Element element) {
		this.element = element;
	}

	@Override
	@NotNull
	public Iterable<Configurable> getNestedConfigurables() {
		return new Iterable<>() {

			@NotNull
			@Override
			public Iterator<Configurable> iterator() {
				return new Iterator<Configurable>() {
					Iterator<Element> iterator = XmlUtil.iterateChildElements(XMLNodeConfigurable.this.element).iterator();

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public Configurable next() {
						return new XMLNodeConfigurable(iterator.next());
					}
				};
			}
		};
	}

	@Override
	@NotNull
	public Iterable<KeyValueString> getConfigurableAttributes() {
		return new Iterable<>() {
			@NotNull
			@Override
			public Iterator<KeyValueString> iterator() {
				return new Iterator<>() {
					Iterator<Attr> iterator = XmlUtil.iterateAttributes(element.getAttributes()).iterator();

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public KeyValueString next() {
						Attr next = iterator.next();
						return new KeyValueString(next.getName(), next.getValue());
					}
				};
			}
		};
	}

	@Override
	public void addNestedConfigurable(@NotNull Configurable c) {
		Document d = element.getOwnerDocument();
		Element nestedElement = d.createElement(c.getConfigurableName());
		this.element.appendChild(nestedElement);

		for (KeyValueString kv : c.getConfigurableAttributes()) {
			nestedElement.setAttribute(kv.getKey(), kv.getValue());
		}

		String body = c.getConfigurableBody();
		if (body.length() > 0) {
			Text text = d.createTextNode(body);
			nestedElement.appendChild(text);
		}

		Iterator<Configurable> iter = c.getNestedConfigurables().iterator();
		if (iter.hasNext()) {
			XMLNodeConfigurable nestedElementAsConfigurable = new XMLNodeConfigurable(nestedElement);
			while (iter.hasNext()) {
				nestedElementAsConfigurable.addNestedConfigurable(iter.next());
			}
		}
	}

	@Override
	public void addAttribute(@NotNull String key, @NotNull String value) {
		element.setAttribute(key, value);
	}

	@Override
	@NotNull
	public String getConfigurableName() {
		return element.getTagName();
	}

	@Override
	@NotNull
	public String getConfigurableBody() {
		return XmlUtil.getImmediateTextContent(element);
	}
}
