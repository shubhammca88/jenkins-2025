def call() {
    sh '''
        docker --version
        sudo docker info
        echo "Docker verification completed"
    '''
}