#!/bin/bash
# Stop and remove containers, networks, images, and volumes all the services specified in descriptors
exec docker-compose -p na -f docker-compose.yml down