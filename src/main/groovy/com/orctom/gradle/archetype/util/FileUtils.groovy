package com.orctom.gradle.archetype.util

import groovy.io.FileType
import groovy.text.SimpleTemplateEngine

class FileUtils {

  static List<File> getTemplates(File sourceDir) {
    List<File> list = []

    sourceDir.eachFileRecurse(FileType.ANY) { file ->
      list << file
    }

    return list
  }

  static void generate(List<File> templates, File sourceDir, File targetDir) {
    def engine = new SimpleTemplateEngine()
    templates.each { source ->
      println it.path
      def target = new File(targetDir, getRelativePath(sourceDir, source))
      target << engine.createTemplate(source).make(binding)
    }
  }

  static String getRelativePath(File sourceDir, File file) {
    sourceDir.toURI().relativize( file.toURI()).toString()
  }
}
