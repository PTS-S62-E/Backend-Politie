node {
    // Get Artifactory server instance, defined in the Artifactory Plugin administration page.
    def server = Artifactory.server "Artifactory"
    // Create an Artifactory Gradle instance.
    def rtGradle = Artifactory.newGradleBuild()

    stage 'Clone sources'
        git url: 'https://github.com/PTS-S62-E/Backend-Politie.git'

    stage 'Build'
        rtGradle.run rootDir: "/", buildFile: 'build.gradle', tasks: 'build'

    stage 'Test'
        rtGradle.run rootDir: "/", buildFile: 'build.gradle', tasks: 'test'

    stage 'Artifactory configuration'
        // Tool name from Jenkins configuration
        rtGradle.tool = "Gradle-4.7"
        // Set Artifactory repositories for dependencies resolution and artifacts deployment.
        rtGradle.deployer repo:'libs-gradle-release-local', server: server
        rtGradle.resolver repo:'libs-gradle-release', server: server

    stage 'Gradle build'
        def buildInfo = rtGradle.run rootDir: "/", buildFile: 'build.gradle', tasks: 'clean artifactoryPublish'

    stage 'Publish build info'
        server.publishBuildInfo buildInfo
}