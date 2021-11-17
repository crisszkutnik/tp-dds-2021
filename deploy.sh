cat ../tp-con-dc/deploy.sh 
cp -r Patitas ../deploy-patitas
cd ../deploy-patitas
git init
heroku git:remote -a tp-dds-2021
cd src/main/resources/ts
npm install
npm run build
cd ../../../../
mvn clean install -DskipTests
cd target/classes/public
rm .gitignore
rm -rf qr dogImg
mkdir qr
mkdir dogImg
git add .
git add -f src/main/resources/public/js
git commit -m "Deploy"
git push heroku master --force
