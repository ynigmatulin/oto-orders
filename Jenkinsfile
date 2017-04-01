
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
     }
    stage ('component-test'){
	sh "tests/ct/run.sh"
	}


}
