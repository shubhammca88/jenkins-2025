# Jenkins Pipeline Examples for Beginners

## 1. Basic Pipeline
```groovy
pipeline {
    agent any
    
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World!'
            }
        }
    }
}
```

## 2. Multi-Stage Pipeline
```groovy
pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'echo "Building application..."'
                sh 'npm install'
                sh 'npm run build'
            }
        }
        
        stage('Test') {
            steps {
                sh 'echo "Running tests..."'
                sh 'npm test'
            }
        }
        
        stage('Deploy') {
            steps {
                sh 'echo "Deploying application..."'
            }
        }
    }
}
```

## 3. Pipeline with Environment Variables
```groovy
pipeline {
    agent any
    
    environment {
        APP_NAME = 'my-app'
        VERSION = '1.0.0'
        DEPLOY_ENV = 'staging'
    }
    
    stages {
        stage('Info') {
            steps {
                echo "Building ${APP_NAME} version ${VERSION}"
                echo "Target environment: ${DEPLOY_ENV}"
            }
        }
        
        stage('Build') {
            steps {
                sh 'echo "Building ${APP_NAME}:${VERSION}"'
            }
        }
    }
}
```

## 4. Conditional Pipeline
```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh 'echo "Always build"'
            }
        }
        
        stage('Test') {
            when {
                branch 'main'
            }
            steps {
                sh 'echo "Testing on main branch"'
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                sh 'echo "Deploying to staging"'
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                sh 'echo "Deploying to production"'
            }
        }
    }
}
```

## 5. Pipeline with Parameters
```groovy
pipeline {
    agent any
    
    parameters {
        string(name: 'ENVIRONMENT', defaultValue: 'dev', description: 'Target environment')
        booleanParam(name: 'RUN_TESTS', defaultValue: true, description: 'Run tests?')
        choice(name: 'VERSION', choices: ['1.0', '1.1', '1.2'], description: 'Version to deploy')
    }
    
    stages {
        stage('Info') {
            steps {
                echo "Environment: ${params.ENVIRONMENT}"
                echo "Run tests: ${params.RUN_TESTS}"
                echo "Version: ${params.VERSION}"
            }
        }
        
        stage('Test') {
            when {
                expression { params.RUN_TESTS == true }
            }
            steps {
                sh 'echo "Running tests..."'
            }
        }
        
        stage('Deploy') {
            steps {
                sh "echo 'Deploying version ${params.VERSION} to ${params.ENVIRONMENT}'"
            }
        }
    }
}
```

## 6. Pipeline with Parallel Stages
```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh 'echo "Building application..."'
            }
        }
        
        stage('Parallel Tests') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        sh 'echo "Running unit tests..."'
                        sh 'npm run test:unit'
                    }
                }
                stage('Integration Tests') {
                    steps {
                        sh 'echo "Running integration tests..."'
                        sh 'npm run test:integration'
                    }
                }
                stage('Lint') {
                    steps {
                        sh 'echo "Running linter..."'
                        sh 'npm run lint'
                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                sh 'echo "Deploying application..."'
            }
        }
    }
}
```

## 7. Pipeline with Error Handling
```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                script {
                    try {
                        sh 'npm install'
                        sh 'npm run build'
                    } catch (Exception e) {
                        echo "Build failed: ${e.getMessage()}"
                        currentBuild.result = 'FAILURE'
                        error("Build stage failed")
                    }
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    try {
                        sh 'npm test'
                    } catch (Exception e) {
                        echo "Tests failed: ${e.getMessage()}"
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed'
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        unstable {
            echo 'Pipeline is unstable'
        }
    }
}
```

## 8. Pipeline with Docker
```groovy
pipeline {
    agent any
    
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    def image = docker.build("my-app:${BUILD_NUMBER}")
                }
            }
        }
        
        stage('Test in Container') {
            steps {
                script {
                    docker.image("my-app:${BUILD_NUMBER}").inside {
                        sh 'npm test'
                    }
                }
            }
        }
        
        stage('Push Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image("my-app:${BUILD_NUMBER}").push()
                        docker.image("my-app:${BUILD_NUMBER}").push('latest')
                    }
                }
            }
        }
    }
}
```

## 9. Pipeline with Shared Library
```groovy
@Library('my-shared-library') _

pipeline {
    agent any
    
    stages {
        stage('Hello') {
            steps {
                sayHello('Jenkins User')
            }
        }
        
        stage('Build Docker') {
            steps {
                dockerBuild('my-app', env.BUILD_NUMBER)
            }
        }
        
        stage('Notify') {
            steps {
                buildNotification('SUCCESS', 'team@company.com')
            }
        }
    }
}
```

## 10. Complete CI/CD Pipeline
```groovy
pipeline {
    agent any
    
    environment {
        APP_NAME = 'my-web-app'
        DOCKER_REGISTRY = 'my-registry.com'
        KUBECONFIG = credentials('k8s-config')
    }
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
        booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'Skip tests?')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(
                        script: 'git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'npm install'
                sh 'npm run build'
                archiveArtifacts artifacts: 'dist/**/*', fingerprint: true
            }
        }
        
        stage('Test') {
            when {
                not { params.SKIP_TESTS }
            }
            parallel {
                stage('Unit Tests') {
                    steps {
                        sh 'npm run test:unit'
                        publishTestResults testResultsPattern: 'test-results.xml'
                    }
                }
                stage('Lint') {
                    steps {
                        sh 'npm run lint'
                    }
                }
                stage('Security Scan') {
                    steps {
                        sh 'npm audit'
                    }
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    def image = docker.build("${DOCKER_REGISTRY}/${APP_NAME}:${GIT_COMMIT_SHORT}")
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'registry-credentials') {
                        image.push()
                        image.push('latest')
                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    sh """
                        helm upgrade --install ${APP_NAME} ./helm-chart \\
                            --set image.tag=${GIT_COMMIT_SHORT} \\
                            --set environment=${params.ENVIRONMENT} \\
                            --namespace ${params.ENVIRONMENT}
                    """
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        waitUntil {
                            script {
                                def response = sh(
                                    script: "curl -s -o /dev/null -w '%{http_code}' http://${APP_NAME}-${params.ENVIRONMENT}.example.com/health",
                                    returnStdout: true
                                ).trim()
                                return response == '200'
                            }
                        }
                    }
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            emailext (
                subject: "✅ Deployment Successful: ${APP_NAME} - ${BUILD_NUMBER}",
                body: "Successfully deployed ${APP_NAME} to ${params.ENVIRONMENT}",
                to: 'team@company.com'
            )
        }
        failure {
            emailext (
                subject: "❌ Deployment Failed: ${APP_NAME} - ${BUILD_NUMBER}",
                body: "Failed to deploy ${APP_NAME} to ${params.ENVIRONMENT}",
                to: 'team@company.com'
            )
        }
    }
}
```

These examples progress from simple to complex, showing various Jenkins pipeline features and patterns commonly used in real-world scenarios.