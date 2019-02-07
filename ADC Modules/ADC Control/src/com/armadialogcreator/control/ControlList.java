package com.armadialogcreator.control;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;

/**
 @author K
 @since 02/06/2019 */
public class ControlList extends ListObserver<ArmaControl> {
	public ControlList() {
		super(new LinkedList<>());
	}

	/** @return an iterator that will traverse all controls and all controls of control groups ({@link ArmaControlGroup}) */
	@NotNull
	public Iterable<ArmaControl> deepIterator() {
		return new DeepIterator(this);
	}

	private static class DeepIterator implements Iterable<ArmaControl>, Iterator<ArmaControl> {

		private final ControlList list;
		private final LinkedList<ArmaControl> toVisit = new LinkedList<>();
		private final Iterator<ArmaControl> primaryListIterator;

		public DeepIterator(@NotNull ControlList list) {
			this.list = list;
			primaryListIterator = list.iterator();
		}

		@Override
		public Iterator<ArmaControl> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return primaryListIterator.hasNext() || toVisit.size() > 0;
		}

		@Override
		public ArmaControl next() {
			if (!hasNext()) {
				throw new IllegalStateException("nothing left to iterate");
			}
			if (primaryListIterator.hasNext()) {
				ArmaControl next = primaryListIterator.next();
				toVisit(next);
				return next;
			}
			ArmaControl next = toVisit.pop();
			toVisit(next);
			return next;
		}

		private void toVisit(ArmaControl next) {
			if (next instanceof ArmaControlGroup) {
				toVisit.addAll(((ArmaControlGroup) next).getControls());
			}
		}
	}
}
