pipeline {
    agent any
    environment {
        WEBAPP_CREDENTIALS=credentials('WEBAPP_SECRETS');
    }
    stages {
        stage ('Init') {
            steps {
                script {
                    def props = readProperties file: env.WEBAPP_CREDENTIALS
                    env.ADMIN_USERNAME = props.ADMIN_USERNAME
                    env.ADMIN_PASSWORD = props.ADMIN_PASSWORD
                    env.SECRET_KEY = props.SECRET_KEY
                    env.WEBAPP_DATASOURCE_URL = props.WEBAPP_DATASOURCE_URL
                    env.WEBAPP_DATASOURCE_USERNAME = props.WEBAPP_DATASOURCE_USERNAME
                    env.WEBAPP_DATASOURCE_PASSWORD = props.WEBAPP_DATASOURCE_PASSWORD
                    env.GITHUB_SERVER_BASE_URL = props.GITHUB_SERVER_BASE_URL
                    env.GITHUB_SERVER_AUTH_TOKEN = props.GITHUB_SERVER_AUTH_TOKEN
                    env.GITHUB_SERVER_HMAC_KEY = props.GITHUB_SERVER_HMAC_KEY
                    env.AWS_ACCESS_KEY_ID_ENV = props.AWS_ACCESS_KEY_ID_ENV
                    env.AWS_ACCESS_KEY_ENV = props.AWS_ACCESS_KEY_ENV
                    env.AWS_DEFAULT_REGION_ENV = props.AWS_DEFAULT_REGION_ENV
                    env.S3_PROPERTIES_PATH = props.S3_PROPERTIES_PATH
                    env.DEPLOY_LOCALLY = false
                    env.GITHUB_TOKEN = props.GITHUB_PAT
                    env.DOCKER_PAT = props.DOCKER_PAT
                    env.DOCKER_USERNAME = props.DOCKER_USERNAME
                    env.SERVER_PORT = 8082
                }
            }
        }
        stage('Docker') {
            when {
                expression {
                    return true
                }
            }
            steps {
                sh '''
docker build --tag shyamkp4/upload-github-release -f docker/Dockerfile .
#docker build --tag shyamkp4/webapp-github-playroom .
#mkdir -p .m2
                '''
            }
        }
        stage('Test') {
            when {
                beforeAgent true;
                expression {
                    return false
                }
            }
            agent {
                docker {
                    image 'maven:3.9.9-amazoncorretto-21'
                    reuseNode true
                    }
            }
            steps {
                echo "Running tests"
                sh "mvn test"
            }
        }
        stage('Upload Properties files') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'GITHUB_PLAYROOM_FIREBASE_SDK_JSON', variable: 'firebaseSdkJson')]) {
                        sh '''
# cp -f \$firebaseSdkJson src/main/resources/github-playroom-firebase-adminsdk.json
#If jenkins says unable to locate credentials run aws configure in the container.
aws s3 cp $firebaseSdkJson $S3_PROPERTIES_PATH/github-playroom-firebase-adminsdk.json'''
                        }
                    withCredentials([file(credentialsId: 'WEBAPP_SECRETS', variable: 'webappSecrets')]) {
                    sh '''
aws s3 cp $webappSecrets $S3_PROPERTIES_PATH/webapp-secrets.properties
'''
                        }
                    }
                }
            }
        stage('Build project') {
            when {
                expression {
                    return true
                }
            }
            agent {
                docker {
                    image 'shyamkp4/upload-github-release'
					//args '-e GITHUB_TOKEN=${env.GITHUB_TOKEN}'
					reuseNode true
				}
            }
            steps {
                sh '''
mvn -Dmaven.repo.local=.m2/repository clean install --batch-mode -DskipTests -q
                '''
//                 withCredentials([
//                       file(credentialsId: 'maven-settings', variable: 'MAVEN_SETTINGS')
//                     ]) {
//                       sh 'mvn -s $MAVEN_SETTINGS -B -DskipTests clean package'
//                     }
            }
        }

        stage('Upload Github release') {
            agent {
                docker {
                    image 'shyamkp4/upload-github-release'
					//args '-e GITHUB_TOKEN=${env.GITHUB_TOKEN}'
					reuseNode true
				}
            }
			when {
                beforeAgent true;
				expression {
                    def scriptOutput = sh(returnStdout: true, script: '''
                    #!/bin/bash
                    commit1=$(git rev-list -1 $(git describe --tags --abbrev=0));
                    commit2=$(git rev-parse HEAD);
                    if [ "$commit1" = "$commit2" ]; then
                        echo "true"
                    else
                        echo "false"
                    fi''').trim()
                    echo "$scriptOutput"
                    return scriptOutput == "true"
                }
            }
            steps {
                script {
                    def tagName = sh(returnStdout: true, script:'git describe --tags --abbrev=0').trim()
                    def commitish = sh(returnStdout: true, script:'git rev-parse HEAD').trim()
                    //def fileName = sh(returnStdout: true, script:'$(find target -type f -name "*jar")').trim()
                    sh """
# Check if the release already exists
if gh release view "${tagName}" &>/dev/null; then
    echo "Release ${tagName} already exists. Skipping creation."
else
    # Create the release if it does not exist
    echo "Creating release ${tagName}..."
    gh release create ${tagName} target/githubplayroom.jar
fi
"""
                }
            }
        }
        stage ('Build dockerImage') {
            when {
                beforeAgent true;
                expression {
                    return true;
                }
            }
            steps {
                echo "Building docker image"
                sh '''
ls -lrt
cat >entrypoint.sh <<EOL
java $JAVA_OPTS -jar githubplayroom.jar
EOL
docker image build -t shyamkp4/deployed_webapp_github_playroom:$BUILD_NUMBER -t shyamkp4/deployed_webapp_github_playroom:latest .
docker images
                '''
            }
        }

    stage('Upload Docker image'){
        when {
            beforeAgent true;
            expression {
             return true;
            }
        }
        steps {
            echo "Uploading docker image"

            sh '''
echo "$DOCKER_PAT" | docker login --username $DOCKER_USERNAME --password-stdin
docker push shyamkp4/deployed_webapp_github_playroom --all-tags
'''
            }
        }

        stage('Deploy locally') {
            when {
                beforeAgent true;
                expression {
                    return env.DEPLOY_LOCALLY.toBoolean() == true;
                }
            }
            steps {
                echo "Running locally"
                script {
                def inspectExitCode = sh script: "docker container inspect deployed_webapp_github_playroom", returnStatus: true
                if (inspectExitCode == 0) {
                    // remove container if exist
                    sh "docker stop deployed_webapp_github_playroom"
                    sh "docker rm deployed_webapp_github_playroom"
                    }
                }
                sh "docker run -d -p 0.0.0.0:8081:8080 \
--name deployed_webapp_github_playroom \
-e ADMIN_PASSWORD='$ADMIN_PASSWORD' \
-e ADMIN_USERNAME='$ADMIN_USERNAME' \
-e SECRET_KEY='$SECRET_KEY' \
-e spring.datasource.password='$WEBAPP_DATASOURCE_PASSWORD' \
-e spring.datasource.username='$WEBAPP_DATASOURCE_USERNAME' \
-e spring.datasource.url='$WEBAPP_DATASOURCE_URL' \
-e SERVER_PORT='8080' \
deployed_webapp_github_playroom:$BUILD_NUMBER"
            }
        }
    }
    post {
        success {
            sh '''
echo post on Success
'''
            // archiveArtifacts artifacts: 'build/**'
        }
        // always {
        // }
    }
}