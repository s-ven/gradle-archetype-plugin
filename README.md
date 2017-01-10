## gradle-archetype-plugin

Maven archetype like plugin for Gradle.
Generating projects from local template.

### Install
https://plugins.gradle.org/plugin/com.orctom.archetype

### Tasks
 * `clean`: cleans the generated folders and files.
 * `generate`: generates projects from the template.

### Interactive Mode:
```
gradle clean generate -i
```

### Batch Mode:
```
gradle clean generate -i -Dtarget=generated -Dgroup=com.xxx.yyy -Dname=dummy-service -Dversion=1.0-SNAPSHOT
```

### Parameters
#### Prompted
Following parameters will be asked, if not available in system properties

| Param           | Description                                         | Default                        |
| --------------- | ----------------------------------------------------| ------------------------------ |
| group           | group name in Gradle or Maven, *Mandatory*          |                                |
| name            | name in Gradle, of artifactId in Maven, *Mandatory* |                                |
| version         | version in Gradle or Maven, *Mandatory*             | 1.0-SNAPSHOT                   |

#### Won't Be Prompted
Following parameters will NOT be prompted, if not available in system properties.

| Param           | Description                                         | Default                        |
| --------------- | ----------------------------------------------------| ------------------------------ |
| templates       | The folder path where template locates, *Mandatory* | `src/main/resources/templates` |
| failIfFileExist | Fail if there are files with the same name exist in the `generated` folder; otherwise overwrite | `true` |

#### System Properties
Parameters will firstly been searched in System Properties, which includes:

 * gradle.properties: systemProp.param1=value1
 * settings.properties: systemProp.param1=value1
 * ~/.gradle/gradle.properties (not suggested for this plugin)
 * Command line: -Dparam1=value1 -Dparam2=value2 -Dparam3=value3

### Variables:
Variables that can be used in template files.

| name         | description                                        | sample                |
| ------------ | -------------------------------------------------- | --------------------- |
| group        | project.group                                      | com.xxx.yyy           |
| name         | project.name                                       | dummy-app             |
| version      | project.version                                    | 1.0-SNAPSHOT          |
| projectName  | project.name                                       | dummy-app             |
| namePackage  | replaced non-characters with '.' in name           | dummy.app             |
| namePath     | replaced non-characters with '/' in name           | dummy/app             |
| groupPath    | replaced '.' with '/' in group                     | com/xxx/yyy           |
| packageName  | (group + name) replaced non-characters with '.'    | com.xxx.yyy.dummy.app |
| packagePath  | replaced '.' with '/' in packageName               | com/xxx/yyy/dummy/app |

Extra variables can be added in command line:
```
-Dparam1=value1 -Dparam2=value2 -Dparam3=value3 ...
```

### Token Format
 * In code: `@variable@`
 * In file name: `__variable__`

### Generated Project(s) Folder
Fixed to: `generated`.

### Non-templates:
Files that will not be resoled by variables, as they would fail if tried to resolve.
Put the non-template lists to `.nontemplates` file,
and put the file to template folder (such as `src/main/resources/templates`) or `src/main/resources/`.

Sample:
```
**/*.jar
**/*.bat
**/*.sh
**/*.zip
**/*.gz
**/*.xz
**/*.tar
**/*.7z
gradle/
.gradle/
gradlew
gradlew.bat
```

It follows ant style. The tailing slash for directory is mandatory.

### Sample
https://github.com/orctom/gradle-archetype-plugin/tree/master/src/test/resources/sample

### Known Issues
 * Doesn't work with property files that have such escapes: key=https`\`://aaa.bbb.ccc/xxx, remove the `\` escape to have it work.
 * In interactive mode, the prompt text got truncated sometimes.

### Change Logs
#### 1.3.1
 * Added variables: `namePackage` and `namePath`

#### 1.3
 * The target folder where the generated project(s) locates is not changeable, fixed to `generated`.
 * The generation will fail by default, if there are files with the same name exist in the `generated` folder.
 * Added `clean` task that will have `generated` folder recreated.
 * Changed `print` and `println` to logger, so please append `-i` args to have the log printed out.