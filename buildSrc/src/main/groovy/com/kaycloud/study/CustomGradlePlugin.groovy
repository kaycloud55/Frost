package com.kaycloud.study

import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomGradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "Hello gradle plugin.." + project.name
    }
}