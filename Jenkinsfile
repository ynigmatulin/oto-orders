
node ('slave1'){
//define app url for component tests
    def APP_URL=""
    def svcName = 'orders'
    def nsName = "${svcName}-testing-${env.BUILD_NUMBER}"
//clean out build dir
    dir ('build')
    {
        deleteDir()
    }
    stage ('git'){
       checkout scm
    }
    stage ('build+ut'){
    	//this runs unit test (assumes there's a mongo running at 'mongo'
	def gr = tool 'gradle'
	sh "${gr}/bin/gradle build"
        junit 'build/test-results/test/*.xml'
   }
   def image = ''
   stage ('dockerize'){
       image = docker.build "otomato/oto-${svcName}:${env.BUILD_NUMBER}"
   }
    
    
    stage ('push'){
        image.push()
    }
    stage ('deploy-to-testing'){
          sh "sed -i -- \'s/BUILD_NUMBER/${env.BUILD_NUMBER}/g\' ${svcName}-dep.yml"
		sh "kubectl create namespace ${nsName}"
        sh "kubectl apply -f mongodep.yml --validate=false -n ${nsName}"
        sh "kubectl apply -f ${svcName}-dep.yml --validate=false -n ${nsName}"
        //get app url
        APP_URL = "<pending>"
        sleep 120
        while ( APP_URL == "<pending>"){
            APP_URL = sh returnStdout: true, script: "kubectl get svc ${svcName} --no-headers=true  -n ${nsName} |  awk '{print \$3}'"
             APP_URL = APP_URL.trim()
            
        }
       
        echo "url is ${APP_URL}"
     }
    stage ('component-test'){
       withEnv(["APP_URL=${APP_URL}:8080"]) {
	sh "tests/ct/run.sh"
       }
    }
    stage ('clean-up'){
	sh "kubectl delete ns ${nsName}"
    }
    stage('deply-to-staging'){
        sh "kubectl replace -f ${svcName}-dep.yml -n staging"  
    }
    stage ('integration-test'){
        echo "Not implemented"
    }
}
