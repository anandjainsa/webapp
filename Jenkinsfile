@Library('pstsharedlibrary')_

pipeline {

    agent any
//    agent {
//        label "nodename"
//    }
    parameters { 
         choice(name: 'Fortify_Version', choices: ['19.2.0', '4.30'], description: 'Select a Fortify Version')

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
                    archiveArtifacts(artifacts: '**/target/*.war', allowEmptyArchive: true)
                }
             }
         }
        stage('Deploy to Tomcat'){
            steps {
                //Deploying Application to tomcat server
                appDeploy("${TOMCAT_IP}","${PORT}","${warPath}");
            }
        }
        stage('Scanning Code - Fortify '){
            steps {
                //Verifying Fortify Scan Results
                fortifyScan("${params.Fortify_Version}");
            }
        }
        stage('Download') {
            steps {
                sh 'mv target/mvn-hello-world.war target/mvn-hello-world.zip'
            }
        }
        //stage('Validating Fortify Scan Results '){
        //    steps {
        //        //Verifying Fortify Scan Results
        //        fortifyScanValidation("${params.Fortify_Version}");
        //    }
       // }
    }
    post {
        always {
             
            emailext attachLog: true, attachmentsPattern: 'target/*.zip',
                body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}",
                recipientProviders: [developers(), requestor()],
                subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}"
        }
    }
}
