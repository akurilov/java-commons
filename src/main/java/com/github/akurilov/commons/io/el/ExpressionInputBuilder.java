package com.github.akurilov.commons.io.el;

import de.odysseus.el.util.SimpleContext;

import java.lang.reflect.Method;

import static com.github.akurilov.commons.io.el.ExpressionInput.EXPRESSION_PATTERN;
import static com.github.akurilov.commons.io.el.ExpressionInput.FACTORY;
import static com.github.akurilov.commons.io.el.ExpressionInput.INIT_MARKER;
import static com.github.akurilov.commons.io.el.ExpressionInput.SYNC_MARKER;
import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

public class ExpressionInputBuilder
implements ExpressionInput.Builder {

	protected final SimpleContext ctx = new SimpleContext();

	protected volatile String expr = null;

	@Override
	public final ExpressionInput.Builder expression(final String expr) {
		this.expr = expr;
		return this;
	}

	@Override
	public final ExpressionInput.Builder function(final String prefix, final String name, final Method method) {
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
	public <U extends ExpressionInput<?>> U build() {
		final var fullExpr = expr;
		final var matcher = EXPRESSION_PATTERN.matcher(fullExpr);
		Object initial = null;
		if(matcher.find()) {
			final var initialValExpr = matcher.group(2);
			if(initialValExpr != null) {
				expr = SYNC_MARKER + initialValExpr.substring(INIT_MARKER.length());
				try(final ExpressionInput initialValueInput = build()) {
					initial = initialValueInput.get();
				} catch(final Exception e) {
					throwUnchecked(e);
				}
			}
			expr = matcher.group(1);
			if(initialValExpr == null && expr == null) {
				throw new IllegalArgumentException(
					"The expression \"" + fullExpr + "\" doesn't match the pattern: " + EXPRESSION_PATTERN.pattern()
				);
			}
		} else {
			throw new IllegalArgumentException(
				"The expression \"" + fullExpr + "\" doesn't match the pattern: " + EXPRESSION_PATTERN.pattern()
			);
		}
		if(null == expr) {
			expr = "";
		}
		// disable the synchronous evaluation on access if "expr" is empty (constant value expression case)
		if(!expr.isEmpty() && expr.startsWith(SYNC_MARKER)) {
			return (U) new SynchronousExpressionInputImpl<>(expr, initial, ctx);
		} else {
			return (U) new ExpressionInputImpl<>(expr, initial, ctx);
		}
	}
}
