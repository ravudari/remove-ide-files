package com.felix.remove.ide.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author ravudari
 *
 */
public class RemoveIdeFiles {
	private static final boolean DEBUG = false;
	private static final List<String> IDE_FILENAMES = Arrays.asList(".classpath", ".project", ".factorypath",
			".settings");
	private static final String EXCLUDE_PATH_NAME = "node_modules";

	public static void main(String[] args) throws IOException {
		String location = "C:\\Users\\rajua\\delete";
		int depth = 2;
		if (args != null && args.length > 0) {
			location = args[0];
			if (args.length > 1) {
				depth = Integer.parseInt(args[1]);
			}
		}

		System.out.println("Given location: " + location + "; depth: " + depth);

		Path locationPath = Paths.get(location);
		if (Files.notExists(locationPath)) {
			System.err.println("Given location doesn't exist");
			return;
		}
		if (!Files.isDirectory(locationPath)) {
			System.err.println("Given location isn't a directory");
			return;
		}

		System.out.println("Working on location: " + locationPath.toAbsolutePath());
		System.out.println("Excluding the files contains path name: " + EXCLUDE_PATH_NAME);
		try (Stream<Path> walkStream = Files.walk(locationPath, depth)) {
			walkStream.filter(sub -> !sub.toString().contains(EXCLUDE_PATH_NAME))
					.filter(sub -> IDE_FILENAMES.contains(sub.getFileName().toString())).forEach(deletingPath -> {
						System.out.println("Deleting: " + deletingPath);
						try (Stream<Path> deletableStream = Files.walk(deletingPath)) {
							deletableStream.sorted(Comparator.reverseOrder()).forEach(delPath -> {
								try {
									Files.delete(delPath);
									if (DEBUG) {
										System.out.println(" -- Deleted --> " + delPath);
									}
								} catch (Exception e) {
									System.err.println(
											"Errror while deleting the file: " + delPath + "; " + e.getMessage());
								}
							});
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
		}
	}
}
