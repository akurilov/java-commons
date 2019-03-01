package com.github.akurilov.commons.io.el;

import de.odysseus.el.util.SimpleContext;

import java.lang.reflect.Method;

import static com.github.akurilov.commons.io.el.ExpressionInput.ASYNC_MARKER;
import static com.github.akurilov.commons.io.el.ExpressionInput.FACTORY;
import static com.github.akurilov.commons.io.el.ExpressionInput.SYNC_MARKER;

public class ExpressionInputBuilder
implements ExpressionInput.Builder {

	protected final SimpleContext ctx = new SimpleContext();

	protected volatile String expr = null;
	protected volatile Object initial = null;
	protected volatile Class<?> type = null;

	@Override
	public final ExpressionInput.Builder expr(final String expr) {
		this.expr = expr;
		return this;
	}

	@Override
	public final <T> ExpressionInput.Builder initial(final T value) {
		this.initial = value;
		return this;
	}

	@Override
	public final <T> ExpressionInput.Builder type(final Class<T> type) {
		this.type = type;
		return this;
	}

	@Override
	public final ExpressionInput.Builder func(final String prefix, final String name, final Method method) {
		ctx.setFunction(prefix, name, method);
		return this;
	}

	@Override
	public final ExpressionInput.Builder value(final String name, final Object val, final Class<?> type) {
		final var ve = FACTORY.createValueExpression(val, type);
		ctx.setVariable(name, ve);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, U extends ExpressionInput<T>> U build() {
		if(null == expr) {
			throw new NullPointerException("Expression shouldn't be null");
		}
		if(null == type) {
			throw new NullPointerException("Type shouldn't be null");
		}
		if(expr.contains(SYNC_MARKER)) {
			if(expr.contains(ASYNC_MARKER)) {
				throw new IllegalArgumentException(
					"The expression shouldn't contain both immediate ($) and deferred (#) expression markers"
				);
			}
			return (U) new SynchronousExpressionInputImpl<>(expr, (T) initial, (Class<T>) type, ctx);
		} else {
			return (U) new ExpressionInputImpl<>(expr, (T) initial, (Class<T>) type, ctx);
		}
	}
}
