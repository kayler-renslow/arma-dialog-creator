package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

/**
 @author Kayler
 @since 04/30/2017 */
public class HeaderAssignmentList implements Iterable<HeaderAssignment> {
	private final List<HeaderAssignment> assignList;

	public HeaderAssignmentList(@NotNull List<HeaderAssignment> assignmentList) {
		this.assignList = assignmentList;
	}

	@Nullable
	public HeaderAssignment getAssignmentByVarName(@NotNull String varName, boolean caseSensitive) {
		for (HeaderAssignment ha : assignList) {
			if (caseSensitive) {
				if (ha.getVariableName().equals(varName)) {
					return ha;
				}
			} else {
				if (ha.getVariableName().equalsIgnoreCase(varName)) {
					return ha;
				}
			}
		}
		return null;
	}

	@NotNull
	@Override
	public Iterator<HeaderAssignment> iterator() {
		return assignList.iterator();
	}
}
