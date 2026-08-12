package com.anggrayudi.storage.file

import androidx.documentfile.provider.DocumentFile
import java.io.File

/**
 * Created on 03/06/21
 *
 * @see File.makeFile
 * @see File.makeFolder
 * @see DocumentFile.makeFile
 * @see DocumentFile.makeFolder
 * @author Anggrayudi H
 */
enum class CreateMode {

  /**
   * For folder case:
   * * If the conflicted file is a file, then return `null`.
   * * If the conflicted file is a folder, then use this directory.
   * * If folder `A/B` already exists under this folder and you're trying to create `A/B`, then
   *   return `A/B`.
   * * If folder `A/B` already exists under this folder and you're trying to create `A/B/C`, then
   *   create folder `C` and return `A/B/C`.
   *
   * For file case:
   * * If the conflicted file is a folder, then return `null`.
   * * If the conflicted file is a file, then return this.
   */
  REUSE,

  /**
   * For folder case:
   * * If the conflicted file is a file, then delete it and create a new directory.
   * * If the conflicted file is a folder, then delete all of its children.
   *
   * For file case:
   * * If the conflicted file is a folder, then delete it and create a new file.
   * * If the conflicted file is a file, then recreate it.
   *
   * @see File.recreateFile
   * @see File.forceDelete
   * @see DocumentFile.recreateFile
   * @see DocumentFile.deleteRecursively
   */
  REPLACE,

  /**
   * * If a conflict is raised, then create a new file/folder with auto-incremented file name, e.g.
   *   `My Video (1).mp4` and `My Documents (2)`.
   * * If folder `A` already exists under this folder and you're trying to create `A`, then return
   *   `A (1)`.
   * * If folder `A/B` already exists under this folder and you're trying to create `A/B`, then
   *   return `A (1)/B`.
   */
  CREATE_NEW,

  /** If the file already exists, then return null. */
  SKIP_IF_EXISTS,
}
