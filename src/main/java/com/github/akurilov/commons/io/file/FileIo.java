package com.github.akurilov.commons.io.file;

import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public interface FileIo {

	OpenOption[] INPUT_OPEN_OPTIONS = new OpenOption[] { StandardOpenOption.READ };

	OpenOption[] OUTPUT_OPEN_OPTIONS = new OpenOption[] {
		StandardOpenOption.APPEND,
		StandardOpenOption.CREATE,
		StandardOpenOption.WRITE
	};

	Path filePath();

}
