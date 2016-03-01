package com.orctom.gradle.archetype

import com.orctom.gradle.archetype.util.ConsoleUtils
import com.orctom.gradle.archetype.util.FileUtils
import groovy.io.FileType
import groovy.text.SimpleTemplateEngine
import org.gradle.api.Plugin
import org.gradle.api.Project

class ArchetypePlugin implements Plugin<Project> {

  static File sourceDir
  static File targetDir
  static Map binding = [:]

  @Override
  void apply(Project project) {
    sourceDir = new File(project.projectDir, System.getProperty('templates', './src/main/resources/templates'))
    def target = getParam('target', 'Please enter the target folder name where the generated project locates', '../')
    if (target.startsWith('/')) {
      targetDir = new File(target)
    } else {
      targetDir = new File(project.projectDir, target)
    }

    String projectGroup = getParam('group', 'Please enter the group name')
    String projectName = getParam('name', 'Please enter the project name')
    String projectVersion = getParam('version', 'Please enter the version name', '1.0-SNAPSHOT')

    binding = ['group': projectGroup, 'name': projectName, 'version': projectVersion]
    binding << getExtendedBinding()

    List<File> templates = FileUtils.getTemplates(sourceDir)
    FileUtils.generate(templates, sourceDir, targetDir)
  }

  static Map getExtendedBinding() {
    Map extendedBinding = [:]
    [:]
  }

  static String getParam(String name, String prompt, String defaultValue = null) {
    String value = System.getProperty(name)
    if (null != value) {
      value = ConsoleUtils.prompt(prompt, defaultValue)
    }

    if (null == value) {
      throw new IllegalArgumentException("Not specified: $name")
    }
    return value
  }
}
