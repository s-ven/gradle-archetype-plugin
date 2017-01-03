package com.orctom.gradle.archetype

import com.orctom.gradle.archetype.util.ConsoleUtils
import com.orctom.gradle.archetype.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction

import java.nio.file.Path
import java.nio.file.Paths

import static com.orctom.gradle.archetype.ConflictResolutionStrategy.*

class ArchetypeTask extends DefaultTask {

  static final Logger LOGGER = Logging.getLogger(ArchetypeTask.class)

  static File sourceDir
  static File targetDir

  @TaskAction
  create() {

    String projectGroup = getParam('group', 'Please enter the group name')
    String projectName = getParam('name', 'Please enter the project name')
    String projectVersion = getParam('version', 'Please enter the version name', '1.0-SNAPSHOT')

    String strategyString = getParam('strategy', 'Please enter conflict resolution strategy: s)weep / o)verwrite / f)ail', 'fail')
    ConflictResolutionStrategy strategy = getStrategy(strategyString)

    sourceDir = new File(project.projectDir, System.getProperty('templates', 'src/main/resources/templates'))

    def targetDirPath = getParam('target', 'Please enter the target folder name where the generated project locates', 'generated')
    targetDir = new File(targetDirPath)
    if (!Paths.get(targetDirPath).absolute) {// relative path to project root
      targetDir = new File(project.projectDir, targetDirPath)
    }

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

    Path defaultNonTemplatesPath = Paths.get(project.projectDir.absolutePath, 'src', 'main', 'resources', '.nontemplates')
    File defaultNonTemplates = defaultNonTemplatesPath.toFile()

    Set<String> nonTemplates = FileUtils.getNonTemplates(defaultNonTemplates, sourceDir)
    List<File> templates = FileUtils.getTemplates(sourceDir)

    FileUtils.generate(templates, binding, sourceDir, targetDir, nonTemplates, strategy)
  }

  private static ConflictResolutionStrategy getStrategy(String strategyName) {
    if (strategyName.length() == 1) {
      switch (strategyName) {
        case 's': return SWEEP
        case 'o': return OVERWRITE
      //case 'a': return ASK
        case 'f': return FAIL
        default: return FAIL
      }
    } else {
      valueOf(strategyName.toUpperCase())
    }
  }


  static void extendedBinding(Map binding) {
    String packageName = binding.get('group') + '/' + binding.get('name')
    String normalizedPackageName = packageName.replaceAll('//', '/')

    binding.put('packageName', normalizedPackageName.replaceAll('\\W', '.'))
    binding.put('packagePath', normalizedPackageName.replaceAll('\\W', File.separator))

    // process command line
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

  /**
   * Gets value of single a plugin parameter.
   *  The value is get from:
   *  <ol>
   *      <li>system property</il>
   *      <li>users's input</il>
   *  </ol>
   *
   * @param paramName name of the parameter
   * @param prompt prompt to display when the value is going to be retrieved from user input
   * @param defaultValue value to use when user input is empty
   *
   * @return parameter's value
   */
  static String getParam(String paramName, String prompt, String defaultValue = null) {
    String value = System.getProperty(paramName)

    if (!value) {
      value = ConsoleUtils.prompt(prompt, defaultValue)
    }

    if (!value) {
      throw new IllegalArgumentException("Not specified: $paramName")
    }

    return value
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