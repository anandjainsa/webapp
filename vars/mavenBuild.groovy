def call()
{
    sh "mvn clean package -DskipTests=true"
}
