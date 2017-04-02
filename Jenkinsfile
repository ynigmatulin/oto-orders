
node ('slave1'){
    stage ('git'){
       checkout scm
    }
    stage ('build'){
    def gr = tool 'gradle'
    sh "${gr}/bin/gradle build -x test"
   }
   def image = ''
   stage ('dockerize'){
       image = docker.build "otomato/oto-orders:${env.BUILD_NUMBER}"
   }
    
    
    stage ('push'){
        image.push()
    }
    stage ('deploy-to-testing'){
          sh "sed -i -- \'s/BUILD_NUMBER/${env.BUILD_NUMBER}/g\' orders-dep.yml"
		sh "kubectl create namespace orders-testing-${env.BUILD_NUMBER}"
        sh "kubectl apply -f mongodep.yml --validate=false --namespace=orders-testing-${env.BUILD_NUMBER}"
        sh "kubectl apply -f orders-dep.yml --validate=false --namespace=orders-testing-${env.BUILD_NUMBER}"
        //get app url
        def APP_URL = "<pending>"
        while ( APP_URL == "<pending>"){
            APP_URL = sh returnStdout: true, script: "kubectl get svc otoorders --no-headers=true  --namespace=orders-testing-${env.BUILD_NUMBER} |  awk '{print \$3}'"
        }
        echo APP_URL
     }
    stage ('component-test'){
       withEnv(["APP_URL=${APP_URL}"]) {
	sh "tests/ct/run.sh"
       }
    } 


}
