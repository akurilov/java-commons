package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.TextStreamInput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 Text file based lines input
 */
public class TextFileInput
extends TextStreamInput
implements FileInput<String> {

	private final Path filePath;

	public TextFileInput(final Path filePath)
	throws IOException {
		super(Files.newInputStream(filePath, INPUT_OPEN_OPTIONS));
		this.filePath = filePath;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}
}
