package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 @author kayler
 @since 4/26/17 */
interface AST {

	abstract class ASTNode implements AST {

	}

	class HeaderAssignmentNode extends ASTNode implements HeaderAssignment {

		private final String varName;
		private final HeaderValue value;

		public HeaderAssignmentNode(@NotNull String varName, @NotNull HeaderValue value) {
			this.varName = varName;
			this.value = value;
		}

		@Override
		public @NotNull String getVariableName() {
			return varName;
		}

		@Override
		public @NotNull HeaderValue getValue() {
			return value;
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
		public HeaderValue getValue() {
			return getArray();
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
	}

	class HeaderArrayNode extends ASTNode implements HeaderArray {

		private final List<HeaderArrayItem> items = new ArrayList<>();

		@Override
		@NotNull
		public List<HeaderArrayItem> getItems() {
			return items;
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
	}

	class HeaderClassNode extends ASTNode implements HeaderClass {

		private final String className;
		private final String extendClassName;
		private final List<HeaderAssignment> assignments = new ArrayList<>();
		private final List<HeaderClass> nestedClasses = new ArrayList<>();

		public HeaderClassNode(@NotNull String className, @Nullable String extendClassName) {
			this.className = className;
			this.extendClassName = extendClassName;
		}

		@NotNull
		@Override
		public List<HeaderAssignment> getAssignments() {
			return assignments;
		}

		@Override
		@NotNull
		public List<HeaderClass> getNestedClasses() {
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
	}
}
