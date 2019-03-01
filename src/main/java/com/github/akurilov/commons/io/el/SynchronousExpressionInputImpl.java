package com.github.akurilov.commons.io.el;

import de.odysseus.el.util.SimpleContext;

import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import java.util.List;

public class SynchronousExpressionInputImpl<T>
extends ExpressionInputImpl<T>
implements SynchronousExpressionInput<T> {

	public SynchronousExpressionInputImpl(
		final String exprStr, final T initial, final Class<T> resultType, final SimpleContext ctx
	) {
		super(exprStr, initial, resultType, ctx);
	}

	@Override
	public final T get()
	throws PropertyNotFoundException, ELException {
		try {
			return call();
		} catch(final Throwable t) {
			t.printStackTrace(System.err);
		}
		return null;
	}

	@Override
	public final int get(final List<T> buffer, final int limit)
	throws PropertyNotFoundException, ELException {
		for(var i = 0; i < limit; i ++) {
			buffer.add(call());
		}
		return limit;
	}

	@Override
	public final long skip(final long count)
	throws PropertyNotFoundException, ELException {
		for(var i = 0L; i < count; i ++) {
			call();
		}
		return count;
	}
}
