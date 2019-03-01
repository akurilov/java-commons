package com.github.akurilov.commons.io.el;

import de.odysseus.el.util.SimpleContext;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import java.util.List;

class ExpressionInputImpl<T>
implements ExpressionInput<T> {

	private final ValueExpression expr;
	private final ELContext ctx;
	private final T initial;
	private volatile T last;

	ExpressionInputImpl(final String exprStr, final T initial, Class<T> type, final SimpleContext ctx) {
		this.last = this.initial = initial;
		final var ve = FACTORY.createValueExpression(this, getClass());
		ctx.setVariable(SELF_REF_ID, ve);
		this.expr = FACTORY.createValueExpression(ctx, exprStr, type);
		this.ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	private T eval() {
		return (T) expr.getValue(ctx);
	}

	@Override
	public final T call()
	throws PropertyNotFoundException, ELException {
		return last = eval();
	}

	@Override
	public final T last() {
		return last;
	}

	@Override
	public T get()
	throws PropertyNotFoundException, ELException {
		return last;
	}

	@Override
	public int get(final List<T> buffer, final int limit)
	throws PropertyNotFoundException, ELException {
		for(var i = 0; i < limit; i ++) {
			buffer.add(last);
		}
		return limit;
	}

	@Override
	public long skip(final long count) {
		return count;
	}

	@Override
	public final void reset() {
		last = initial;
	}

	@Override
	public final String expr() {
		return expr.getExpressionString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Class<T> type() {
		return (Class<T>) expr.getExpectedType();
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + "(" + expr() + ", " + type() + ")";
	}

	@Override
	public final void close() {
		last = null;
	}

}
