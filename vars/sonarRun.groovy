def call()
{
        sh "mvn clean jacoco:prepare-agent verify jacoco:report -Dsonar.host.url=http://ttgssdv0vrhmt01.ttgtpmg.net:9000 org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar"
}
