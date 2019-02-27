package com.github.akurilov.commons.io.el;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import java.util.List;

class ExpressionInputImpl<T>
implements ExpressionInput<T> {

	private final ValueExpression expr;
	private final ELContext ctx;

	ExpressionInputImpl(final String exprStr, Class<T> resultType, final ELContext ctx) {
		expr = FACTORY.createValueExpression(ctx, exprStr, resultType);
		this.ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	final T eval()
	throws NullPointerException, PropertyNotFoundException, ELException {
		return (T) expr.getValue(ctx);
	}

	@Override
	public T get()
	throws NullPointerException, PropertyNotFoundException, ELException {
		return eval();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int get(final List<T> buffer, final int limit)
	throws NullPointerException, PropertyNotFoundException, ELException {
		for(var i = 0; i < limit; i ++) {
			buffer.add(eval());
		}
		return limit;
	}

	@Override
	public long skip(final long count)
	throws NullPointerException, PropertyNotFoundException, ELException {
		for(var i = 0; i < count; i ++) {
			eval();
		}
		return count;
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
	public void close() {
	}
}
