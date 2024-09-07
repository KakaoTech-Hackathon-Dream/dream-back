#!/bin/bash

cleanUpImages(){
  docker rmi $(docker images -f "dangling=true" -q)
  sudo docker system prune -af
}

#cd /home/ubuntu/deploy-back/
#docker-compose up --build -d

cd /home/ubuntu/deploy-nginx/
docker-compose up --build -d

cleanUpImages


