/*
 * #%L
 * How to Continue from a Subdirectory with SimpleFileVisitor
 * %%
 * Copyright (C) 2012 - 2015 Java Creed
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.javacreed.examples.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

public class CustomSimpleFileVisitor extends SimpleFileVisitor<Path> {

  private List<String> continueFrom;

  private Path rootPath;

  private FileVisitResult preVisitContinueFromResult = FileVisitResult.SKIP_SUBTREE;

  @Override
  public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
    if (rootPath == null) {
      rootPath = dir;
      return FileVisitResult.CONTINUE;
    }

    if (preVisitContinueFromResult != FileVisitResult.CONTINUE) {
      if (continueFrom == null) {
        preVisitContinueFromResult = FileVisitResult.CONTINUE;
      } else if (rootPath.equals(dir)) {
        return FileVisitResult.CONTINUE;
      } else {
        final Path relative = rootPath.relativize(dir);
        final int index = relative.getNameCount() - 1;
        if (index < continueFrom.size()) {
          final String dirName = dir.getFileName().toString();
          final String name = continueFrom.get(index);

          if (name.equals(dirName)) {
            if (index == continueFrom.size() - 1) {
              preVisitContinueFromResult = FileVisitResult.CONTINUE;
            }

            return FileVisitResult.CONTINUE;
          }
        }
      }
    }

    return preVisitContinueFromResult;
  }

  public void setContinueFrom(final String... path) {
    continueFrom = Arrays.asList(path);
  }

  public void setRootPath(final Path root) {
    rootPath = root;
  }
}
