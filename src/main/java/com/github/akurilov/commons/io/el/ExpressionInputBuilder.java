package com.github.akurilov.commons.io.el;

import de.odysseus.el.util.SimpleContext;

import java.lang.reflect.Method;

import static com.github.akurilov.commons.io.el.ExpressionInput.ASYNC_MARKER;
import static com.github.akurilov.commons.io.el.ExpressionInput.FACTORY;
import static com.github.akurilov.commons.io.el.ExpressionInput.SYNC_MARKER;

public class ExpressionInputBuilder<T>
implements ExpressionInput.Builder<T> {

	protected final SimpleContext ctx = new SimpleContext();

	protected volatile String expr = null;
	protected volatile T initial = null;
	protected volatile Class<T> type = null;

	@Override
	public final ExpressionInput.Builder<T> expr(final String expr) {
		this.expr = expr;
		return this;
	}

	@Override
	public final ExpressionInput.Builder<T> initial(final T value) {
		this.initial = value;
		return this;
	}

	@Override
	public final ExpressionInput.Builder<T> type(final Class<T> type) {
		this.type = type;
		return this;
	}

	@Override
	public final ExpressionInput.Builder<T> func(final String prefix, final String name, final Method method) {
		ctx.setFunction(prefix, name, method);
		return this;
	}

	@Override
	public final ExpressionInput.Builder<T> value(final String name, final Object val, final Class<?> type) {
		final var ve = FACTORY.createValueExpression(val, type);
		ctx.setVariable(name, ve);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpressionInput<T> build() {
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
			return new SynchronousExpressionInputImpl<>(expr, initial, type, ctx);
		} else {
			return new ExpressionInputImpl<>(expr, initial, type, ctx);
		}
	}
}
