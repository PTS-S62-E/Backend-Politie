node {
    // Get Artifactory server instance, defined in the Artifactory Plugin administration page.
    def server = Artifactory.server "Artifactory"
    // Create an Artifactory Gradle instance.
    def rtGradle = Artifactory.newGradleBuild()
    def rtMaven = Artifactory.newMavenBuild();
    def buildInfo

    stage('Clone sources') {
            git url: 'https://github.com/PTS-S62-E/Backend-Politie.git'
        }

        stage('Artifactory configuration') {
            // Tool name from Jenkins configuration
            rtGradle.tool = "Gradle-4.7"
            rtMaven.tool = "Maven 3.5.3"
            // Set Artifactory repositories for dependencies resolution and artifacts deployment.
            rtGradle.deployer repo:'libs-release-local', server: server
            rtGradle.resolver repo:'jcenter', server: server
        }

        stage('Gradle build') {
            buildInfo = rtGradle.run buildFile: 'build.gradle', tasks: 'clean artifactoryPublish'
        }

        stage('Analyze') {
            rtMaven.run pom: 'pom.xml', goals: 'sonar:sonar -Dsonar.host.url=http://85.144.215.28:9001 -Dsonar.login=089ecbe71f30a12f9af77098b09921b83cf88786'
        }

        stage('Publish build info') {
            server.publishBuildInfo buildInfo
        }

        stage('Deploy to wildfly') {
            rtMaven.run pom: 'pom.xml', goals: 'wildfly:deploy -Dwildfly.address=192.168.24.100 -Dwildfly.hostname=192.168.24.100 -Dwildfly.port=9990 -Dwildfly.username=admin -Dwildfly.password=proftaak'
        }
}