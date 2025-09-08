# How to Setup Shared Libraries in Jenkins Pipeline

## Step 1: Create Shared Library Repository

### Repository Structure
```
my-jenkins-shared-library/
├── vars/
│   ├── sayHello.groovy
│   ├── buildNotification.groovy
│   └── dockerBuild.groovy
├── src/
│   └── com/
│       └── company/
│           └── Utils.groovy
└── resources/
    └── scripts/
        └── deploy.sh
```

## Step 2: Configure in Jenkins

### Global Configuration
1. Go to **Manage Jenkins** → **Configure System**
2. Scroll to **Global Pipeline Libraries**
3. Click **Add** and configure:
   - **Name**: `my-shared-library`
   - **Default version**: `main`
   - **Retrieval method**: Modern SCM
   - **Source Code Management**: Git
   - **Repository URL**: `https://github.com/your-org/jenkins-shared-library.git`
   - **Credentials**: Select appropriate credentials

### Folder-Level Configuration
1. Go to folder → **Configure**
2. Add **Pipeline Libraries**
3. Same configuration as global

## Step 3: Use in Pipeline

### Method 1: Global Library (Implicit Loading)
```groovy
@Library('my-shared-library') _

pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                sayHello('World')
            }
        }
    }
}
```

### Method 2: Specific Version
```groovy
@Library('my-shared-library@v1.0') _

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                dockerBuild('myapp', '1.0')
            }
        }
    }
}
```

### Method 3: Dynamic Loading
```groovy
pipeline {
    agent any
    stages {
        stage('Load Library') {
            steps {
                script {
                    library 'my-shared-library@main'
                    sayHello('Dynamic')
                }
            }
        }
    }
}
```

## Step 4: Create Library Functions

### vars/sayHello.groovy
```groovy
def call(String name = 'World') {
    echo "Hello, ${name}!"
}
```

### vars/buildApp.groovy
```groovy
def call(Map config) {
    echo "Building ${config.name}"
    sh "${config.buildCommand}"
    if (config.runTests) {
        sh "${config.testCommand}"
    }
}
```

### Usage in Pipeline
```groovy
@Library('my-shared-library') _

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                buildApp([
                    name: 'MyApp',
                    buildCommand: 'npm run build',
                    testCommand: 'npm test',
                    runTests: true
                ])
            }
        }
    }
}
```

## Step 5: Advanced Features

### Using src/ Classes
```groovy
// src/com/company/Utils.groovy
package com.company

class Utils {
    static String formatMessage(String msg) {
        return "[INFO] ${msg}"
    }
}
```

### In Pipeline
```groovy
@Library('my-shared-library') _
import com.company.Utils

pipeline {
    agent any
    stages {
        stage('Example') {
            steps {
                script {
                    echo Utils.formatMessage('Build started')
                }
            }
        }
    }
}
```

## Quick Setup Commands

### Create Repository
```bash
mkdir jenkins-shared-library
cd jenkins-shared-library
git init
mkdir -p vars src/com/company resources

# Create basic function
cat > vars/sayHello.groovy << 'EOF'
def call(String name = 'World') {
    echo "Hello, ${name}!"
}
EOF

git add .
git commit -m "Initial shared library"
git remote add origin https://github.com/your-org/jenkins-shared-library.git
git push -u origin main
```

### Test in Pipeline
```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sayHello('Jenkins')
            }
        }
    }
}
```

## Troubleshooting

### Common Issues
1. **Library not found**: Check repository URL and credentials
2. **Function not recognized**: Ensure file is in `vars/` directory
3. **Permission denied**: Verify Jenkins has access to repository
4. **Syntax errors**: Check Groovy syntax in library files

### Debug Library Loading
```groovy
@Library('my-shared-library') _

pipeline {
    agent any
    stages {
        stage('Debug') {
            steps {
                script {
                    echo "Library loaded successfully"
                    // Test your functions here
                }
            }
        }
    }
}
```