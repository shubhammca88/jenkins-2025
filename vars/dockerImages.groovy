def call() {
    sh '''
        sg docker -c "docker images"
    '''
}