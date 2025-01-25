
# Jenkins Terms and Commands

Here are some common Jenkins terms and commands explained briefly:

1. **Jenkins**: An open-source automation server used for Continuous Integration/Continuous Deployment (CI/CD).

2. **Pipeline**: A series of automated steps that define the stages of your build, test, and deployment process.

3. **Job**: A single task or process defined in Jenkins. A job can run tests, compile code, or deploy applications.

4. **Build**: The process of compiling or assembling your code and running tests. It can be triggered manually or automatically.

5. **Freestyle Project**: A type of Jenkins job that allows for simple, configurable automation tasks.

6. **Declarative Pipeline**: A type of Jenkins pipeline that uses a predefined structure, making it easier to define stages and steps.

7. **Declarative Syntax Example**:
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

These terms and commands form the core components of Jenkins.