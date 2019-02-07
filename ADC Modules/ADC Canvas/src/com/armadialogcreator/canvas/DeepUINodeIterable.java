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
	public Iterator<UINode> iterator() {
		return new Iterator<UINode>() {
			Stack<Iterator<? extends UINode>> iterStack = new Stack<>();

			{
				iterStack.push(iterable.iterator());
			}

			boolean checked = false;

			@Override
			public boolean hasNext() {
				if (!checked) {
					while (!iterStack.isEmpty() && !iterStack.peek().hasNext()) {
						iterStack.pop();
					}
					checked = true;
				}
				return !iterStack.isEmpty() && iterStack.peek().hasNext();
			}

			@Override
			public UINode next() {
				UINode next = iterStack.peek().next();
				if (next.getComponent() == null) {
					iterStack.push(next.deepIterateChildren().iterator());
				}
				return next;
			}
		};
	}
}
