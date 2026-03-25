FROM nginx:alpine

COPY erp-admin.html /usr/share/nginx/html/

RUN cat > /etc/nginx/conf.d/default.conf << 'EOF'
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index erp-admin.html;
    
    # 设置字符编码
    charset utf-8;

    location / {
        try_files $uri $uri/ /erp-admin.html;
    }

    # 用户服务 - 去掉/api前缀
    location /api/user {
        rewrite ^/api(.*)$ $1 break;
        proxy_pass http://erp-gateway:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 商品服务 - 去掉/api前缀
    location /api/product {
        rewrite ^/api(.*)$ $1 break;
        proxy_pass http://erp-gateway:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 订单服务 - 去掉/api前缀
    location /api/order {
        rewrite ^/api(.*)$ $1 break;
        proxy_pass http://erp-gateway:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF

EXPOSE 80
