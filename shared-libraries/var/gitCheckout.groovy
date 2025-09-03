def call(String repo, String branch = 'main') {
    checkout([
        $class: 'GitSCM',
        branches: [[name: "*/${branch}"]],
        userRemoteConfigs: [[url: repo]]
    ])
    
    echo "Checked out ${repo} on branch ${branch}"
    
    // Get commit info
    script {
        def commitHash = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
        def commitMsg = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
        echo "Commit: ${commitHash}"
        echo "Message: ${commitMsg}"
    }
}