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

	public static void main(String[] args) throws IOException {
		String location = "C:\\Users\\rajua\\delete";
		if (args != null && args.length > 0) {
			location = args[0];
		}

		System.out.println("Given location: " + location);

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
		try (Stream<Path> walkStream = Files.walk(locationPath, 2)) {
			walkStream.filter(sub -> IDE_FILENAMES.contains(sub.getFileName().toString())).forEach(deletingPath -> {
				System.out.println("Deleting: " + deletingPath);
				try (Stream<Path> deletableStream = Files.walk(deletingPath)) {
					deletableStream.sorted(Comparator.reverseOrder()).forEach(delPath -> {
						try {
							Files.delete(delPath);
							if (DEBUG) {
								System.out.println(" -- Deleted --> " + delPath);
							}
						} catch (IOException e) {
							System.err.println("Errror while deleting the file: " + delPath + "; " + e.getMessage());
						}
					});
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
	}
}
