package com.github.akurilov.commons.io.text;

import com.github.akurilov.commons.io.Input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 Text file based lines output
 */
public class TextFileOutput
extends LinesBufferedStreamOutput {
	
	private final Path filePath;
	
	public TextFileOutput(final Path filePath)
	throws IOException {
		super(Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE));
		this.filePath = filePath;
	}
	
	@Override
	public Input<String> getInput()
	throws IOException {
		return new TextFileInput(filePath);
	}
}
