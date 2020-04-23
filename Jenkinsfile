@Library('pstsharedlibrary')_

pipeline {

    agent any
//    agent {
//        label "nodename"
//    }
    tools {
        jdk "java1.8"
        maven  "maven3"
    }

//    environment {
//
//        project_name = "crewpro"
//        port = "8090"
//        env = "Dev"
//        hostname =""
//    }
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
                  sshagent(['my-ssh-key']) {
                  sh 'scp -rv -o StrictHostKeyChecking=no target/*.war vagrant@192.168.33.11:/opt/tomcat/webapps/'
            }
       }
   }
    }
    post {
        always {
            sendNotifications("currentBuild.result")
        }
    }
}
