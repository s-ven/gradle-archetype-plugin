package com.orctom.gradle.archetype

import org.gradle.api.Plugin
import org.gradle.api.Project

class ArchetypePlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.task('generate', type: ArchetypeTask)
  }

}
