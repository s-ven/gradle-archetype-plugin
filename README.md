# gradle-archetype-plugin

Maven archetype like plugin for Gradle.
Generating projects from local template.

## Install
https://plugins.gradle.org/plugin/com.orctom.archetype

## Use

### Interactive Mode:
```
gradle generate
```

### Batch Mode:
```
gradle generate -Dtarget=generated -Dgroup=com.xxx.yyy -Dname=dummy-service -Dversion=1.0-SNAPSHOT
```

### Settings in File

To save answering the same question always the same or to save repeating the same command-line arguments, you
can add the variables into `gradle settings`. Example:

```
systemProp.group=org.my
systemProp.version=0.1-SNAPSHOT
systemProp.strategy=fail
```

Just keep in mind, that system property set this way will not be not overridden on command line.

## Settings

### Template Folder
Default to: `src/main/resources/templates`

Can be overridden by `-Dtemplates=your-template-folder`

### Generated Project(s) Folder
Default to: `generated`
Will recreate this folder on every run.

Can be override by `-Dtarget=folder-name`

### Non-templates:
Files that will not be resoled by variables, as they would fail if try to resolve.
Put the non-template lists to `src/main/resources/.nontemplates`.

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

Follows ant style. The tailing slash for directory is mandatory.

### Conflict Resolution Strategy

The strategy (from system property `strategy`) says what approach to take when a file exist in the target path:
 
  * **sweep** - the target directory is deleted before the generation starts,
   ensuring there won't be any conflicts
  * **overwrite** - conflicting target file is overwritten
  * **fail** - generation stops, all files generated so far are deleted, non-destructive, i.e. **recommended** 
 

### Variables:

| name | description | sample |
| ---- | ----------- | ------ |
| group | project.group | com.xxx.yyy |
| name  | project.name  | dummy-app |
| version | project.version | 1.0-SNAPSHOT |
| projectName | project.name | dummy-app |
| packageName | (group + name) replaced non-characters with '.' | com.xxx.yyy.dummy.app |
| packagePath | replaced '.' with '/' in packageName | com/xxx/yyy/dummy/app |

### Token Format
In code: `@variable@`
In file name: `__variable__`

## Sample
https://github.com/orctom/gradle-archetype-plugin/tree/master/src/test/resources/sample

## Known Issues
 * Doesn't work with property files that have such escapes: key=https`\`://aaa.bbb.ccc/xxx, remove the `\` escape to have it work.