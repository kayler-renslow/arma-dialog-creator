package com.armadialogcreator.canvas;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Stack;

/**
 @author K
 @since 02/06/2019 */
public class DeepUINodeIterable implements Iterable<UINode> {
	private final Iterable<? extends UINode> iterable;

	public DeepUINodeIterable(@NotNull Iterable<? extends UINode> iterable) {
		this.iterable = iterable;
	}

	@NotNull
	@Override
	public MyIterator iterator() {
		return new MyIterator(iterable);
	}

	public static class MyIterator implements Iterator<UINode> {
		private final Stack<Iterator<? extends UINode>> iterStack = new Stack<>();

		private boolean checked = false;
		private int depth = 0;

		public MyIterator(@NotNull Iterable<? extends UINode> iterable) {
			iterStack.push(iterable.iterator());
		}

		@Override
		public boolean hasNext() {
			check();
			return !iterStack.isEmpty() && iterStack.peek().hasNext();
		}

		private void check() {
			if (!checked) {
				while (!iterStack.isEmpty() && !iterStack.peek().hasNext()) {
					iterStack.pop();
					depth--;
				}
				depth = depth < 0 ? 0 : depth;
				checked = true;
			}
		}

		/**
		 @return -1 if nothing left to iterate, 0 for first node, 1 for children of first node,
		 2 for children of children of first node, etc.
		 Initial value before first {@link #hasNext()} or {@link #next()} call is 0.
		 */
		public int getDepth() {
			check();
			return depth;
		}

		@Override
		public UINode next() {
			if (!hasNext()) {
				throw new IllegalStateException();
			}
			UINode next = iterStack.peek().next();
			if (next.getChildCount() > 0) {
				iterStack.push(next.deepIterateChildren().iterator());
				depth++;
			}
			return next;
		}
	}
}
