/*
 * Community Rust Plugin
 * Copyright (C) 2021-2025 Vladimir Shelkovnikov
 * mailto:community-rust AT pm DOT me
 * http://github.com/C4tWithShell/community-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.communityrust.clippy;

import java.io.File;
import java.nio.file.Path;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;

public class FileAdjustor {
  private final FileSystem fileSystem;
  private int relativePathOffset = 0;

  private FileAdjustor(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  public static FileAdjustor create(SensorContext context) {
    return new FileAdjustor(context.fileSystem());
  }

  public String relativePath(String fileName) {
    if (isKnown(fileName)) {
      return fileName;
    }

    String normalizedPath = chooseSeparator(fileName);
    if (normalizedPath == null) {
      return fileName;
    }

    // Handle absolute paths by removing the root component
    Path path = Path.of(normalizedPath);
    if (path.isAbsolute()) {
      path = path.getRoot().relativize(path);
    }
    
    int pathNameCount = path.getNameCount();

    if (relativePathOffset > 0) {
      if (pathNameCount > relativePathOffset) {
        path = path.subpath(relativePathOffset, pathNameCount);
        if (isKnown(path)) {
          return path.toString();
        }
      }
      return fileName;
    }

    // Try to find the correct offset by removing path components from the start
    for (int i = 0; i < pathNameCount - 1; i++) {
      Path subpath = path.subpath(i, pathNameCount);
      if (isKnown(subpath)) {
        relativePathOffset = i;
        return subpath.toString();
      }
    }

    // Try just the file name as a last resort
    Path lastComponent = path.getFileName();
    if (isKnown(lastComponent)) {
      relativePathOffset = pathNameCount - 1;
      return lastComponent.toString();
    }

    return fileName;
  }

  private static String chooseSeparator(String path) {
    return isWindows() ? windowsSeparators(path) : unixSeparators(path);
  }

  private static boolean isWindows() {
    return File.separatorChar == '\\';
  }

  private static String unixSeparators(String path) {
    return path.indexOf(92) != -1 ? path.replace('\\', '/') : path;
  }

  private static String windowsSeparators(String path) {
    return path.indexOf(47) != -1 ? path.replace('/', '\\') : path;
  }

  private boolean isKnown(Path path) {
    return isKnown(path.toString());
  }

  private boolean isKnown(String path) {
    return fileSystem.hasFiles(fileSystem.predicates().hasPath(path));
  }
}