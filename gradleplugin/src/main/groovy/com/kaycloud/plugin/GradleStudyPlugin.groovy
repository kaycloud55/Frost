package com.kaycloud.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 自定义gradle插件
 */
class GradleStudyPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        println "hello plugin : ${project.name}"
    }
}