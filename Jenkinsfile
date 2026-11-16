pipeline {
    agent any

    tools {
        maven 'Maven3'   // Must match the name configured in Jenkins Global Tool Configuration
    }

    environment {
       PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
       JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21'  // Adjust to your actual JDK path


        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_TOKEN = credentials('SONAR_TOKEN') //make sure a right token is selected
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKERHUB_REPO = 'amirdirin/bmi_3013_f1'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/ADirin/RTL_BMI_Solution_Instructions.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${env.SONARQUBE_SERVER}") {
                    bat """
                        sonar-scanner ^
                        -Dsonar.projectKey=BMI_CL ^
                        -Dsonar.sources=src ^
                        -Dsonar.projectName=BMI_CL ^
                        -Dsonar.host.url=http://localhost:9000 ^
                        -Dsonar.login=${env.SONAR_TOKEN} ^
                        -Dsonar.java.binaries=target/classes
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def app = docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                    env.BUILT_IMAGE = app.id  // Optional: store image ID for reuse
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                       docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }


}
