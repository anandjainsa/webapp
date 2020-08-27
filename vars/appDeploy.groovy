def call(TOMCAT_IP,PORT,warPath)
{
    sh "curl -u ifind:zs123456 http://${TOMCAT_IP}:${PORT}/manager/text/undeploy?path=${warPath}"
    sh "curl --upload-file target/mvn-hello-world.war  -u ifind:zs123456 http://${TOMCAT_IP}:${PORT}/manager/text/deploy?path=${warPath}"
}
