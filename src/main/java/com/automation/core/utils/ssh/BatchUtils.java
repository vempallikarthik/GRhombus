package com.automation.core.utils.ssh;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.automation.core.config.ConfigProperties;

public class BatchUtils {

	public static void triggerBatchforQTP() throws IOException {
		
		Path resourceDirectory = Paths.get("src","test","resources","batch");
        String batchFilePath = resourceDirectory.toFile().getAbsolutePath();

        File dir = new File(batchFilePath);
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "Start",ConfigProperties.BATCH_FILE.get(),
        		ConfigProperties.QTP_RUNNER_LOC.get(),
        		ConfigProperties.QTP_SUITE_LOC.get());
        pb.directory(dir);
        Process p = pb.start();		
	}
}
