pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Starting Build Step'
        sh 'mvn clean install -Dlicense.skip=true'
      }
    }

    stage('Print build number') {
      steps {
        echo 'This is build the number ${BUILD_ID}'
      }
    }

    stage('JFrog') {
      steps {
        script {
          def server = Artifactory.server "artifactory"
          def buildInfo = Artifactory.newBuildInfo()
          def rtMaven = Artifactory.newMavenBuild()
          rtMaven.tool = 'maven'
          rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
          buildInfo = rtMaven.run pom: 'pom.xml', goals: "clean install -Dlicense.skip=true"
          buildInfo.env.capture = true
          buildInfo.name = 'jpetstore-6'
          server.publishBuildInfo buildInfo
        }

      }
    }

    stage('Deploy Prompt') {
      steps {
        input 'Deploy to Production?'
        echo 'Deploymnet Completed'
      }
    }

  }
}