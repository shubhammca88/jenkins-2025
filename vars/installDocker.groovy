def call() {
    sh '''
        set -e
        
        echo "Starting Docker installation..."
        
        # Update system
        sudo apt update -y
        
        # Install prerequisites
        sudo apt install -y ca-certificates curl gnupg lsb-release
        
        # Add Docker GPG key
        sudo mkdir -p /etc/apt/keyrings
        curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
        
        # Add Docker repository
        echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
        
        # Install Docker
        sudo apt update -y
        sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
        
        # Start Docker service
        sudo systemctl enable docker
        sudo systemctl start docker
        
        # Add current user to docker group
        sudo usermod -aG docker $USER

        # active docker user 
        newgrp docker
        
        # Verify installation
        docker --version
        
    '''
    echo 'Docker install complete'
}