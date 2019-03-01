package com.armadialogcreator.control;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 You can think of a section as a partition of text. Each partition of text has its own attributes ({@link #getAttributes()}).
 Each section also has a {@link #getTagName()}. The tag name comes from the xml tag name that was parsed. <p>
 <p>
 Example:
 <code>&lt;t size='1'&gt;hi&lt;/t&gt;</code> will result in the tag name {@link TagName#T} and attribute 't' with
 value 1. The text would be "hi"

 @author Kayler
 @see StructuredTextParser
 @since 07/03/2017 */
public class TextSection {
	public enum TagName {
		/**
		 This technically doesn't exist in the structured text, however, when this is used, it can mean that either
		 a {@link TagName} wasn't matched or the section isn't a part of a tag.
		 */
		Root,
		T,
		Img,
		A,
		Br
	}

	private final TagName tagName;
	private final String text;
	private final Map<String, String> attributes;

	public TextSection(@NotNull TagName tagName, @NotNull String text, @NotNull Map<String, String> attributes) {
		this.tagName = tagName;
		this.text = text;
		this.attributes = attributes;
	}

	@NotNull
	public TagName getTagName() {
		return tagName;
	}

	/**
	 Get the text associated with this section. You should not need to look for things like &amp;nbsp; because it
	 should have already been converted in the {@link StructuredTextParser}

	 @return the text associated with this section
	 */
	@NotNull
	public String getText() {
		return text;
	}

	/**
	 Get the attributes for this section

	 @return all attributes
	 */
	@NotNull
	public Map<String, String> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return "TextSection{" +
				"tagName=" + tagName +
				", text='" + text + '\'' +
				", attributes=" + attributes +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof TextSection) {
			TextSection other = (TextSection) obj;
			return this.tagName == other.tagName &&
					this.attributes.equals(other.attributes) && this.text.equals(other.text);
		}
		return false;
	}
}
