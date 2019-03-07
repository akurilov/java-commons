package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Input;

import java.util.Arrays;
import java.util.List;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

public final class CompositeStringInput
implements Input<String> {

	private static final ThreadLocal<StringBuilder> STR_BUILDER = ThreadLocal.withInitial(StringBuilder::new);

	private final Object[] segments;
	private final int count;

	public CompositeStringInput(final Object[] segments) {
		this.segments = segments;
		this.count = segments.length;
	}

	@Override @SuppressWarnings("unchecked")
	public final String get() {
		final var strBuilder = STR_BUILDER.get();
		Object segment;
		for(var i = 0; i < count; i ++) {
			segment = segments[i];
			if(segment instanceof String) {
				strBuilder.append((String) segment);
			} else if(segment instanceof Input) {
				strBuilder.append(((Input<String>) segment).get());
			}
		}
		final var result = strBuilder.toString();
		strBuilder.setLength(0);
		return result;
	}

	@Override
	public final int get(final List<String> buffer, final int limit) {
		for(var i = 0; i < limit; i ++) {
			buffer.add(get());
		}
		return limit;
	}

	@Override
	public final long skip(final long count) {
		Arrays.stream(segments)
			.filter(segment -> segment instanceof Input)
			.map(segment -> (Input) segment)
			// this quick and dirty implementation doesn't care about actually skipped count
			.forEach(input -> input.skip(count));
		return count;
	}

	@Override
	public final void reset() {
		Arrays.stream(segments)
			.filter(segment -> segment instanceof Input)
			.map(segment -> (Input) segment)
			.forEach(Input::reset);
	}

	@Override
	public final void close() {
		Object segment;
		for(var i = 0; i < count; i ++) {
			segment = segments[i];
			segments[i] = null; // release the reference ASAP for GC
			if(segment instanceof Input) {
				try {
					((Input) segment).close();
				} catch(final Exception e) {
					throwUnchecked(e);
				}
			}
		}

	}
}
