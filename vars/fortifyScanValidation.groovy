def call(FORTIFY_VERSION)
     {
          sh '''
		LOW=`/opt/software/Fortify_SCA_and_Apps_${FORTIFY_VERSION}/bin/FPRUtility -information -errors -project *.fpr -search -query "[fortify priority order]:low"| awk '{print $1}'`
    MEDIUM=`/opt/software/Fortify_SCA_and_Apps_${FORTIFY_VERSION}/bin/FPRUtility -information -errors -project *.fpr -search -query "[fortify priority order]:medium"`
    HIGH=`/opt/software/Fortify_SCA_and_Apps_${FORTIFY_VERSION}/bin/FPRUtility -information -errors -project *.fpr -search -query "[fortify priority order]:high"`
    CRITICAL=`/opt/software/Fortify_SCA_and_Apps_${FORTIFY_VERSION}/bin/FPRUtility -information -errors -project *.fpr -search -query "[fortify priority order]:critical"`
    CRITICAL_COUNT=`echo $CRITICAL | awk '{print $1}' | grep  "No" | wc -l`
    HIGH_COUNT=`echo $HIGH | awk '{print $1}' | grep  "No" | wc -l`

    if [ "$CRITICAL_COUNT" -eq "1" ]
      then
        CRITICAL=$CRITICAL_COUNT
    fi

    if [ "$HIGH_COUNT" -eq "1" ]
      then
        HIGH=$HIGH_COUNT
    fi

 if [ "$CRITICAL" -gt 20 ]
   then
        echo "Couldn't Deploy to QA as code has $CRITICAL Critical issues"
        exit 0;
elif [ "$HIGH" -gt 50 ]
   then
        echo "Couldn't Deploy to QA as code has $HIGH High issues"
        exit 0;
else
     echo "Deploying to QA"
fi

 	'''
}

