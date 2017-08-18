package com.github.akurilov.commons.io.bin.file;

import com.github.akurilov.commons.io.Input;

import java.nio.file.Path;

/**
 * A Java objects file input
 */
public interface FileItemInput<T>
extends Input<T> {

	Path getFilePath();
}
