# gradle-archetype-plugin

Maven archetype like plugin for Gradle.
Generating projects from local template.

## Install
https://plugins.gradle.org/plugin/com.orctom.archetype

## Use
### Interactive mode:
```
gradle generate
```

### Batch mode:
```
gradle generate -Dtarget=generated -Dgroup=com.xxx.yyy -Dname=dummy-service -Dversion=1.0-SNAPSHOT
```

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

### Variables:

| name | description | sample |
| ---- | ----------- | ------ |
| group | project.group | com.xxx.yyy |
| name  | project.name  | dummy-app |
| version | project.version | 1.0-SNAPSHOT |
| projectName | project.name | dummy-app |
| packageName | (group + name) replaced non-characters with '.' | com.xxx.yyy.dummy.app |
| packagePath | replaced '.' with '/' in packageName | com/xxx/yyy/dummy/app |


### Token
`@variable@`

## Sample
https://github.com/orctom/gradle-archetype-plugin/tree/master/src/test/resources/sample

## Known Issues
 * Doesn't work with property files that have such escapes: key=https`\`://aaa.bbb.ccc/xxx, remove the `\` escape to have it work.