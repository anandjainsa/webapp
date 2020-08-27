def call(TOMCAT_IP, PORT, warPath)
{
    sh '''
    curl -u ifind:zs123456 http://${TOMCAT_IP}:${PORT}/manager/text/undeploy?path=${warPath}
    curl --upload-file target/mvn-hello-world.war  -u ifind:zs123456 http://${TOMCAT_IP}:${PORT}/manager/text/deploy?path=${warPath}
    echo "Anand Jain ${TOMCAT_IP}:${PORT}"
    '''
}
