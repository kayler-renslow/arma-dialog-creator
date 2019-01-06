package com.armadialogcreator.canvas;

import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 A {@link ListObserver} for a list of {@link CanvasControl} instances.
 <p>
 This will also manage the {@link CanvasControl#getHolder()} to be equal to this list's {@link #getHolder()}

 @author Kayler
 @since 08/12/2016. */
public class ControlList<C extends CanvasControl> extends ListObserver<C> {
	private final ControlHolder<C> holder;

	public ControlList(@NotNull ControlHolder<C> holder) {
		super(new ArrayList<>());
		this.holder = holder;
	}

	/** Remove all controls. Also set {@link CanvasControl#getHolder()} to null on each control */
	@Override
	public void clear() {
		for (C c : this) {
			c.getHolderObserver().updateValue(null);
		}
		super.clear();
	}


	@Override
	protected void notifyListeners(@NotNull ListObserverChange<C> change) {
		switch (change.getChangeType()) {
			case Add: {
				ListObserverChangeAdd<C> c = change.getAdded();
				c.getAdded().getHolderObserver().updateValue(this.holder);
				break;
			}
			case Move: {
				ListObserverChangeMove<C> c = change.getMoved();
				if (c.isEntryUpdate()) { //perform this operation only once
					ControlList<C> destinationControlList = (ControlList<C>) c.getDestinationList();
					c.getMoved().getHolderObserver().updateValue(destinationControlList.holder);
				}
				break;
			}
			case Remove: {
				ListObserverChangeRemove<C> c = change.getRemoved();
				c.getRemoved().getHolderObserver().updateValue(null);
				break;
			}
			case Set: {
				ListObserverChangeSet<C> c = change.getSet();
				c.getOld().getHolderObserver().updateValue(null);
				c.getNew().getHolderObserver().updateValue(this.holder);
				break;
			}
			default: {
				throw new IllegalStateException();
			}
		}
		super.notifyListeners(change);
	}

	/** @return the {@link ControlHolder} that uses this list */
	@NotNull
	public ControlHolder<C> getHolder() {
		return holder;
	}

	/** Get an iterator that will traverse all descendants (all controls and all their controls) */
	@NotNull
	public Iterable<C> deepIterator() {
		return new DeepIterator<>(this);
	}

	private static class DeepIterator<C extends CanvasControl> implements Iterable<C>, Iterator<C> {

		private final ControlList<C> list;
		private final LinkedList<C> toVisit = new LinkedList<>();
		private final Iterator<C> primaryListIterator;

		public DeepIterator(@NotNull ControlList<C> list) {
			this.list = list;
			primaryListIterator = list.iterator();
		}

		@Override
		public Iterator<C> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return primaryListIterator.hasNext() || toVisit.size() > 0;
		}

		@Override
		public C next() {
			if (!hasNext()) {
				throw new IllegalStateException("nothing left to iterate");
			}
			if (primaryListIterator.hasNext()) {
				C next = primaryListIterator.next();
				toVisit(next);
				return next;
			}
			C next = toVisit.pop();
			toVisit(next);
			return next;
		}

		private void toVisit(C next) {
			if (next instanceof CanvasControlGroup) {
				toVisit.addAll(((CanvasControlGroup<C>) next).getControls());
			}
		}
	}

}
