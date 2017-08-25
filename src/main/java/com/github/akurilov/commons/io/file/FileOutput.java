package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.Output;

import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * A Java objects file output
 */
public interface FileOutput<T>
extends Output<T> {

	OpenOption[] OUTPUT_OPEN_OPTIONS = new OpenOption[] {
		StandardOpenOption.APPEND,
		StandardOpenOption.CREATE,
		StandardOpenOption.WRITE
	};

	Path getFilePath();
}
