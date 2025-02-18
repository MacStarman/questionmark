# COMP30220 - Distributed Systems Group Project

## Description

COMP30220 Group Porject repository for Team name : QuestionMark

## QuestionMark (?)

  | Name              | Email                           | Student No   |
  | ----------------- | :-----------------------------: | -----------: |
  | Matthew Kennedy   | matthew.kennedy2@ucdconnect.ie  | 19512406     |
  | Daniel Mc Carthy  | daniel.mccarthy1@ucdconnect.ie  | 19350093     |
  | Rory Browne       | rory.browne@ucdconnect.ie       | 19419774     |
  | Ishika Tewatia    | ishika.tewatia@ucdconnect.ie    | 20203568     |

Can be ran using docker-compose or kubernetes-

Docker-Compose:

    - cd project
    - mvn clean install
    - docker-compose up

K8s:

    - cd project
    - mvn clean install
    - docker build -t account ./account
    - docker build -t balance ./balance
    - docker build -t transaction ./transaction
    - minikube start
    - minikube image load account
    - minikube image load balance
    - minikube image load transaction
    - cd k8s
    - kubectl apply -f mongodb-deployment.yaml
    - kubectl apply -f mongodb-service.yaml
    - kubectl apply -f account-deployment.yaml
    - kubectl apply -f account-service.yaml
    - kubectl apply -f balance-deployment.yaml
    - kubectl apply -f balance-service.yaml
    - kubectl apply -f transaction-deployment.yaml
    - kubectl apply -f transaction-service.yaml
