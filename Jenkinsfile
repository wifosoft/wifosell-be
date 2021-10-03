pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Initiating maven build'
        sh 'mvn clean install -Dlicense.skip=true'
        echo 'Maven build complete'
      }
    }

    stage('Testing') {
      parallel {
        stage('SonarQube Test') {
          steps {
            echo 'Initiating SonarQube test'
            sh 'echo "Testing by sonar"'
            echo 'SonarQube test Complete'
          }
        }

        stage('Print Build Number') {
          steps {
            sleep 3
            echo "This is build number ${BUILD_ID}"
          }
        }

      }
    }

    stage('JFrog Push') {
      steps {
        script {
          echo 'Pulling...' + env.BRANCH_NAME
          def mvnHome = tool 'Maven 3.6.3'
          if (isUnix()) {
            def targetVersion = getDevVersion()
            print 'target build version...'
            print targetVersion
            sh "'${mvnHome}/bin/mvn' -Dintegration-tests.skip=true -Dbuild.number=${targetVersion} clean package"
            def pom = readMavenPom file: 'pom.xml'
            // get the current development version
            developmentArtifactVersion = "${pom.version}-${targetVersion}"
            print pom.version
            // execute the unit testing and collect the reports
            junit '**//*target/surefire-reports/TEST-*.xml'
            archive 'target*//*.jar'
          } else {
            bat(/"${mvnHome}\bin\mvn" -Dintegration-tests.skip=true clean package/)
            def pom = readMavenPom file: 'pom.xml'
            print pom.version
            junit '**//*target/surefire-reports/TEST-*.xml'
            archive 'target*//*.jar'
          }
        }

      }
    }

    stage('Deploy prompt') {
      steps {
        input 'Deploy to Production?'
      }
    }

    stage('Deploy') {
      steps {
        echo 'Initiating Deployment'
        echo 'Deployment Complete'
      }
    }

  }
  tools {
    maven 'Maven 3.6.3'
  }
  environment {
    TESTER = 'placeholder'
  }
}