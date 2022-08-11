# mm

## Setup and usage

1. Change the NGINX configuration as needed (`./data/nginx/app.conf`)
2. Tweak `domains` and `email` variables inside the bash script `./init-letsencrypt.sh`
3. Run `sudo ./init-letsencrypt.sh`
4. Run `docker-compose -f docker-compose-prod.yml up --build -d`

## Useful commands

* `docker logs -f -n 50 filemanager`: show and follow output logs for filemanager
* `docker-compose -f docker-compose-dev.yml config`: verify your config is correct after having edited the .env file.
* `docker-compose -f docker-compose-test.yml config`: just run MySQL

## Credits

* [Philipp](https://pentacent.medium.com/nginx-and-lets-encrypt-with-docker-in-less-than-5-minutes-b4b8a60d3a71)
