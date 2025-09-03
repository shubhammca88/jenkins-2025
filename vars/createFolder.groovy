def call(String folderName = 'pipeline_folder') {
    sh """
        if [ ! -d "${folderName}" ]; then
            mkdir -p ${folderName}
            echo "Folder created: \$(pwd)/${folderName}"
        else
            echo "Folder already exists: \$(pwd)/${folderName}"
        fi
    """
}