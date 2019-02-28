package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.Input;
import com.github.akurilov.commons.io.TextStreamOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 Text file based lines output
 */
public class TextFileOutput
extends TextStreamOutput
implements FileOutput<String> {
	
	private final Path filePath;

	public TextFileOutput() {
		this(createTempFile());
	}

	static Path createTempFile() {
		try {
			return Files.createTempFile(null, ".txt");
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return null;
	}

	public TextFileOutput(final Path filePath) {
		super(open(filePath));
		this.filePath = filePath;
	}

	static OutputStream open(final Path filePath) {
		try {
			return Files.newOutputStream(filePath, OUTPUT_OPEN_OPTIONS);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return null;
	}
	
	@Override
	public Input<String> getInput() {
		return new TextFileInput(filePath);
	}

	@Override
	public Path filePath() {
		return filePath;
	}
}
