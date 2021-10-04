
def notifyGitHub(status) {
    def NODE_NAME = "wifosell-be"
    if(NOTIFY_GITHUB == "true") {
        context = "JenkinsCI/${NODE_NAME}"
        run_type = 'Build'

        if(STAGE_NAME == 'test-continuous') {
            context = context + "/test-continuous"
            run_type = 'Continuous test'
        }

        switch(status) {
            case 'PENDING':
                message = 'Stage: ' + (env.PARENT_STAGE_NAME ?: STAGE_NAME)
                break
            case 'SUCCESS':
                message = run_type + ' succeeded!'
                break
            case 'FAILURE':
                message = run_type + ' failed!'
                break
            case 'ABORTED':
                message = run_type + ' aborted!'
                status == 'ERROR'
                break
        }

        step([$class: 'GitHubCommitStatusSetter',
              contextSource: [$class: 'ManuallyEnteredCommitContextSource', context: context],
              statusResultSource: [$class: 'ConditionalStatusResultSource',
              results: [[$class: 'AnyBuildResult', message: message, state: status]]]])
    }
}

pipeline {
  agent any
  environment {
    AGENT_OS_NAME = getOSName()
    JOB_TYPE = getJobType()
    NOTIFY_GITHUB = "true"
    TESTER = 'placeholder'

  }
  
  
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

    post {
      always {
        notifyGitHub("${currentBuild.result}")
      }
    }

  }

  post {
    always {
      notifyGitHub("${currentBuild.result}")
    }
  }

  tools {
    maven 'Maven 3.6.3'
  }

}