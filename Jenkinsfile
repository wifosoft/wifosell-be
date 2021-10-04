def getRepoURL() {
  sh "git config --get remote.origin.url > .git/remote-url"
  return readFile(".git/remote-url").trim()
}

def getCommitSha() {
  sh "git rev-parse HEAD > .git/current-commit"
  return readFile(".git/current-commit").trim()
}

def updateGithubCommitStatus(build) {
  // workaround https://issues.jenkins-ci.org/browse/JENKINS-38674
  repoUrl = getRepoURL()
  commitSha = getCommitSha()

  step([
    $class: 'GitHubCommitStatusSetter',
    reposSource: [$class: "ManuallyEnteredRepositorySource", url: repoUrl],
    commitShaSource: [$class: "ManuallyEnteredShaSource", sha: commitSha],
    errorHandlers: [[$class: 'ShallowAnyErrorHandler']],
    statusResultSource: [
      $class: 'ConditionalStatusResultSource',
      results: [
        [$class: 'BetterThanOrEqualBuildResult', result: 'SUCCESS', state: 'SUCCESS', message: build.description],
        [$class: 'BetterThanOrEqualBuildResult', result: 'FAILURE', state: 'FAILURE', message: build.description],
        [$class: 'AnyBuildResult', state: 'FAILURE', message: 'Loophole']
      ]
    ]
  ])
}

def notifyGitHub(status) {
    def NODE_NAME = "wifosell-be"
    if(env.NOTIFY_GITHUB == "true") {
        context = "JenkinsCI/${NODE_NAME}"
        run_type = 'Build'

        if(STAGE_NAME == 'Testing') {
            context = context + "/Testing"
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
    NOTIFY_GITHUB = "true"
    TESTER = 'placeholder'
  }
  
  
  stages {
    stage('Build') {
      steps {
        notifyGitHub('PENDING')

        echo 'Initiating maven build'
        sh 'mvn clean install -Dlicense.skip=true'
        echo 'Maven build complete'
      }
    }

    stage('Testing') {
      parallel {
        stage('SonarQube Test') {
          steps {
            notifyGitHub('PENDING')

            echo 'Initiating SonarQube test'
            sh 'echo "Testing by sonar"'
            echo 'SonarQube test Complete'
          }
        }

        stage('Print Build Number') {
          steps {
            notifyGitHub('PENDING')
            sleep 1
            echo "This is build number ${BUILD_ID}"
          }
        }

      }
    }

    stage('Deploy') {
      steps {
        notifyGitHub('PENDING')
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
        currentBuild.description = "Success Build! Access to API for using"
        notifyGitHub('SUCCESS')
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