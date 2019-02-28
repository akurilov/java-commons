package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.BinInput;
import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An item input implementation deserializing something from the specified file.
 */
public class BinFileInput<T>
extends BinInput<T>
implements FileInput<T> {
	
	protected final Path srcPath;

	/**
	 @param srcPath the path to the file which should be used to restore the serialized items
	 @throws IOException if unable to open the file for reading
	 */
	public BinFileInput(final Path srcPath) {
		super(buildObjectInputStream(srcPath));
		this.srcPath = srcPath;
	}
	
	protected static ObjectInputStream buildObjectInputStream(final Path itemsSrcPath) {
		try {
			return new ObjectInputStream(
				new BufferedInputStream(Files.newInputStream(itemsSrcPath, INPUT_OPEN_OPTIONS))
			);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "binFileInput<" + srcPath.getFileName() + ">";
	}
	
	@Override
	public final Path filePath() {
		return srcPath;
	}
	
	@Override
	public void reset() {
		if(itemsSrc != null) {
			try {
				itemsSrc.close();
			} catch(final IOException e) {
				throwUnchecked(e);
			}
		}
		setItemsSrc(buildObjectInputStream(srcPath));
	}
}
