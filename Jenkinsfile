pipeline {
    agent none
    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.6.1-jdk-8-slim'
                }
            }
            steps {
                script {
                    sh 'mvn clean package -B -ntp -DskipTests'
                }
            }
        }
        
        
        stage('Sonar') {
            steps {
		withCredentials( [[ $class: 'FileBinding', credentialsId : 'settings-sonarqube', variable: 'M2_SETTINGS']] ){
		sh: "mvn sonar:sonar -ntp -s ${M2_SETTINGS}"	}
               
            }
        }
        
        stage('Approval') {
            steps {
                script {

                    def ambiente = "ambiente"
                    def users = "eddie.zorrilla"

                    timeout(time: 15, unit: 'MINUTES') {
                        userInput = input(
                            submitterParameter: 'approval',
                            submitter: "${users}",
                            message: "¿Desplegar en Servidor de ${ambiente}?", parameters: [
                            [$class: 'BooleanParameterDefinition', defaultValue: true, description: '', name: 'Aprobar']
                        ])
                    }
                }
            }
        }
        stage('registryDocker') {
            agent any
            steps {
                script {

                    sh 'whoami'

                    echo "${userInput}"

                    if(userInput.Aprobar == true){


                        def pom = readMavenPom file : 'pom.xml'


                        def app = docker.build("eddiez/${pom.artifactId}:${pom.version}")

                        docker.withRegistry('https://registry.hub.docker.com/', 'docker-hub-credentials'){
                            app.push()
                            app.push('latest')
                        }
                    }


                }
            }
        }
    }
}