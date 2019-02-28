package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.BinOutput;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 * <p>An item input implementation serializing something to the specified file.</p>
 * <p><b>WARNING</b>: doesn't support the appending of the previously used output file (already
 * containing the data which had been output earlier). This limitation is due to Java serialization,
 * which adds the new header on each new ObjectOutputStream instance.</p>
 */
public class BinFileOutput<T>
extends BinOutput<T>
implements FileOutput<T> {
	
	protected final Path dstPath;

	/**
	 @param dstPath the path to the file which should be used to store the serialized items
	 @throws IOException if unable to open the file for writing
	 */
	public BinFileOutput(final Path dstPath) {
		super(open(dstPath));
		this.dstPath = dstPath;
	}

	static ObjectOutputStream open(final Path dstPath) {
		try {
			return new ObjectOutputStream(
				new BufferedOutputStream(Files.newOutputStream(dstPath, OUTPUT_OPEN_OPTIONS))
			);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return null;
	}

	/**
	 * Opens the temporary output file with ".bin" extension and random name.
	 */
	public BinFileOutput() {
		this(createTempFile());
		this.dstPath.toFile().deleteOnExit();
	}

	static Path createTempFile() {
		try {
			return Files.createTempFile(null, ".bin");
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return null;
	}
	
	@Override
	public BinFileInput<T> getInput() {
		return new BinFileInput<>(dstPath);
	}
	
	@Override
	public String toString() {
		return "binFileOutput<" + dstPath.getFileName() + ">";
	}
	
	@Override
	public final Path filePath() {
		return dstPath;
	}
}
