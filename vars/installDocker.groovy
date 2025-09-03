def call() {
    sh '''
        if ! command -v docker &> /dev/null; then
            echo "Installing Docker..."
            curl -fsSL https://get.docker.com -o get-docker.sh
            sudo sh get-docker.sh
            sudo usermod -aG docker $USER
        else
            echo "Docker already installed"
        fi
        sudo chmod 666 /var/run/docker.sock
        docker --version
    '''
    echo "Docker setup completed"
}