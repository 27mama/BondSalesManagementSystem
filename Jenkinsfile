node{
    stage('Down code') {
       git 'http://localhost:3000/root/m71-springboot-tomcat.git'
    }
    stage('deploy') {
       bat(/mvn install /)
    }    
    stage('run sonar') {
        withSonarQubeEnv('Sonar-Server') {
            bat(/mvn org.jacoco:jacoco-maven-plugin:prepare-agent sonar:sonar /)
        }
        script {
            timeout(1) { 
                def qg = waitForQualityGate() 
                if (qg.status != 'OK') {
                    error "未通过Sonarqube的代码质量阈检查，请及时修改！failure: ${qg.status}"
                }
            }
        }
    }
}
