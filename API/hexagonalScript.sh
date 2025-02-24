#!/bin/bash
# Author: JonayKB (DONT CHANGE THIS)
if [ "$#" -lt 1 ]; then
  echo "Use: $0 <class_name1> [<class_name2> ...]"
  exit 1
fi

# Shared

if [ ! -d "shared" ]; then
  mkdir -p shared/config
  mkdir -p shared/controllers
  mkdir -p shared/dto
  mkdir -p shared/security
  mkdir -p shared/tasks
  mkdir -p shared/utils
  echo "Shared created."
else
  echo "Shared already exists"
fi
for CLASS_NAME in "$@"
do

  # Infrastructure
  
  mkdir -p "$CLASS_NAME/infrastructure/adapters/primary/v2/controllers"
  mkdir -p "$CLASS_NAME/infrastructure/adapters/primary/v3/controllers"
  mkdir -p "$CLASS_NAME/infrastructure/adapters/primary/v2/dtos"
  mkdir -p "$CLASS_NAME/infrastructure/adapters/primary/v3/dtos"
  mkdir -p "$CLASS_NAME/infrastructure/adapters/secondary/repositories"
  mkdir -p "$CLASS_NAME/infrastructure/adapters/secondary/mappers"
  mkdir -p "$CLASS_NAME/infrastructure/adapters/secondary/services"
  mkdir -p "$CLASS_NAME/infrastructure/adapters/secondary/entities"

  # Domain
  
  mkdir -p "$CLASS_NAME/domain/ports/primary"
  mkdir -p "$CLASS_NAME/domain/ports/secondary"
  mkdir -p "$CLASS_NAME/domain/services"
  
  echo "Class structure created: $CLASS_NAME"
done
