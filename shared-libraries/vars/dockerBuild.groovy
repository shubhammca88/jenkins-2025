def call(String imageName, String tag = 'latest') {
    sh "docker build -t ${imageName}:${tag} ."
    echo "Docker image built: ${imageName}:${tag}"
    
    // Optional: Push to registry
    if (env.DOCKER_REGISTRY) {
        sh "docker tag ${imageName}:${tag} ${env.DOCKER_REGISTRY}/${imageName}:${tag}"
        sh "docker push ${env.DOCKER_REGISTRY}/${imageName}:${tag}"
        echo "Image pushed to registry: ${env.DOCKER_REGISTRY}/${imageName}:${tag}"
    }
}