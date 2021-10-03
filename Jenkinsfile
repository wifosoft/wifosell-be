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

    stage('Deploy') {
      steps {
        echo 'Initiating Deployment'
        echo 'Deployment Complete'
        sh '''echo \'------- start copy jar to /home/stackjava/workspace ------------\'
cp /var/lib/jenkins/workspace/wifosell-be_main/target/Zeus-0.0.1-SNAPSHOT.jar /home/workspace/zeus/zeus.jar
echo \'------- finish copy jar -------------------------------------\'
echo \'------- restart zeus service------------------\'
sudo systemctl restart zeus
echo \'------- finish restart zeus service\'
sudo systemctl status zeus
echo \'------- finish restart zeus service\'
'''
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