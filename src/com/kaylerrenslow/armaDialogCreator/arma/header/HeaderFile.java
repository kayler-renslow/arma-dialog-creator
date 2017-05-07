package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderFile {
	private final File file;
	private List<HeaderAssignment> assignmentsMutable = new LinkedList<>();
	private HeaderAssignmentList assignments = new HeaderAssignmentList(assignmentsMutable);
	private List<HeaderClass> classesMutable = new LinkedList<>();
	private HeaderClassList classes = new HeaderClassList(null, classesMutable);
	private final HashMap<HeaderClass, HeaderClass> inheritanceHelper = new HashMap<>();

	protected HeaderFile(@NotNull File file) {
		this.file = file;
	}

	@NotNull
	public File getFile() {
		return file;
	}

	@NotNull
	protected List<HeaderClass> getClassesMutable() {
		return classesMutable;
	}

	@NotNull
	public List<HeaderAssignment> getAssignmentsMutable() {
		return assignmentsMutable;
	}

	@NotNull
	public HeaderAssignmentList getAssignments() {
		return assignments;
	}


	@NotNull
	public HeaderClassList getClasses() {
		return classes;
	}

	/**
	 An iterative way to traverse downwards the inheritance tree. The algorithm is iterative BFS.
	 This method just iterates through {@link #getClasses()} and invokes {@link HeaderClass#traverseDownwards(Function)}

	 @param classTraverseCallback function invoked on each descendant class. Return true if traversal should continue, false if it should stop
	 */
	public void traverseDownwards(@NotNull Function<HeaderClass, Boolean> classTraverseCallback) {
		for (HeaderClass hc : getClasses()) {
			hc.traverseDownwards(classTraverseCallback);
		}
	}

	/**
	 Get an assignment for a {@link HeaderClass}. This method will also check inherited assignments.
	 <b>NOTE:</b> this method will only work if the {@link HeaderClass} is a member of this file.

	 @param start the {@link HeaderClass} to get an assignment for
	 @param varName the variable name ({@link HeaderAssignment#getVariableName()})
	 @param caseSensitive true if the class names and assignment names should be matched with case sensitivity
	 @return the {@link HeaderAssignment} that was matched, or null if none was matched
	 @throws HeaderClassNotFoundException when the extend class of <code>start</code> doesn't exist
	 */
	@Nullable
	public HeaderAssignment getAssignmentByVarName(@NotNull HeaderClass start, @NotNull String varName, boolean caseSensitive) {
		if (start.getOwnerFile() != this) {
			throw new IllegalArgumentException("start is not a member of this file");
		}
		HeaderClass cursor = start;
		while (true) {
			HeaderAssignment a = cursor.getAssignments().getByVarName(varName, caseSensitive);
			if (a != null) {
				return a;
			}

			HeaderClass extendClass = getInherited(cursor, caseSensitive);

			if (extendClass == null) {
				return null;
			}

			cursor = extendClass;
		}
	}

	/**
	 Get the extend class for the given {@link HeaderClass} instance

	 @param headerClass the class to get extend class for
	 @param caseSensitive true if the case sensitivity of class names matters
	 @return the class, or null if <code>{@link HeaderClass#getExtendClassName()}==null</code>
	 */
	@Nullable
	public HeaderClass getExtendClass(@NotNull HeaderClass headerClass, boolean caseSensitive) {
		if (headerClass.getOwnerFile() != this) {
			throw new IllegalArgumentException("headerClass is not a member of this file");
		}
		return getInherited(headerClass, caseSensitive);
	}

	@Nullable
	private HeaderClass getInherited(@NotNull HeaderClass headerClass, boolean caseSensitive) {
		if (headerClass.getExtendClassName() == null) {
			return null;
		}
		return inheritanceHelper.computeIfAbsent(headerClass, (hc) -> {
			Reference<HeaderClass> extendClassRef = new Reference<>();

			hc.traverseUpwards((parent) -> {
				if (parent.classNameEquals(hc.getExtendClassName(), caseSensitive)) {
					extendClassRef.setValue(parent);
					return false;
				}
				HeaderClass c = parent.getNestedClasses().getByName(hc.getExtendClassName(), caseSensitive);
				if (c != null) {
					extendClassRef.setValue(c);
					return false;
				}
				return true;
			});

			if (extendClassRef.getValue() == null) {
				throw new HeaderClassNotFoundException(hc);
			}

			return extendClassRef.getValue();
		});
	}

	@NotNull
	public String getAsString() {
		StringBuilder b = new StringBuilder();
		for (HeaderAssignment a : getAssignments()) {
			b.append(a.getAsString());
		}
		for (HeaderClass hc : getClasses()) {
			b.append(hc.getAsString());
		}
		return b.toString();
	}
}
