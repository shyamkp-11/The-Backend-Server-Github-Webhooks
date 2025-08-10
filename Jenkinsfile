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
                    env.S3_WAR_PATH = props.S3_WAR_PATH
                    env.DEPLOY_LOCALLY = false
                    env.GITHUB_TOKEN = props.GITHUB_PAT
                }
            }
        }
        stage('Docker') {
            when {
                expression {
                    return false
                }
            }
            steps {
                sh '''
docker build --tag shyamkp4/webapp-github-playroom .
mkdir -p .m2
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
        stage('Build project') {
            when {
                expression {
                    return true
                }
            }
            agent {
                dockerfile {
                    dir 'docker'
					reuseNode true
				}
            }
            steps {
                withCredentials([file(credentialsId: 'GITHUB_PLAYROOM_FIREBASE_SDK_JSON', variable: 'firebaseSdkJson')]) {
                    sh '''
cp -f \$firebaseSdkJson src/main/resources/github-playroom-firebase-adminsdk.json
                    '''
                }
                sh '''
mvn -Dmaven.repo.local=.m2/repository clean install --batch-mode -DskipTests
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
                dockerfile {
                    dir 'docker'
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
                    return env.DEPLOY_LOCALLY.toBoolean() == true;
                }
            }
            steps {
                echo "Building docker image"
                sh '''
ls -lrt
docker image build -t deployed_webapp_github_playroom:$BUILD_NUMBER .
docker images
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
        stage('Copy MyProfileApp Artifacts') {
        when {
            expression {
                return false
            }
        }
            steps {
                copyArtifacts(projectName: 'MyProfileApp',
                target: 'MyProfileApp',
                 filter: 'target/*.jar',);
                 sh '''
file_name=$(find MyProfileApp/target -type f -name "*jar")
aws s3 cp $file_name $S3_WAR_PATH/ --quiet
                 '''
            }
        }
        stage('Deploy') {
            when {
                 beforeAgent true;
                 expression {
                     return env.DEPLOY_LOCALLY.toBoolean() == false;
                 }
            }
            steps {
//              todo change hard coded name
                sh '''
#copy JAR file to s3
file_name=$(find target -type f -name "*jar")
aws s3 cp $file_name $S3_WAR_PATH/ --quiet
cat >entrypoint.sh <<EOL
#install awscli
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o \
"awscliv2.zip" && unzip awscliv2.zip && ./aws/install && rm awscliv2.zip
printf "$AWS_ACCESS_KEY_ID_ENV\n$AWS_ACCESS_KEY_ENV\n$AWS_DEFAULT_REGION_ENV\njson\n" | aws configure
# run jar
aws s3 cp $S3_WAR_PATH/githubplayroom.jar /home/ --quiet
#aws s3 cp $S3_WAR_PATH/MyProfileApp.jar /home/ --quiet
java -jar /home/githubplayroom.jar
EOL
mkdir -p .ebextensions
cat >.ebextensions/environment.config <<EOL
option_settings:
    - namespace: aws:autoscaling:launchconfiguration
      option_name: SecurityGroups
      value: sg-074f3dd3d21cab7d9
    - namespace: aws:elbv2:loadbalancer
      option_name: SecurityGroups
      value: sg-074f3dd3d21cab7d9
    - namespace: aws:elbv2:loadbalancer
      option_name: ManagedSecurityGroup
      value: sg-074f3dd3d21cab7d9
    - option_name: ADMIN_PASSWORD
      value: $ADMIN_PASSWORD
    - option_name: ADMIN_USERNAME
      value: $ADMIN_USERNAME
    - option_name: SECRET_KEY
      value: $SECRET_KEY
    - option_name: spring.datasource.password
      value: $WEBAPP_DATASOURCE_PASSWORD
    - option_name: spring.datasource.username
      value: $WEBAPP_DATASOURCE_USERNAME
    - option_name: spring.datasource.url
      value: $WEBAPP_DATASOURCE_URL
    - option_name: application.github.hmac_key
      value: $GITHUB_SERVER_HMAC_KEY
    - option_name: application.github.token
      value: $GITHUB_SERVER_AUTH_TOKEN
    - option_name: SERVER_PORT
      value: 8080
    - option_name: S3_WAR_PATH
      value: $S3_WAR_PATH
EOL

# Because if not target directory won't be uploaded to eb instance's docker image
cat >.ebignore <<EOL
target/
src/
MyProfileApp/
.git/
EOL
eb init -p docker -r us-east-2 -k DeployEC2KeyPair GithubPlayroomEBS
if eb status | grep -q " Application name: GithubPlayroomEBS"; then
eb deploy GithubPlayroomEBS -l $BUILD_NUMBER
fi
#else
#eb create GithubPlayroomEBS --vpc.publicip --vpc.elbpublic --instance_profile iam-ebs-role --instance-types t2.micro --enable-spot  --vpc.id vpc-011aed36112c9889e --vpc.ec2subnets subnet-0a83820bfcd09082e --vpc.elbsubnets subnet-08f62229703fb2168,subnet-0a83820bfcd09082e --vpc.securitygroups sg-074f3dd3d21cab7d9
#fi
'''
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