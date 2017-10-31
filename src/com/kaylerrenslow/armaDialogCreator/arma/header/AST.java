package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.IndentedStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author kayler
 @since 4/26/17 */
interface AST {

	abstract class ASTNode implements AST {

		public ASTNode() {

		}

		@Override
		public String toString() {
			String s = "";
			if (this instanceof HeaderItem) {
				HeaderItem me = (HeaderItem) this;
				s = me.getAsString(new IndentedStringBuilder(4));
			} else {
				s = super.toString();
			}
			return s;
		}

	}

	class HeaderAssignmentNode extends ASTNode implements HeaderAssignment {

		private final String varName;
		private final HeaderValue value;

		public HeaderAssignmentNode(@NotNull String varName, @Nullable HeaderValue value) {
			this.varName = varName;
			if (value == null) {
				this.value = new BasicHeaderValue("\"\"");
			} else {
				this.value = value;
			}
		}

		@Override
		@NotNull
		public String getVariableName() {
			return varName;
		}

		@Override
		@NotNull
		public HeaderValue getValue() {
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

		private String className;
		private String extendClassName;
		private final HeaderAssignmentList assignments;
		private final HeaderClassList nestedClasses;
		private final HeaderClass parentClass;
		private HeaderFile headerFile;

		public HeaderClassNode(@Nullable HeaderFile file, @NotNull List<HeaderAssignment> assignments, @NotNull List<HeaderClass> nestedClasses) {
			this((HeaderClass) null, assignments, nestedClasses);
			this.className = "`ROOT CLASS`";
			this.headerFile = file;
		}

		public HeaderClassNode(@Nullable HeaderClass parentClass, @NotNull List<HeaderAssignment> assignments, @NotNull List<HeaderClass> nestedClasses) {
			this.assignments = new HeaderAssignmentList(assignments);
			this.nestedClasses = new HeaderClassList(this, nestedClasses);
			this.parentClass = parentClass;
			this.headerFile = parentClass != null ? parentClass.getOwnerFile() : null;
		}


		public void setClassName(@NotNull String className) {
			this.className = className;
		}

		public void setExtendClassName(@Nullable String extendClassName) {
			this.extendClassName = extendClassName;
		}

		@NotNull
		@Override
		public HeaderAssignmentList getAssignments() {
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
		@Nullable
		public HeaderClass getContainingClass() {
			return parentClass;
		}

		@Override
		@NotNull
		public HeaderFile getOwnerFile() {
			return headerFile;
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
