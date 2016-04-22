package com.orctom.gradle.archetype

import com.orctom.gradle.archetype.util.ConsoleUtils
import com.orctom.gradle.archetype.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ArchetypeTask extends DefaultTask {

  static File sourceDir
  static File targetDir
  static Set<String> nonTemplates = []

  @TaskAction
  def create() {
    String projectGroup = getParam('group', 'Please enter the group name')
    String projectName = getParam('name', 'Please enter the project name')
    String projectVersion = getParam('version', 'Please enter the version name', '1.0-SNAPSHOT')

    sourceDir = new File(project.projectDir, System.getProperty('templates', 'src/main/resources/templates'))
    def target = getParam('target', 'Please enter the target folder name where the generated project locates', 'generated')
    if (target.startsWith('/')) {
      targetDir = new File(target, projectName)
    } else {
      targetDir = new File(project.projectDir, target + '/' + projectName)
    }

    Set<String> nonTemplatesWildcards = new File(project.projectDir, 'src/main/resources/.nontemplates').readLines() as Set
    FileNameFinder finder = new FileNameFinder()
    nonTemplatesWildcards.each {
      def files = finder.getFileNames(sourceDir.path, it)
      nonTemplates.addAll(files)
    }

    Map binding = [
        'group': projectGroup,
        'groupId': projectGroup,
        'name': projectName,
        'projectName': projectName,
        'artifactId': projectName,
        'version': projectVersion,
        'project.version': projectVersion
    ]
    extendedBinding(binding)

    List<File> templates = FileUtils.getTemplates(sourceDir)
    FileUtils.generate(templates, binding, sourceDir, targetDir, nonTemplates)
  }

  static void extendedBinding(Map binding) {
    String packageName = binding.get('group') + '/' + binding.get('name')
    String normalizedPackageName = packageName.replaceAll('//', '/')
    binding.put('packageName', normalizedPackageName.replaceAll('\\W', '.'))
    binding.put('packagePath', normalizedPackageName.replaceAll('\\W', '/'))

    String extraProperties = System.getProperty("sun.java.command")
    if (null != extraProperties) {
      extraProperties.split('\\s+').each {item ->
        int equalSignIndex
        if (item.startsWith("-D") && ( equalSignIndex = item.indexOf('=')) > 2) {
          String key = item.substring(2, equalSignIndex)
          String value = item.substring(equalSignIndex + 1, item.length())
          if (!binding.containsKey(key)) {
            binding.put(key, value)
          }
        }
      }
    }
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
