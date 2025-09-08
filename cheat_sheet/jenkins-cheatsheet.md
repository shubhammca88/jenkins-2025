# Jenkins Quick Reference Cheat Sheet

## Essential Jenkins CLI Commands

### Job Management
```bash
# List all jobs
java -jar jenkins-cli.jar -s http://jenkins_url list-jobs

# Build a job
java -jar jenkins-cli.jar -s http://jenkins_url build job_name

# Build with parameters
java -jar jenkins-cli.jar -s http://jenkins_url build job_name -p param1=value1 -p param2=value2

# Get job configuration
java -jar jenkins-cli.jar -s http://jenkins_url get-job job_name > job.xml

# Create job from XML
java -jar jenkins-cli.jar -s http://jenkins_url create-job new_job < job.xml

# Delete job
java -jar jenkins-cli.jar -s http://jenkins_url delete-job job_name

# Disable/Enable job
java -jar jenkins-cli.jar -s http://jenkins_url disable-job job_name
java -jar jenkins-cli.jar -s http://jenkins_url enable-job job_name
```

### Node Management
```bash
# List nodes
java -jar jenkins-cli.jar -s http://jenkins_url list-nodes

# Take node offline
java -jar jenkins-cli.jar -s http://jenkins_url offline-node node_name

# Bring node online
java -jar jenkins-cli.jar -s http://jenkins_url online-node node_name
```

### Plugin Management
```bash
# List installed plugins
java -jar jenkins-cli.jar -s http://jenkins_url list-plugins

# Install plugin
java -jar jenkins-cli.jar -s http://jenkins_url install-plugin plugin_name

# Update plugins
java -jar jenkins-cli.jar -s http://jenkins_url install-plugin plugin_name -restart
```

## Pipeline Syntax Quick Reference

### Basic Pipeline Structure
```groovy
pipeline {
    agent any
    
    environment {
        VAR_NAME = 'value'
    }
    
    stages {
        stage('Stage Name') {
            steps {
                // Your steps here
            }
        }
    }
    
    post {
        always { /* cleanup */ }
        success { /* on success */ }
        failure { /* on failure */ }
    }
}
```

### Common Pipeline Steps
```groovy
// Shell commands
sh 'echo "Hello World"'
sh '''
    echo "Multi-line"
    echo "shell script"
'''

// Checkout code
checkout scm

// Archive artifacts
archiveArtifacts artifacts: '*.jar', fingerprint: true

// Publish test results
publishTestResults testResultsPattern: 'test-results.xml'

// Stash/Unstash files
stash includes: '**/*', name: 'source'
unstash 'source'

// Parallel execution
parallel {
    'Test A': { sh 'test-a.sh' },
    'Test B': { sh 'test-b.sh' }
}
```

### Conditional Execution
```groovy
when {
    branch 'main'
    environment name: 'DEPLOY', value: 'true'
    expression { return params.RUN_TESTS == true }
    anyOf {
        branch 'main'
        branch 'develop'
    }
}
```

### Error Handling
```groovy
// Retry on failure
retry(3) {
    sh 'flaky-command.sh'
}

// Timeout
timeout(time: 5, unit: 'MINUTES') {
    sh 'long-running-command.sh'
}

// Try-catch
script {
    try {
        sh 'risky-command.sh'
    } catch (Exception e) {
        echo "Command failed: ${e.getMessage()}"
        currentBuild.result = 'UNSTABLE'
    }
}
```

## Environment Variables

### Built-in Variables
```bash
BUILD_NUMBER        # Current build number
BUILD_ID           # Build timestamp
JOB_NAME           # Name of the job
WORKSPACE          # Job workspace directory
JENKINS_URL        # Jenkins server URL
BUILD_URL          # URL of current build
NODE_NAME          # Name of the node
EXECUTOR_NUMBER    # Executor number

# Git variables (when using Git SCM)
GIT_COMMIT         # Git commit hash
GIT_BRANCH         # Git branch name
GIT_URL            # Git repository URL
```

### Using Variables in Pipeline
```groovy
pipeline {
    agent any
    environment {
        CUSTOM_VAR = 'my_value'
        PATH = "${env.PATH}:/custom/path"
    }
    stages {
        stage('Example') {
            steps {
                echo "Build number: ${BUILD_NUMBER}"
                echo "Custom var: ${CUSTOM_VAR}"
                sh 'echo "Workspace: $WORKSPACE"'
            }
        }
    }
}
```

## Credentials Management

### Using Credentials in Pipeline
```groovy
// Username/Password
withCredentials([usernamePassword(credentialsId: 'my-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh 'docker login -u $USER -p $PASS'
}

// Secret text
withCredentials([string(credentialsId: 'api-key', variable: 'API_KEY')]) {
    sh 'curl -H "Authorization: Bearer $API_KEY" api.example.com'
}

// SSH key
withCredentials([sshUserPrivateKey(credentialsId: 'ssh-key', keyFileVariable: 'SSH_KEY')]) {
    sh 'ssh -i $SSH_KEY user@server'
}

// Certificate
withCredentials([certificate(credentialsId: 'cert', keystoreVariable: 'KEYSTORE', passwordVariable: 'KEYSTORE_PASSWORD')]) {
    sh 'keytool -list -keystore $KEYSTORE -storepass $KEYSTORE_PASSWORD'
}
```

## Useful Groovy Snippets

### File Operations
```groovy
// Read file
def content = readFile 'config.txt'

// Write file
writeFile file: 'output.txt', text: 'Hello World'

// Check if file exists
if (fileExists('config.txt')) {
    echo 'File exists'
}

// List files
def files = sh(script: 'ls -la', returnStdout: true)
echo files
```

### JSON/XML Processing
```groovy
// Parse JSON
def jsonString = '{"name": "Jenkins", "version": "2.0"}'
def json = readJSON text: jsonString
echo "Name: ${json.name}"

// Parse XML
def xmlString = '<root><name>Jenkins</name></root>'
def xml = readXML text: xmlString
echo "Name: ${xml.name}"
```

### HTTP Requests
```groovy
// GET request
def response = httpRequest 'http://api.example.com/status'
echo "Status: ${response.status}"
echo "Content: ${response.content}"

// POST request
httpRequest httpMode: 'POST', 
           url: 'http://api.example.com/webhook',
           contentType: 'APPLICATION_JSON',
           requestBody: '{"message": "Build completed"}'
```

## Debugging Tips

### Debug Information
```groovy
// Print all environment variables
sh 'printenv | sort'

// Print current directory and files
sh 'pwd && ls -la'

// Debug pipeline variables
echo "Debug Info:"
echo "- Stage: ${env.STAGE_NAME}"
echo "- Build: ${env.BUILD_NUMBER}"
echo "- Node: ${env.NODE_NAME}"

// Capture command output
script {
    def result = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
    echo "Git commit: ${result}"
}
```

### Common Issues & Solutions

1. **Permission Denied**
   ```bash
   chmod +x script.sh
   ```

2. **Workspace Cleanup**
   ```groovy
   cleanWs()  // Clean workspace after build
   ```

3. **Large Log Files**
   ```groovy
   // Limit log output
   sh 'command 2>&1 | head -100'
   ```

4. **Memory Issues**
   ```groovy
   // Use different agent with more memory
   agent { label 'large-memory' }
   ```

## Performance Tips

### Optimize Build Time
```groovy
// Skip checkout for certain stages
options {
    skipDefaultCheckout()
}

// Use parallel stages
parallel {
    'Unit Tests': { sh 'npm test' },
    'Lint': { sh 'npm run lint' },
    'Security Scan': { sh 'npm audit' }
}

// Cache dependencies
script {
    if (!fileExists('node_modules')) {
        sh 'npm install'
    }
}
```

### Resource Management
```groovy
// Limit concurrent builds
options {
    disableConcurrentBuilds()
}

// Set build timeout
options {
    timeout(time: 30, unit: 'MINUTES')
}

// Cleanup workspace
post {
    always {
        cleanWs()
    }
}
```

This cheat sheet covers the most commonly used Jenkins commands and patterns for daily development work.