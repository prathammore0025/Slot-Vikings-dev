def PROJECT_NAME = "Slot-Vikings-dev"
def UNITY_VERSION = "2022.3.47f1"
def UNITY_INSTALLATION = "C:\\Program Files\\Unity\\Hub\\Editor\\${UNITY_VERSION}\\Editor\\Unity.exe"
def REPO_URL = "https://github.com/prathammore0025/Slot-Vikings-dev.git"

pipeline {
    agent {
        label 'Windows-Agent' // Specify the agent by label
    }
    
    environment {
        PROJECT_PATH = "C:\\Program Files\\workspace\\Unity_build" // Define project path based on Jenkins pipeline name
        Token = credentials('GITHUB_TOKEN') // Use GitHub credentials
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git url: REPO_URL, branch: 'dev'
                }
            }
        }

        stage('Build WebGL') {
            steps {
                script {
                    withEnv(["UNITY_PATH=${UNITY_INSTALLATION}"]) {
                        bat '''
                        "%UNITY_PATH%" -quit -batchmode -projectPath "%PROJECT_PATH%" -executeMethod BuildScript.BuildWebGL -logFile -
                        '''
                    }
                }
            }
        }

        stage('Push Build') {
            steps {
                script {
                    // Ensure you are in the build directory
                    bat '''
                    git checkout dev-build
                    git checkout dev -- Builds
                    git add Builds
                    git commit -m "Added Builds folder from dev branch"
                    git config user.email "moreprathmesh849@gmail.com"
                    git config user.name "prathammore0025"
                    git remote set-url origin https://${Token}@github.com/prathammore0025/Slot-Vikings-dev.git
                    git push origin dev-build
                    '''
                }
            }
        }
    }
}
