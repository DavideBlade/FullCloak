/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.util;

import com.gmail.davideblade99.fullcloak.FullCloak;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUtil {

    public final static File CONFIG_FILE = new File(FullCloak.getInstance().getDataFolder(), "config.yml");

    private FileUtil() {
        throw new IllegalAccessError();
    }

    /**
     * Copy an embedded file to another location
     *
     * @param inFile  the name of embedded file to be copied
     * @param outFile the file where specified embedded file should be copied
     */
    public static void copyFile(@NotNull final String inFile, @NotNull final File outFile) {
        outFile.getParentFile().mkdirs(); // Create output folder

        // Copy contents of inFile to outFile
        try (final InputStream input = FullCloak.getInstance().getResource(inFile); final OutputStream output = new FileOutputStream(outFile)) {
            final byte[] buf = new byte[1024];
            int number;
            while ((number = input.read(buf)) > 0)
                output.write(buf, 0, number);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}