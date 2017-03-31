node ('slave1'){
    stage ('git'){
       git 'https://github.com/antweiss/oto-orders.git'
    }
    stage ('build'){
     def gr = tool 'gradle'
     sh "${gr}/bin/gradle build -x test"
    }
    
    stage ('dockerize'){
        docker.build "otomato/oto-orders:$params.BUILD_NUMBER"
    }
}
