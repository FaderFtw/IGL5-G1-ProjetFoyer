# Use an official Node.js image as the base
FROM node:16 AS build

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application’s source code to the working directory
COPY . .


CMD ["npm", "start"]
# Build the React app for production
# RUN npm run build

# # Use a minimal Nginx image to serve the static files
# FROM nginx:alpine

# # Copy the build files from the previous stage to the Nginx public directory
# COPY --from=build /app/build /usr/share/nginx/html

# # Expose the port Nginx will listen on
# EXPOSE 80

# # Start Nginx
# CMD ["nginx", "-g", "daemon off;"]
