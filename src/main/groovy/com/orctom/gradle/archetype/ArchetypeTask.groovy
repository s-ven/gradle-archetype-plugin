package com.orctom.gradle.archetype

import com.orctom.gradle.archetype.util.ConsoleUtils
import com.orctom.gradle.archetype.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ArchetypeTask extends DefaultTask {

  static File sourceDir
  static File targetDir

  @TaskAction
  def create() {
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

    Map binding = ['group': projectGroup, 'name': projectName, 'version': projectVersion]
    extendBinding(binding)

    List<File> templates = FileUtils.getTemplates(sourceDir)
    FileUtils.generate(templates, binding, sourceDir, targetDir)
  }

  static void extendBinding(Map binding) {
    binding.put('package', binding.get('group'))
    binding.put('projectName', binding.get('name'))
  }

  static String getParam(String name, String prompt, String defaultValue = null) {
    String value = System.getProperty(name)
    if (!value) {
      value = ConsoleUtils.prompt(prompt, defaultValue)
    }

    if (!value) {
      throw new IllegalArgumentException("Not specified: $name")
    }
    return value
  }

  @Override
  String getGroup() {
    'Archetype'
  }

  @Override
  String getDescription() {
    'Generate projects from templates'
  }
}
