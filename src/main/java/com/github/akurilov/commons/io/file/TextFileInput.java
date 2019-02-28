package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.TextStreamInput;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 Text file based lines input
 */
public class TextFileInput
extends TextStreamInput
implements FileInput<String> {

	private final Path filePath;

	public TextFileInput(final Path filePath) {
		super(open(filePath));
		this.filePath = filePath;
	}

	static InputStream open(final Path filePath) {
		try {
			return Files.newInputStream(filePath, INPUT_OPEN_OPTIONS);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return null;
	}

	@Override
	public Path filePath() {
		return filePath;
	}
}
