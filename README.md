# mm

## Setup and usage

1. Change the NGINX configuration as needed (`./data/nginx/app.conf`)
2. Tweaks `domains` and `email` variables inside the bash script `./init-letsencrypt.sh`
3. Run `sudo ./init-letsencrypt.sh`
4. Run `docker-compose up --build`

## Questions

1. Are there cases when `restart: always` shouldn't be used?
2. Do I need to specify `volumes` also outside the `services` block? If so, why?


## Credits

* [Philipp](https://pentacent.medium.com/nginx-and-lets-encrypt-with-docker-in-less-than-5-minutes-b4b8a60d3a71)
