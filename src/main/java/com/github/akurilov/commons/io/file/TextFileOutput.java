package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.Input;
import com.github.akurilov.commons.io.TextStreamOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 Text file based lines output
 */
public class TextFileOutput
extends TextStreamOutput
implements FileOutput<String> {
	
	private final Path filePath;

	public TextFileOutput()
	throws IOException {
		this(Files.createTempFile(null, ".txt"));
	}

	public TextFileOutput(final Path filePath)
	throws IOException {
		super(Files.newOutputStream(filePath, OUTPUT_OPEN_OPTIONS));
		this.filePath = filePath;
	}
	
	@Override
	public Input<String> getInput()
	throws IOException {
		return new TextFileInput(filePath);
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}
}
