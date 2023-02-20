pipeline {
    agent any

    environment {
    SVC_ACCOUNT_KEY = credentials('dev-auth')
  }
     
    stages {
      	stage('Set creds') {
            steps {
              
               sh 'echo $SVC_ACCOUNT_KEY | base64 -d > jenkins.json'
		sh 'pwd' 
               
            }
        }

	
	stage('Auth-Project') {
	 steps {
		 
    
        sh 'gcloud auth activate-service-account jenkins@mi-de-env.iam.gserviceaccount.com --key-file=jenkins.json'
        sh 'gcloud config set project mi-dev-env'
    }
    }
	
 	 
	stage('Create gke cluster') {
	 steps {
    
    sh 'gcloud container clusters create my-cluster --zone=us-central1-a  --num-nodes=1 --project mi-dev-env'
        
    }
    }
	    
  stage('Gather Credentials') {
	 steps {
    
    sh 'gcloud container clusters get-credentials my-cluster --zone us-central1-a --project mi-dev-env'
        
    }
    }
      
      stage('Deploy POD') {
	 steps {
    
    sh 'kubectl apply -f ./k8s/manual-deploy.yaml'
        
    }
    }
      
     stage('Test Application') {
	 steps {
    
    sh 'sleep 60'
    sh 'curl http://34.171.69.164/hello'
        
    }
    }
     
     
   }
}
