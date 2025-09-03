def call() {
    sh '''
        curl -fsSL https://get.docker.com -o get-docker.sh
        sudo sh get-docker.sh
        sudo usermod -aG docker $USER
        sudo chmod 666 /var/run/docker.sock
        docker --version
    '''
    echo "Docker installed successfully"
}