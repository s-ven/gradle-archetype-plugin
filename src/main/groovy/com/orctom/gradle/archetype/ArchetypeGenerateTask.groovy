package com.orctom.gradle.archetype

import com.orctom.gradle.archetype.util.ConsoleUtils
import com.orctom.gradle.archetype.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction

import java.util.regex.Matcher

class ArchetypeGenerateTask extends DefaultTask {

  static final Logger LOGGER = Logging.getLogger(ArchetypeGenerateTask.class)

  @TaskAction
  create() {
    String projectGroup = getParam('group', 'Please enter the group name')
    String projectName = getParam('name', 'Please enter the project name')
    String projectVersion = getParam('version', 'Please enter the version name', '1.0-SNAPSHOT')

    String templatePath = System.getProperty('templates', 'src/main/resources/templates')

    Map binding = [
        'group'          : projectGroup,
        'groupId'        : projectGroup,
        'name'           : projectName,
        'projectName'    : projectName,
        'artifactId'     : projectName,
        'version'        : projectVersion,
        'project.version': projectVersion
    ]
    extendedBinding(binding)
    logBindings(binding)

    FileUtils.generate(project.projectDir, templatePath, binding, isFailIfFileExist())
  }

  private static void extendedBinding(Map binding) {
    addCommandLinePropertiesToBinding(binding)

    String group = binding.get('group')

    binding.put('groupPath', group.replaceAll('\\.', File.separator))

    String packageName = group + '/' + binding.get('name')
    String normalizedPackageName = packageName.replaceAll('//', '/')

    binding.put('packageName', normalizedPackageName.replaceAll('\\W', '.'))
    binding.put('packagePath', normalizedPackageName.replaceAll('\\W', Matcher.quoteReplacement(File.separator)))
  }

  private static void addCommandLinePropertiesToBinding(binding) {
    String extraProperties = System.getProperty("sun.java.command")
    if (null != extraProperties) {
      extraProperties.split('\\s+').each { item ->
        int equalSignIndex
        if (item.startsWith("-D") && (equalSignIndex = item.indexOf('=')) > 2) {
          String key = item.substring(2, equalSignIndex)
          String value = item.substring(equalSignIndex + 1, item.length())
          binding.put(key, value)
        }
      }
    }
  }

  private static String getParam(String paramName, String prompt, String defaultValue = null) {
    String value = System.getProperty(paramName)

    if (!value) {
      value = ConsoleUtils.prompt(prompt, defaultValue)
    }

    if (!value) {
      throw new IllegalArgumentException("Parameter required: $paramName")
    }

    return value
  }

  private static void logBindings(Map map) {
    LOGGER.info('Variables:')
    map.each { k, v -> LOGGER.info("  {}='{}'", k.padRight(25), v) }
  }

  private static boolean isFailIfFileExist() {
    String value = System.getProperty('failIfFileExist', 'y').trim().toLowerCase().charAt(0)
    'y' == value || 't' == value || '1' == value
  }

  @Override
  String getGroup() {
    'Archetype'
  }

  @Override
  String getDescription() {
    'Generates project(s) from template(s)'
  }
}
