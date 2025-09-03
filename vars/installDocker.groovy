def call() {
    sh '''
        curl -fsSL https://get.docker.com -o get-docker.sh
        sudo sh get-docker.sh
        sudo usermod -aG docker $USER
        newgrp docker << EOF
docker --version
EOF
    '''
    echo "Docker installed successfully"
}