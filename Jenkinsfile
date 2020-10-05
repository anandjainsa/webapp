@Library('pstsharedlibrary')_

pipeline {

//    agent any
    agent {
        label "maven-agent"
    }
    parameters { 
         choice(name: 'Fortify_Version', choices: ['19.2.0', '4.30'], description: 'Select a Fortify Version')

    } 
    tools {
        jdk "java1.8"
        maven  "maven3"
    }

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.33.11:8081"
        NEXUS_REPOSITORY = "myrepo"
        NEXUS_CREDENTIAL_ID = "nexus-cred"
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
       stage("Publish to Nexus Repository Manager") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
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
