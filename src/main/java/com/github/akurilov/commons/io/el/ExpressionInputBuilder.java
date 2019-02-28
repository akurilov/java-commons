package com.github.akurilov.commons.io.el;

import de.odysseus.el.util.SimpleContext;

import java.lang.reflect.Method;

import static com.github.akurilov.commons.io.el.ExpressionInput.FACTORY;
import static com.github.akurilov.commons.io.el.ExpressionInput.LAST_VALUE_ID;

public final class ExpressionInputBuilder<T>
implements ExpressionInput.Builder<T> {

	private final SimpleContext ctx = new SimpleContext();

	private volatile String expr = null;
	private volatile T initial = null;
	private volatile Class<T> type = null;
	private volatile boolean sync = true;

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

	@Override
	public final ExpressionInput.Builder<T> sync(final boolean syncFlag) {
		this.sync = syncFlag;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ExpressionInput<T> build() {
		if(null == expr) {
			throw new NullPointerException("Expression shouldn't be null");
		}
		if(null == type) {
			throw new NullPointerException("Type shouldn't be null");
		}
		if(sync) {
			return new SynchronousExpressionInputImpl<>(expr, initial, type, ctx);
		} else {
			return new ExpressionInputImpl<>(expr, initial, type, ctx);
		}
	}
}
