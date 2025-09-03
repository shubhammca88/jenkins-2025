
# Jenkins Terms and Commands

Here are some common Jenkins terms and commands explained briefly:

1. **Jenkins**: An open-source automation server used for Continuous Integration/Continuous Deployment (CI/CD).

2. **Pipeline**: A series of automated steps that define the stages of your build, test, and deployment process.

3. **Job**: A single task or process defined in Jenkins. A job can run tests, compile code, or deploy applications.

4. **Build**: The process of compiling or assembling your code and running tests. It can be triggered manually or automatically.

5. **Freestyle Project**: A type of Jenkins job that allows for simple, configurable automation tasks.

6. **Declarative Pipeline**: A type of Jenkins pipeline that uses a predefined structure, making it easier to define stages and steps.

7. **Declarative Syntax Example**
   ```groovy
   pipeline {
     agent any
     stages {
       stage('Build') {
         steps {
           echo 'Building...'
         }
       }
     }
   }
   ```

8. **Node**: A machine where Jenkins runs the build jobs. The master node controls the jobs, and agent nodes perform the tasks.

9. **Workspace**: A directory where Jenkins stores files related to a specific job.

10. **SCM (Source Code Management)**: Refers to systems like Git, which Jenkins integrates with to pull code before building.

11. **Trigger**: An event that starts a job, such as a commit to a repository or a scheduled time.

12. **Blue Ocean**: A modern Jenkins UI that provides a more user-friendly, visually appealing interface for pipelines.

13. **Artifact**: A file or set of files generated during a build process, such as compiled code or packaged applications.

14. **Executor**: A computing resource (on a node) that Jenkins uses to run build jobs.

15. **Workspace Cleanup**: A command to clean up files in the workspace after a build to save space:
    ```bash
    rm -rf $WORKSPACE/*
    ```

16. **Jenkins CLI**: A command-line interface to interact with Jenkins remotely. Example to trigger a job:
    ```bash
    java -jar jenkins-cli.jar -s http://jenkins_url build job_name
    ```

17. **Shared Library**: Reusable code that can be shared across multiple Jenkins pipelines to avoid duplication.

18. **Agent**: A machine or container where Jenkins executes pipeline steps. Can be the master or a separate worker node.

19. **Stage**: A logical division of work in a pipeline, such as 'Build', 'Test', or 'Deploy'.

20. **Step**: Individual commands or actions within a stage, like running a shell command or archiving files.

These terms and commands form the core components of Jenkins.

## Jenkins Cheat Sheet - Essential Commands

### Jenkins CLI Commands
```bash
# Download Jenkins CLI
wget http://jenkins_url/jnlpJars/jenkins-cli.jar

# Build a job
java -jar jenkins-cli.jar -s http://jenkins_url build job_name

# List all jobs
java -jar jenkins-cli.jar -s http://jenkins_url list-jobs

# Get job info
java -jar jenkins-cli.jar -s http://jenkins_url get-job job_name

# Create job from XML
java -jar jenkins-cli.jar -s http://jenkins_url create-job job_name < job.xml

# Delete a job
java -jar jenkins-cli.jar -s http://jenkins_url delete-job job_name
```

### Pipeline Script Commands
```groovy
// Get current build number
echo "Build Number: ${BUILD_NUMBER}"

// Get workspace path
echo "Workspace: ${WORKSPACE}"

// Get job name
echo "Job Name: ${JOB_NAME}"

// Archive artifacts
archiveArtifacts artifacts: '*.jar', fingerprint: true

// Publish test results
publishTestResults testResultsPattern: 'test-results.xml'

// Send email notification
email to: 'team@company.com', subject: 'Build Status', body: 'Build completed'
```

### Environment Variables
```bash
$BUILD_NUMBER     # Current build number
$BUILD_ID         # Build timestamp
$JOB_NAME         # Name of the job
$WORKSPACE        # Job workspace directory
$JENKINS_URL      # Jenkins server URL
$BUILD_URL        # URL of current build
$GIT_COMMIT       # Git commit hash
$GIT_BRANCH       # Git branch name
```

## Shared Libraries for Beginners

### What are Shared Libraries?
Shared Libraries allow you to create reusable code that can be used across multiple Jenkins pipelines.

### Basic Shared Library Structure
```
shared-library/
├── vars/           # Global variables (pipeline steps)
├── src/            # Groovy classes
└── resources/      # Static resources
```

### Simple Shared Library Examples

#### 1. Hello World Library (`vars/sayHello.groovy`)
```groovy
def call(String name = 'World') {
    echo "Hello, ${name}!"
}
```

#### 2. Build Notification Library (`vars/buildNotification.groovy`)
```groovy
def call(String status, String email) {
    def subject = "Build ${status}: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
    def body = "Build ${status} for job ${env.JOB_NAME}\nBuild Number: ${env.BUILD_NUMBER}\nBuild URL: ${env.BUILD_URL}"
    
    emailext (
        subject: subject,
        body: body,
        to: email
    )
}
```

#### 3. Docker Build Library (`vars/dockerBuild.groovy`)
```groovy
def call(String imageName, String tag = 'latest') {
    sh "docker build -t ${imageName}:${tag} ."
    echo "Docker image built: ${imageName}:${tag}"
}
```

#### 4. Git Checkout Library (`vars/gitCheckout.groovy`)
```groovy
def call(String repo, String branch = 'main') {
    checkout([
        $class: 'GitSCM',
        branches: [[name: "*/${branch}"]],
        userRemoteConfigs: [[url: repo]]
    ])
}
```

### Using Shared Libraries in Pipeline
```groovy
@Library('my-shared-library') _

pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                sayHello('Jenkins')
            }
        }
        stage('Build') {
            steps {
                dockerBuild('myapp', '1.0')
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

## Useful Jenkins Tips & Tricks

### 1. Pipeline Syntax
```groovy
// Parallel execution
parallel {
    stage('Test A') { steps { sh 'test-a.sh' } }
    stage('Test B') { steps { sh 'test-b.sh' } }
}

// Conditional execution
when {
    branch 'main'
    environment name: 'DEPLOY', value: 'true'
}

// Retry on failure
retry(3) {
    sh 'flaky-command.sh'
}

// Timeout
timeout(time: 5, unit: 'MINUTES') {
    sh 'long-running-command.sh'
}
```

### 2. Common Pipeline Patterns
```groovy
// Multi-branch pipeline
pipeline {
    agent any
    stages {
        stage('Build') {
            when { not { branch 'main' } }
            steps { sh 'npm run build' }
        }
        stage('Deploy') {
            when { branch 'main' }
            steps { sh 'npm run deploy' }
        }
    }
}

// Matrix builds
matrix {
    axes {
        axis {
            name 'PLATFORM'
            values 'linux', 'windows', 'mac'
        }
    }
    stages {
        stage('Build') {
            steps {
                echo "Building on ${PLATFORM}"
            }
        }
    }
}
```

### 3. Debugging Tips
```groovy
// Print all environment variables
sh 'printenv | sort'

// Debug pipeline
echo "Debug: Current stage is ${env.STAGE_NAME}"
echo "Debug: Build number is ${env.BUILD_NUMBER}"

// Capture command output
script {
    def result = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
    echo "Git commit: ${result}"
}
```

### 4. Security Best Practices
```groovy
// Use credentials
withCredentials([usernamePassword(credentialsId: 'my-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh 'docker login -u $USER -p $PASS'
}

// Mask sensitive data
wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: 'SECRET', var: 'PASSWORD']]]) {
    sh 'echo $PASSWORD'
}
```

### 5. Performance Optimization
```groovy
// Skip checkout for certain stages
options {
    skipDefaultCheckout()
}

// Parallel stages
parallel {
    'Unit Tests': { sh 'npm test' },
    'Lint': { sh 'npm run lint' },
    'Security Scan': { sh 'npm audit' }
}

// Workspace cleanup
cleanWs()
```

## Quick Reference Commands

### System Administration
```bash
# Restart Jenkins safely
sudo systemctl restart jenkins

# Check Jenkins status
sudo systemctl status jenkins

# View Jenkins logs
sudo journalctl -u jenkins -f

# Jenkins configuration location
/var/lib/jenkins/

# Install plugins via CLI
java -jar jenkins-cli.jar -s http://jenkins_url install-plugin plugin-name
```

### Backup & Restore
```bash
# Backup Jenkins home
tar -czf jenkins-backup.tar.gz /var/lib/jenkins/

# Backup specific job
cp -r /var/lib/jenkins/jobs/job-name/ /backup/location/

# Restore job
cp -r /backup/location/job-name/ /var/lib/jenkins/jobs/
```

These commands and examples provide a solid foundation for working with Jenkins effectively.