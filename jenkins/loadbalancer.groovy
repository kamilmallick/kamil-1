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
 	 
	stage('Create Instance Template') {
	 steps {
    
    sh 'gcloud compute instance-templates create nginx-template --metadata-from-file startup-script=script/startup.sh'
        
    }
    }
	    
  stage('Create targetpool') {
	 steps {
    
    sh 'gcloud compute target-pools create nginx-pool --region us-central1'
        
    }
    }
      
      stage('Create MIG') {
	 steps {
    
    sh 'gcloud compute instance-groups managed create nginx-group --base-instance-name nginx --size 2 --template nginx-template --target-pool nginx-pool --region us-central1'
        
    }
    }
      
     stage('Create firewall rule') {
	 steps {
    
    sh 'gcloud compute firewall-rules create allow-fw-http-01 --allow tcp:80'
        
    }
    }
      
      stage('Create loadbalancer') {
	 steps {
    
    sh 'gcloud compute forwarding-rules create nginx-lb --region us-central1 --ports=80 --target-pool nginx-pool'
        
    }
    }
	    
	stage('List external IP address') {
	 steps {
    
    sh 'gcloud compute forwarding-rules list'
        
    }
    }
     
   }
}
