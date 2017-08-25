package com.github.akurilov.commons.io.file;

import com.github.akurilov.commons.io.Input;

import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * A Java objects file input
 */
public interface FileInput<T>
extends Input<T> {

	OpenOption[] INPUT_OPEN_OPTIONS = new OpenOption[] { StandardOpenOption.READ };

	Path getFilePath();
}
