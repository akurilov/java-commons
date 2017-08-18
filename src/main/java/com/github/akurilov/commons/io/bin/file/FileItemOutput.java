package com.github.akurilov.commons.io.bin.file;

import com.github.akurilov.commons.io.Output;

import java.nio.file.Path;

/**
 * A Java objects file output
 */
public interface FileItemOutput<T>
extends Output<T> {

	Path getFilePath();
}
