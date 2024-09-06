#!/bin/bash

serviceDown(){
  if [ $(docker ps -a -q -f name=$CONTAINER_NAME) ]
  then
      echo "컨테이너 $CONTAINER_NAME 종료 및 삭제 중..."

      IMAGE_ID=$(docker images -q $CONTAINER_NAME)

      if [ "$IMAGE_ID" ]; then
          echo "이미지 $CONTAINER_NAME 삭제 중..."

      docker rmi -f $IMAGE_ID
      fi
  fi
}

reloadNginx(){
  docker exec -it nginx nginx -s reload
}

cleanUpImages(){
  docker rmi $(docker images -f "dangling=true" -q)
  sudo docker system prune -af
}

cd /home/ubuntu/deploy-back/
docker-compose up --build -d

cleanUpImages


