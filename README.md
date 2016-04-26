# simple chat

The simple chat without authorization

====

This application is available at <a href="https://jurasikt.pw">https://jurasikt.pw</a>

## Requirements 

- Python > 2.7
- Django > 1.9
- PostgresSQL > 9.1
- Nginx > 1.4.6
- OpenSSL > 1.0.1

## Install

### Install the packages from the repositories

```bash
apt-get update
apt-get install postgresql libpq-dev nginx python-pip  
```

### Install django

```bash
pip install django psycopg2 uwsgi
```

## Run project

Create uWSGI config file `nano /var/www/example.com/chat.ini` 

```
[uwsgi]
module = chat.wsgi:application

master = true
processes = 3

socket = chat.sock
chmod-socket = 664
vacuum = true
```

Create nginx virtual hosts. `nano /etc/nginx/sites-enabled/example.com` 

```
server {
    listen 80;
    server_name example.com; 

    location = /favicon.ico { 
        access_log off; 
        log_not_found off;
    }

    location ~* \.(?:jpg|jpeg|gif|png|ico)$ {
        expires 1M;
        access_log off;
        add_header Cache-Control "public";
    }

    location ~* \.(?:css|js)$ {
        expires 1y;
        access_log off;
        add_header Cache-Control "public";
    } 

    location /static/ {
        root /var/www/example.com/;
    }

    location / {
        include         uwsgi_params;
        uwsgi_pass      unix:/var/www/example.com/chat.sock;

    }

```

Run project 

```
service nginx restart
chown -R www-data:www-data /var/www/*
cd /var/www/example.com/
uwsgi --ini chat.ini </dev/null> chat.log &
```

## Why PostgresSQL not MySQL ?

![img](https://habrastorage.org/files/18e/026/fe6/18e026fe6eab49fba18c39fc2818d61b.jpg)

===== 

## Licenses

MIT License
