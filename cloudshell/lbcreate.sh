#Make sure below things are set
#Change script file permission Eg: chmod u+x <name-of-script-file>.sh
#./script.sh
#http load balancer
#Create startup script
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Get started***************** ${NC}"

cat << EOF > startup.sh
#! /bin/bash
apt-get update
apt-get install -y nginx
service nginx start
sed -i -- 's/nginx/Google Cloud Platform - '"\$HOSTNAME"'/' /var/www/html/index.nginx-debian.html
EOF

#create instance template with startup script

gcloud compute instance-templates create nginx-template --metadata-from-file startup-script=startup.sh
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Instance template creation completed***************** ${NC}"

#Create a target pool to hit single ip

gcloud compute target-pools create nginx-pool --region us-central1


#Create MIG and add instances to targetpool

gcloud compute instance-groups managed create nginx-group --base-instance-name nginx --size 2 --template nginx-template --target-pool nginx-pool --region us-central1
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************MIG creation completed***************** ${NC}"
		 
#open http fw
gcloud compute firewall-rules create allow-fw-http-01 --allow tcp:80

#Create http load balancer
gcloud compute forwarding-rules create nginx-lb --region us-central1 --ports=80 --target-pool nginx-pool		 
gcloud compute forwarding-rules list
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Congratulation infra build completed successfully***************** ${NC}"
