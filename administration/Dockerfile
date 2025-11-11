# Build Stage
FROM node:18-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY public ./public
COPY src ./src
COPY tsconfig.json ./
COPY tailwind.config.js ./
RUN npm run build

# Production Stage
FROM nginx:stable-alpine AS production

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
