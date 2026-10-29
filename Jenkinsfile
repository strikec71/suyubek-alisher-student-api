pipeline {
    agent any
    options { skipDefaultCheckout(true); disableConcurrentBuilds(); timeout(time: 20, unit: 'MINUTES') }
    triggers { pollSCM('* * * * *') }
    environment {
        IMAGE_NAME = 'suyubek-alisher-student-api:v1.0-suyubek-alisher-37773'
        GRADLE_USER_HOME = "${WORKSPACE}/.gradle-ci"
    }
    stages {
        stage('Checkout') { steps { checkout scm; sh 'echo "Student: Suyubek Alisher | IT2-2306 | 37773"; git rev-parse HEAD; java -version; javac -version' } }
        stage('Build') { steps { sh './gradlew --no-daemon clean classes' } }
        stage('Test') {
            steps {
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
        stage('Docker Build') { steps { sh 'docker build --label "org.opencontainers.image.revision=$(git rev-parse HEAD)" -t "$IMAGE_NAME" .' } }
        stage('Verify') {
            steps {
                sh 'docker image inspect "$IMAGE_NAME" > verified-image.json'
                sh '''python3 - <<'PY'
import json, subprocess
image = json.load(open('verified-image.json'))[0]
revision = subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip()
assert image['Config']['Labels']['org.opencontainers.image.revision'] == revision
print('Verified image:',image['Id'])
print('Size bytes:',image['Size'],'Created:',image['Created'])
print('Source revision:',revision)
PY'''
            }
        }
    }
    post { success { echo 'Suyubek Alisher Student API verification SUCCESS' } }
}
