FROM node:16

# create the directory inside the container
WORKDIR /usr/src/app

# copy the package.json files from local machine to the workdir in container
COPY package*.json ./

# install modules and compile the code
RUN npm install
RUN npm run build

# copy the generated modules and all other files to the container
COPY . .

EXPOSE 3000

CMD ["node", "dist/index.js"]