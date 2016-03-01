package com.orctom.gradle.archetype.util

import groovy.io.FileType
import groovy.text.SimpleTemplateEngine

class FileUtils {

  static List<File> getTemplates(File sourceDir) {
    List<File> list = []
    sourceDir.eachFileRecurse(FileType.ANY) { file ->
      list << file
    }
    list
  }

  static void generate(List<File> templates, Map binding, File sourceDir, File targetDir) {
    println('generate................')
    binding.each {entry ->
      println entry.key + " -> " + entry.value
    }
    def engine = new SimpleTemplateEngine()
    templates.each { source ->
      println source.path
      def target = new File(targetDir, getRelativePath(sourceDir, source))
      target << engine.createTemplate(source).make(binding)
    }
  }

  static String getRelativePath(File sourceDir, File file) {
    sourceDir.toURI().relativize( file.toURI()).toString()
  }

  static String transformFileName(String pathName) {

  }
}
