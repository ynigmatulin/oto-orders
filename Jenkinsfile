node ('master') {
    stage "checkout"
    git "https://github.com/antweiss/spring-orders.git"
    
    stage 'build'
    sh './gradlew build -x test'
    
  //  stage 'codefresh build'
//    codefresh build: true, cfBranch: 'master', cfService: 'spring-orders', launch: false

    stage 'codefresh launch'
    res = codefresh build: false, cfComposition: 'orders-withmongo', launch: true

    sleep(100)
   if (res)
   {
       log = currentBuild.rawBuild.getLog()
       print log
        pattern = /.*successfully - (http.*)/
@NonCPS
        matcher = ( log =~ pattern)
        print matcher.size() > 0 ? matcher[0] : "NOTHING"
        //print match
   }
   slackSend channel: '#builds', color: '#439FE0',   message: 'codefresh environment' + env.CODEFRESH_ENV_URL + 'is available for testing for build ${env.BUILD_NUMBER}'
    
 
}
