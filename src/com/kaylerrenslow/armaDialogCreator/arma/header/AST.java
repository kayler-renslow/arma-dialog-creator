package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author kayler
 @since 4/26/17 */
interface AST {

	abstract class ASTNode implements AST {
		private String s;

		public ASTNode() {

		}

		@Override
		public String toString() {
			if (s == null) {
				if (this instanceof HeaderItem) {
					HeaderItem me = (HeaderItem) this;
					s = me.getAsString();
				} else {
					s = super.toString();
				}
			}
			return s;
		}
	}

	class HeaderAssignmentNode extends ASTNode implements HeaderAssignment {

		private final String varName;
		private final HeaderValue value;

		public HeaderAssignmentNode(@NotNull String varName, @NotNull HeaderValue value) {
			this.varName = varName;
			this.value = value;
		}

		@Override
		@NotNull
		public String getVariableName() {
			return varName;
		}

		@Override
		public @NotNull HeaderValue getValue() {
			return value;
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof HeaderAssignment && this.equalsAssignment((HeaderAssignment) o);
		}
	}

	class HeaderArrayAssignmentNode extends ASTNode implements HeaderArrayAssignment {

		private final String varName;
		private final HeaderArray array;
		private final boolean concat;

		public HeaderArrayAssignmentNode(@NotNull String varName, @NotNull HeaderArray array, boolean concat) {
			this.varName = varName;
			this.array = array;
			this.concat = concat;
		}

		@Override
		@NotNull
		public String getVariableName() {
			return varName;
		}

		@Override
		@NotNull
		public HeaderArray getArray() {
			return array;
		}

		@Override
		public boolean isConcatenated() {
			return concat;
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof HeaderAssignment && this.equalsAssignment((HeaderAssignment) o);
		}
	}

	class HeaderArrayNode extends ASTNode implements HeaderArray {

		private final List<HeaderArrayItem> items;

		public HeaderArrayNode(List<HeaderArrayItem> items) {
			this.items = items;
		}

		@Override
		@NotNull
		public List<HeaderArrayItem> getItems() {
			return items;
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof HeaderArray && this.equalsArray((HeaderArray) o);
		}
	}

	class HeaderArrayItemNode extends ASTNode implements HeaderArrayItem {

		private final HeaderValue value;

		public HeaderArrayItemNode(@NotNull HeaderValue value) {
			this.value = value;
		}

		@Override
		@NotNull
		public HeaderValue getValue() {
			return value;
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof HeaderArrayItem && this.arrayItemEquals((HeaderArrayItem) o);
		}
	}

	class HeaderClassNode extends ASTNode implements HeaderClass {

		private final String className;
		private final String extendClassName;
		private final List<HeaderAssignment> assignments;
		private final HeaderClassList nestedClasses;

		public HeaderClassNode(@NotNull String className, @Nullable String extendClassName, @NotNull List<HeaderAssignment> assignments, @NotNull List<HeaderClass> nestedClasses) {
			this.className = className;
			this.extendClassName = extendClassName;
			this.assignments = assignments;
			this.nestedClasses = new HeaderClassList(nestedClasses);
		}

		@NotNull
		@Override
		public List<HeaderAssignment> getAssignments() {
			return assignments;
		}

		@Override
		@NotNull
		public HeaderClassList getNestedClasses() {
			return nestedClasses;
		}

		@Override
		@NotNull
		public String getClassName() {
			return className;
		}

		@Override
		@Nullable
		public String getExtendClassName() {
			return extendClassName;
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof HeaderClass && this.equalsClass((HeaderClass) o);
		}
	}

	class HeaderValueNode extends ASTNode implements HeaderValue {

		private final String content;

		public HeaderValueNode(@NotNull String content) {
			this.content = content;
		}

		@Override
		@NotNull
		public String getContent() {
			return content;
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof HeaderValue && this.equalsValue((HeaderValue) o);
		}
	}
}
