package com.duckyshine.app.utilities;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.Deque;
import java.util.Arrays;
import java.util.ArrayDeque;

import java.util.stream.Stream;
import java.util.stream.Collectors;

import com.duckyshine.app.debug.Debug;

public class ResourceFinder {
    public static List<String> getFiles(String directory) {
        Path filepath = ResourceFinder.getResourcePath(directory);

        List<String> filenames = ResourceFinder.getFilenames(filepath);

        if (filenames == null) {
            return null;
        }

        return getAbsolutePaths(filepath, filenames);
    }

    private static List<String> getFilenames(Path filepath) {
        try (Stream<Path> paths = Files.list(filepath)) {
            return paths.map(path -> path.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    private static List<String> getAbsolutePaths(Path filepath, List<String> filenames) {
        return filenames.stream().map(filename -> filepath.resolve(filename).toString()).toList();
    }

    public static Path getResourcePath(String directory) {
        ClassLoader classLoader = ResourceFinder.class.getClassLoader();
        URI uri = null;

        try {
            uri = classLoader.getResource(directory).toURI();
        } catch (URISyntaxException exception) {
            exception.printStackTrace();

            return null;
        }

        return Paths.get(uri);
    }

    public static String getCacheFilepath(String filepath) {
        String[] splitFilepaths = filepath.split("/");

        Path resourcePath = ResourceFinder.getResourcePath(".cache");

        StringBuilder stringBuilder = new StringBuilder(resourcePath.toString());

        Debug.debug(resourcePath);

        Deque<String> queue = new ArrayDeque<>(Arrays.asList(splitFilepaths));

        // while (!queue.isEmpty() && queue.poll()) {

        // }

        return filepath;
    }
}
