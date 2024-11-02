pipeline {
    environment {
        git_branch = 'main'
        registry = "fadyzaafrane/tpfoyer-17"
        registryCredential = 'dockerhub_id'
        dockerImage = ''
        kubeConfigCredentialId = 'kubeCredentials'
        awsCredentialsId = 'awsCredentials'
    }

    agent any

    tools {
        maven "maven"
    }

    stages {
        stage('CHECKOUT GIT') {
            steps {
                script {
                    git branch: env.git_branch, url: 'https://github.com/FaderFtw/IGL5-G1-ProjetFoyer'
                }
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
               script {
                   if (env.GIT_BRANCH == 'develop') {
                       sh '''
                           mvn deploy -s /var/jenkins_home/.m2/settings.xml \
                           -DaltDeploymentRepository=snapshotRepo::default::http://nexus:8081/repository/maven-snapshots/
                       '''
                   } else if (env.GIT_BRANCH == 'main') {
                       sh '''
                           mvn deploy -s /var/jenkins_home/.m2/settings.xml \
                           -DaltDeploymentRepository=releaseRepo::default::http://nexus:8081/repository/maven-releases/
                       '''
                   }
               }
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

        // Stage to deploy on Kubernetes
        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                script {
                    // Inject kubeconfig and AWS credentials
                    withCredentials([file(credentialsId: kubeConfigCredentialId, variable: 'KUBECONFIG'),
                                     file(credentialsId: awsCredentialsId, variable: 'AWS_CREDENTIALS_FILE')]) {
                        // Test AWS Credentials
                        sh 'aws sts get-caller-identity' // Ensure AWS CLI can access the credentials

                        // Print kubeconfig content for debugging (optional)
                        echo "Kubeconfig content: ${readFile(env.KUBECONFIG).trim()}"

                        // Deploy to Kubernetes using the specified kubeconfig
                        sh "kubectl --kubeconfig=${env.KUBECONFIG} apply -f deployment.yaml"

                        sh "kubectl --kubeconfig=${env.KUBECONFIG} apply -f service.yaml"
                    }
                }
            }
        }
    }
}