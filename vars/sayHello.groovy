def call(String name = 'World') {
    echo "Hello, ${name}!"
    echo "Current time: ${new Date()}"
    echo "Build number: ${env.BUILD_NUMBER ?: 'N/A'}"
}