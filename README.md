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
gradle generate -Dtarget=target -Dgroup=com.xxx.yyy -Dname=dummy-service -Dversion=1.0-SNAPSHOT
```

### Template Folder
`src/main/resources/templates`

### Non-templates:
Files that will not be resoled by variables, as they would fail if try to resolve.
Put the non-template lists to `src/main/resource/.nontemplates`.

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
```

Follows ant style. The tailing slash for directory is mandatory.

### Variables:

| name | description | sample |
| ---- | ----------- | ------ |
| group | project.group | com.xxx.yyy |
| name  | project.name  | dummy-app |
| version | project.version | 1.0-SNAPSHOT |
| projectName | project.name | dummy-app |
| packageName | (group + name) replace non-characters to '.' | com.xxx.yyy.dummy.app |
| packagePath | replace '.' with '/' in packageName | com/xxx/yyy/dummy/app |


### Token
`@variable@`

## Sample
https://github.com/orctom/gradle-archetype-plugin/tree/master/src/test/resources/sample