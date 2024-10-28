pipeline {
    environment {
        registry = "fadyzaafrane/tpfoyer-17"  // Replace with your Docker Hub username
        registryCredential = 'dockerhub_id'
        dockerImage = ''
        kubeConfigCredentialId = 'kubeCredentials'
        awsCredentialsId = 'awsCredentials'
        imageTag = 'latest'
        awsRegion = 'us-east-1'
        clusterName = 'KubeCluster'
    }

    agent any

    tools {
        maven "maven"
    }

    stages {
        stage('CHECKOUT GIT') {
            steps {
                git branch: "develop", url:'https://github.com/FaderFtw/IGL5-G1-ProjetFoyer'
            }
        }

        stage('MVN CLEAN') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('ARTIFACT CONSTRUCTION') {
            steps {
                echo 'ARTIFACT CONSTRUCTION...'
                sh 'mvn package -Dmaven.test.skip=true -P test-coverage'
            }
        }

        stage('UNIT TESTS') {
            steps {
                echo 'Launching Unit Tests...'
                sh 'mvn test'
            }
        }


        stage('MVN SONARQUBE'){
            steps {
                 script {
                    withSonarQubeEnv('sonar') {
                        sh """ mvn sonar:sonar \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml """
                    }
                }
            }
        }


        stage("PUBLISH TO NEXUS") {
            steps {
                sh 'mvn deploy -s /var/jenkins_home/.m2/settings.xml'
            }
        }

        stage('BUILDING OUR IMAGE') {
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }

        stage('DEPLOY OUR IMAGE') {
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
        }

        stage('Docker Compose Up') {
            steps {
                script {
                    def imageTag = "latest"
                    sh """
                    export IMAGE_TAG=${imageTag}
                    export registry=${registry}
                    docker-compose up -d
                    """
                }
            }
        }


        stage('Test AWS Credentials') {
            steps {
                withCredentials([file(credentialsId: awsCredentialsId, variable: 'AWS_CREDENTIALS_FILE')]) {
                    script {
                        def awsCredentials = readFile(AWS_CREDENTIALS_FILE).trim().split("\n")
                        env.AWS_ACCESS_KEY_ID = awsCredentials.find { it.startsWith("aws_access_key_id") }.split("=")[1].trim()
                        env.AWS_SECRET_ACCESS_KEY = awsCredentials.find { it.startsWith("aws_secret_access_key") }.split("=")[1].trim()
                        env.AWS_SESSION_TOKEN = awsCredentials.find { it.startsWith("aws_session_token") }?.split("=")[1]?.trim()

                        echo "AWS Access Key ID: ${env.AWS_ACCESS_KEY_ID}"
                        echo "AWS Credentials File Loaded"
                    }
                }
            }
        }

        stage('Terraform Setup') {
            steps {
                script {
                    dir('terraform') { // Change to the terraform subdirectory
                        sh '''
                            terraform init
                            terraform validate
                            terraform apply -auto-approve
                        '''
                    }
                }
            }
        }

        stage('Get Cluster Credentials') {
            steps {
                sh "aws eks --region ${env.awsRegion} update-kubeconfig --name ${env.clusterName}"
            }
        }

        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                script {
                    sh """
                    sed -i 's|image: .*|image: ${registry}:${imageTag}|' APP_deployment.yaml
                    cat APP_deployment.yaml
                    """
                    sh "kubectl apply -f DB_deployment.yaml"
                    sh "kubectl rollout status deployment/my-db"
                    sh "kubectl apply -f APP_deployment.yaml"
                }
            }
        }
    }
}