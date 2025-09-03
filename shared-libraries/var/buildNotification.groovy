def call(String status, String email) {
    def subject = "Build ${status}: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
    def body = """
Build ${status} for job ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Build URL: ${env.BUILD_URL}
Git Commit: ${env.GIT_COMMIT ?: 'N/A'}
"""
    
    emailext (
        subject: subject,
        body: body,
        to: email
    )
    
    echo "Notification sent to ${email} - Status: ${status}"
}