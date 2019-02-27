package com.github.akurilov.commons.io.el;

import javax.el.ELContext;
import java.util.List;

final class ExternEvalExpressionInputImpl<T>
extends ExpressionInputImpl<T>
implements ExternEvalExpressionInput<T> {

	private volatile T last;

	ExternEvalExpressionInputImpl(final String exprStr, final Class<T> resultType, final ELContext ctx) {
		super(exprStr, resultType, ctx);
	}

	@Override
	public final T get() {
		return last;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final int get(final List<T> buffer, final int limit) {
		for(var i = 0; i < limit; i ++) {
			buffer.add(last);
		}
		return limit;
	}

	@Override
	public final long skip(final long count) {
		return count;
	}

	@Override
	public final void close() {
		last = null;
	}

	@Override
	public final void run() {
		last = eval();
	}
}
