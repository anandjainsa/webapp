@Library('pstsharedlibrary')_

pipeline {

    agent any
//    agent {
//        label "nodename"
//    }
    parameters { 
         string(name: 'tomcat_dev', defaultValue: '35.166.210.154', description: 'Staging Server')
         string(name: 'tomcat_prod', defaultValue: '34.209.233.6', description: 'Production Server')
    } 
    tools {
        jdk "java1.8"
        maven  "maven3"
    }

    environment {
        PORT = "8082"
        TOMCAT_IP = "192.168.33.11"
        warPath="/mvn-hello-world"
//
//        project_name = "crewpro"
//        port = "8090"
//        env = "Dev"
//        hostname =""
    }
    options {
        // Only keep the 10 most recent builds
        buildDiscarder(logRotator(numToKeepStr:'2'))
    }
    stages {
        stage ('Build') {
            steps {
                // build
                mavenBuild();
            }
            post {
               success {
                    // we only worry about archiving the jar file if the build steps are successful
                    archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true)
                }
             }
         }
        stage('Deploy to Tomcat'){
            steps {
                appDeploy("${TOMCAT_IP}","${PORT}","${warPath}");
                  //sshagent(['my-ssh-key']) {
                  //sh 'scp -rv -o StrictHostKeyChecking=no target/*.war vagrant@192.168.33.11:/opt/tomcat/webapps/'
                  //  sh "curl -u ifind:zs123456 http://${TOMCAT_IP}:${PORT}/manager/text/undeploy?path=${warPath}"
                  // sh "curl --upload-file target/mvn-hello-world.war  -u ifind:zs123456 http://${TOMCAT_IP}:${PORT}/manager/text/deploy?path=${warPath}"
            //}
       }
   }
    }
    post {
        always {
            sendNotifications("currentBuild.result")
        }
    }
}
