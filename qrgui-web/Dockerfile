#stage 1
FROM node:16-alpine as node
WORKDIR /app
COPY . .
RUN npm install && npm run build

RUN mkdir /app/dist/assets
COPY dist/ /app/dist/assets

COPY *.html /app/dist/

RUN mkdir /app/dist/views
COPY ./views /app/dist/views

RUN mkdir /app/dist/images
COPY ./images /app/dist/images

RUN mkdir /app/dist/styles
COPY ./styles /app/dist/styles

RUN mkdir -p /app/dist/node_modules/bootstrap/dist/css/
COPY ./node_modules/bootstrap/dist/css/bootstrap.min.css /app/dist/node_modules/bootstrap/dist/css/

RUN mkdir -p /app/dist/node_modules/angular-loading-bar/build/
COPY ./node_modules/angular-loading-bar/build/loading-bar.min.css /app/dist/node_modules/angular-loading-bar/build/

RUN mkdir -p /app/dist/node_modules/bootstrap/dist/
COPY ./node_modules/bootstrap/dist /app/dist/node_modules/bootstrap/dist/

RUN mkdir -p /app/dist/scripts/translations
COPY ./scripts/translations/locale-en.json /app/dist/scripts/translations
COPY ./scripts/translations/locale-it.json /app/dist/scripts/translations

RUN mkdir -p /app/dist/node_modules/angular-i18n/
COPY ./node_modules/angular-i18n/angular-locale_it.js /app/dist/node_modules/angular-i18n/
COPY ./node_modules/angular-i18n/angular-locale_en.js /app/dist/node_modules/angular-i18n/

#stage 2
FROM nginx:alpine
COPY --from=node /app/dist /usr/share/nginx/html/

# Expose port
EXPOSE 8080
