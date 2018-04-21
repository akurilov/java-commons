package com.github.akurilov.commons.collection;

import com.github.akurilov.commons.system.SizeInBytes;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The range described with at least one bound (begin or end position) and optional size.
 */
public final class Range
implements Externalizable {
	
	private long beg;
	private long end;
	private long size;

	public Range() {
	}

	/**
	 * Copy constructor
	 */
	public Range(final Range other) {
		this.beg = other.getBeg();
		this.end = other.getEnd();
		this.size = other.getSize();
	}

	public Range(final long beg, final long end, final long size) {
		this.beg = beg;
		this.end = end;
		this.size = size;
	}

	/**
	 * @param rawRange range textual representation
	 * @throws InvalidRangeException
	 * @throws NumberFormatException
	 */
	public Range(final String rawRange)
	throws InvalidRangeException, NumberFormatException {
		if(rawRange.startsWith("-")) {
			if(rawRange.endsWith("-")) {
				if(rawRange.length() > 2) {
					beg = end = -1;
					size = SizeInBytes.toFixedSize(rawRange.substring(1, rawRange.length() - 1));
				} else {
					throw new InvalidRangeException("Invalid range string: \""+ rawRange + "\"");
				}
			} else {
				if(rawRange.length() > 1) {
					beg = -1;
					end = SizeInBytes.toFixedSize(rawRange.substring(1));
					size = -1;
				} else {
					throw new InvalidRangeException("Invalid range string: \""+ rawRange + "\"");
				}
			}
		} else if(rawRange.endsWith("-")) {
			if(rawRange.length() > 1) {
				beg = SizeInBytes.toFixedSize(rawRange.substring(0, rawRange.length() - 1));
				end = -1;
				size = -1;
			} else {
				throw new InvalidRangeException("Invalid range string: \""+ rawRange + "\"");
			}
		} else if(rawRange.contains("-")){
			final String[] pair = rawRange.split("-");
			if(pair.length == 2) {
				beg = SizeInBytes.toFixedSize(pair[0]);
				end = SizeInBytes.toFixedSize(pair[1]);
				size = -1;
			} else {
				throw new InvalidRangeException("Invalid range string: \""+ rawRange + "\"");
			}
		} else {
			throw new InvalidRangeException("Invalid range string: \""+ rawRange + "\"");
		}
	}

	/**
	 * @return range start offset
	 */
	public final long getBeg() {
		return beg;
	}

	/**
	 * @return range end offset
	 */
	public final long getEnd() {
		return end;
	}

	/**
	 Note that this method may return -1 if begin and end are set (size is not -1 actually)
	 @return the size of the range having no position
	 */
	public final long getSize() {
		return size;
	}

	/**
	 * @return the range textual representation
	 */
	public final String toString() {
		if(beg == -1) {
			if(end == -1) {
				return "-" + size + "-";
			}
			return "-" + end;
		} else if(end == -1) {
			return beg + "-";
		} else {
			return beg + "-" + end;
		}
	}

	@Override
	public final void writeExternal(final ObjectOutput out)
	throws IOException {
		out.writeLong(beg);
		out.writeLong(end);
		out.writeLong(size);
	}

	@Override
	public final void readExternal(ObjectInput in)
	throws IOException, ClassNotFoundException {
		beg = in.readLong();
		end = in.readLong();
		size = in.readLong();
	}
}
