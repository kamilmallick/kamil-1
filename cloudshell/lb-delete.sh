#Make sure below things are set
#Change script file permission Eg: chmod u+x <name-of-script-file>.sh
#./script.sh 
#Clean the lab
#remove load balancer

RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Deletion process started***************** ${NC}"
gcloud compute forwarding-rules delete nginx-lb --region us-central1 --quiet
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Load balanacer deleted***************** ${NC}"
#delete MIG

gcloud compute instance-groups managed delete nginx-group --region us-central1 --quiet
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************MIG deleted***************** ${NC}"

#Delete target pool
gcloud compute target-pools delete nginx-pool --region us-central1 --quiet
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Target pool deleted***************** ${NC}"


#Delete Instance template
gcloud compute instance-templates delete nginx-template --quiet
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Instance template deleted***************** ${NC}"

#delete firewall rules
gcloud compute firewall-rules delete allow-fw-http-01 --quiet
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Firewall rules deleted***************** ${NC}"
RED='\033[0;32m'
NC='\033[0m' # No Color
echo -e "${RED}**************Infra deleted successfully***************** ${NC}"
