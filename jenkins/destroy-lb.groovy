pipeline {
    agent any

    environment {
    SVC_ACCOUNT_KEY = credentials('jenkins-auth')
  }
     
    stages {
      	stage('Set creds') {
            steps {
              
               sh 'echo $SVC_ACCOUNT_KEY | base64 -d > ./jenkins/jenkins.json'
		sh 'pwd' 
               
            }
        }

	
	stage('Auth-Project') {
	 steps {
		 dir('jenkins')
		 {
    
        sh 'gcloud auth activate-service-account jenkins@gcpkole.iam.gserviceaccount.com --key-file=jenkins.json'
    }
    }
	}
      
     stage('Delete loadbalancer') {
	 steps {
    
    sh 'gcloud compute forwarding-rules delete nginx-lb --region us-central1 --quiet'
        
    }
    }
 	 
      
       stage('Delete MIG') {
	 steps {
    
    sh 'gcloud compute instance-groups managed delete nginx-group --region us-central1 --quiet'
        
    }
    }
      
      stage('Delete targetpool') {
	 steps {
    
    sh 'gcloud compute target-pools delete nginx-pool --region us-central1 --quiet'
        
    }
    }
      
      
	stage('Delete Instance Template') {
	 steps {
    
    sh 'gcloud compute forwarding-rules delete nginx-lb --region us-central1 --quiet'
        
    }
    } 
     
      
     stage('Delete firewall rule') {
	 steps {
    
    sh 'gcloud compute firewall-rules delete allow-fw-http-01 --quiet'
	sh 'gcloud compute firewall-rules delete allow-fw-http --quiet'
        
    }
    }
     
   }
}
