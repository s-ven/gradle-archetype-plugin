package com.orctom.gradle.archetype.util

import groovy.io.FileType
import groovy.text.GStringTemplateEngine

import java.nio.file.Files

class FileUtils {

  def static engine = new GStringTemplateEngine()

  static List<File> getTemplates(File sourceDir) {
    List<File> list = []
    sourceDir.eachFileRecurse(FileType.ANY) { file ->
      list << file
    }
    list
  }

  static void generate(List<File> templates, Map binding, File sourceDir, File targetDir, Set<String> nonTemplates) {
    if (targetDir.exists()) {
      targetDir.deleteDir()
    }
    targetDir.mkdirs()

    templates.each { source ->
      try {
        File target = new File(targetDir, resolvePaths(getRelativePath(sourceDir, source)))
        String path = engine.createTemplate(target.path).make(binding)
        target = new File(path)
        ensureParentDirs(target)

        if (!isDir(target)) {
          if (isNotTemplate(source.path, nonTemplates)) {
            Files.copy(source.toPath(), target.toPath())
          } else {
            try {
              target << resolve(source.text, binding)
            } catch (Exception e) {
              println "[WARNING] Failed to resolve variables in: ${source.path}"
              Files.copy(source.toPath(), target.toPath())
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace()
      }
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
    pathName.split('/').each {
      if (it.contains('__')) {
        path += resolvePath(it)
      } else {
        path += it
      }

      path += '/'
    }

    path
  }

  static String resolvePath(String path) {
    path.replaceAll('(.*)__(\\w+)__(.*)', '$1\\$\\{$2\\}$3')
  }

  static void ensureParentDirs(File file) {
    if (file.exists()) {
      return
    }

    if (isDir(file)) {
      file.mkdirs()
    } else {
      ensureParentDirs(file.getParentFile())
    }
  }

  static boolean isDir(File file) {
    !file.name.contains('.')
  }
}
