package com.orctom.gradle.archetype.util

import groovy.io.FileType
import groovy.text.GStringTemplateEngine
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import java.nio.file.Files

class FileUtils {

  static final Logger log = Logging.getLogger(getClass().name)
  static engine = new GStringTemplateEngine()

  /** Provides collection of all files from the source directory.
   *
   * @param sourceDir source directory, i.e. directory with the templates(s)
   * @return list of all files in the source directory, including sub-directories
   */
  static List<File> getTemplates(File sourceDir) {
    log.info("source dir: '{}'", sourceDir.name)

    List<File> sourceFiles = []
    sourceDir.eachFileRecurse(FileType.ANY) { file ->
      sourceFiles << file
    }

    sourceFiles
  }

  /** Provides collection of all non-templates files from the source directory.
   *
   * @param sourceDir source directory, i.e. directory with the templates(s)
   * @param defaultNonTemplatesFile location of the default non-templates files
   *
   * @return set of all non-templates files in the source directory, including sub-directories
   */
  static Set<String> getNonTemplates(defaultNonTemplatesFile, sourceDir) {
    Set<String> nonTemplates = []

    // TODO: allow template-specific settings overriding the default
    Set<String> nonTemplatesWildcards = defaultNonTemplatesFile.readLines() as Set
    FileNameFinder finder = new FileNameFinder()
    nonTemplatesWildcards.each {
      def files = finder.getFileNames(sourceDir.path, it)
      nonTemplates.addAll(files)
    }

    nonTemplates
  }

  static void generate(
      List<File> templates,
      Map binding,
      File sourceDir,
      File targetDir,
      Set<String> nonTemplates) {

    log.info("target dir: '{}'", targetDir.name)

    // TODO: introduce variable to be able to disable default overwriting/deletion of existing targetDir,
    // if the targetDir is not empty - possible values: never, ask, always
    if (targetDir.exists()) {
      log.warn("removing existing target dir: '{}'", targetDir.absolutePath)
      targetDir.deleteDir()
    }

    targetDir.mkdirs()

    logBindings(binding)

    templates.each {
      source ->
      try {
        File target = new File(targetDir, resolvePaths(getRelativePath(sourceDir, source)))
        String path = engine.createTemplate(target.path).make(binding)
        target = new File(path)
        boolean isFile = source.isFile()
        ensureParentDirs(target, isFile)

        if (isFile) {
          if (isNotTemplate(source.path, nonTemplates)) {
            Files.copy(source.toPath(), target.toPath())
          } else {
            try {
              target << resolve(source.text, binding)
            } catch (Exception ex) {
              log.error("Failed to resolve variables in: '{}]", source.path)
              log.error(ex.getMessage())
              Files.copy(source.toPath(), target.toPath())
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace()
      }
    }

    log.info('Done')
  }

  static def logBindings(Map map) {
    map.each {
      k, v -> log.info("variable: {}='{}'", k, v)
    }
  }

  static boolean isNotTemplate(String source, Set<String> nonTemplates) {
    source in nonTemplates
  }

  static String resolve(String text, Map binding) {
    String escaped = text.replaceAll('\\$', '__DOLLAR__')
    String ready = escaped.replaceAll('@(\\w+)@', '\\$\\{$1\\}')
    String resolved = engine.createTemplate(ready).make(binding)
    String done = resolved.replaceAll('__DOLLAR__', '\\$')
    done
  }

  static String getRelativePath(File sourceDir, File file) {
    sourceDir.toURI().relativize(file.toURI()).toString()
  }

  static String resolvePaths(String pathName) {

    if (!pathName.contains('__')) {
      pathName
    }

    String path = '';
    pathName.split(File.separator).each {
      if (it.contains('__')) {
        path += resolvePath(it)
      } else {
        path += it
      }

      path += File.separator
    }

    path
  }

  // replaces __variable__ with ${variable}
  static String resolvePath(String path) {
    path.replaceAll('(.*)__(\\w+)__(.*)', '$1\\$\\{$2\\}$3')
  }

  static void ensureParentDirs(File file, boolean isFile) {
    if (isFile) {
      file.getParentFile().mkdirs();
    } else {
      file.mkdirs();
    }
  }
}
