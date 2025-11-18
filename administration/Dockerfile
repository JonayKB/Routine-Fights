# syntax=docker/dockerfile:1.4

FROM node:18-slim AS build
WORKDIR /app

COPY package*.json ./
RUN --mount=type=cache,target=/root/.npm npm ci

COPY . .
ENV GENERATE_SOURCEMAP=false
RUN --mount=type=cache,target=/app/node_modules/.cache npm run build -- --no-progress

FROM nginx:stable-alpine AS production
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
