FROM nginx:1.23
RUN openssl dhparam -out /etc/nginx/dhparam.pem 4096 && openssl req -x509 -nodes -days 365 -subj \
    "/C=GH/ST=Ashanti/O=Yasobafinibus/CN=localhost" \
    -addext "subjectAltName=DNS:localhost" -newkey \
    rsa:2048 -keyout /etc/ssl/private/nginx.key -out /etc/ssl/certs/nginx.crt;

COPY ssl-params.conf /etc/nginx/snippets/ssl-params.conf
COPY self-signed.conf /etc/nginx/snippets/self-signed.conf
