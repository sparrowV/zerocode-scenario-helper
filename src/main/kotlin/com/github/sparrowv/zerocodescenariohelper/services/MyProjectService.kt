package com.github.sparrowv.zerocodescenariohelper.services

import com.intellij.openapi.project.Project
import com.github.sparrowv.zerocodescenariohelper.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
