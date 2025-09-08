
# Groovy in Jenkins Pipelines

**Groovy** is a dynamic, object-oriented programming language used in Jenkins pipelines. It is used to define the steps and logic within Jenkins jobs, including Declarative Pipelines. Groovy is based on Java but provides more concise syntax and flexibility for scripting.

## Basic Groovy Syntax

1. **Variables:**
   You can define variables using the `def` keyword.

   ```groovy
   def name = "Jenkins"
   echo "Hello, $name"
   ```

2. **Conditions:**
   Groovy uses `if-else` for conditional logic.

   ```groovy
   def num = 10
   if (num > 5) {
       echo "Number is greater than 5"
   } else {
       echo "Number is less than or equal to 5"
   }
   ```

3. **Loops:**
   Groovy supports loops like `for`, `while`, and `each`.

   ```groovy
   def numbers = [1, 2, 3, 4]
   numbers.each { number ->
       echo "Number: $number"
   }
   ```

4. **Functions:**
   You can define reusable functions in Groovy.

   ```groovy
   def greet(name) {
       return "Hello, $name"
   }
   echo greet("Jenkins")
   ```

5. **Lists and Maps:**
   Groovy supports lists and maps (key-value pairs).

   ```groovy
   def list = [1, 2, 3]
   def map = [name: 'Jenkins', type: 'CI']

   echo list[0]  // Output: 1
   echo map['name']  // Output: Jenkins
   ```

6. **Closures:**
   A closure is a block of code that can be passed as an argument or assigned to a variable.

   ```groovy
   def closure = { name -> echo "Hello, $name" }
   closure("Groovy")
   ```

### Example in Jenkins Pipeline (Groovy-based)

```groovy
pipeline {
    agent any

    environment {
        GREETING = "Welcome to Jenkins!"
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Groovy code within the pipeline
                    echo "Building the project..."
                    def message = "Project build is successful"
                    echo message
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    def tests = ['Test1', 'Test2', 'Test3']
                    tests.each { test ->
                        echo "Running $test"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying the project..."
            }
        }
    }

    post {
        success {
            echo "Pipeline executed successfully"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}
```

### Key Groovy Features for Jenkins

- **Dynamic Typing**: Variables don't require explicit types.
- **Script Blocks**: You can use Groovy script blocks to execute custom logic.
- **String Interpolation**: Groovy supports string interpolation, allowing you to easily insert variables into strings.

Groovy allows for flexibility and simplicity when writing Jenkins pipelines and automating tasks in CI/CD processes.
