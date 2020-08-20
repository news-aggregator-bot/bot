wget -q $1 -O docker-stack-bot.yml
echo "Deploying new na-bot stack"
docker stack rm na-bot
docker stack deploy --compose-file docker-stack-bot.yml --with-registry-auth na-bot