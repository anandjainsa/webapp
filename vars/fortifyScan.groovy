def call(FORTIFY_VERSION)
     {
          sh '''
	        mvn clean package -U -Prun-fortify -DskipTests=true -Dfortify.version=${FORTIFY_VERSION} -Dlocation=cloud
 	       '''
}
