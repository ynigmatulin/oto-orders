
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
    stage ('deploy'){
         sh 'kubectl apply -f orders-dep.yml --validate=false'
     }

}
