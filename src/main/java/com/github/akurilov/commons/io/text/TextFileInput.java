package com.github.akurilov.commons.io.text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 Text file based lines input
 */
public class TextFileInput
extends LinesBufferedStreamInput {
	
	public TextFileInput(final Path filePath)
	throws IOException {
		super(Files.newInputStream(filePath, StandardOpenOption.READ));
	}
}
