docker-compose down --rmi=all
docker volume rm zeuswifosell_dbdata
docker-compose up -d


/*
openssl pkcs12 -export -in ./certificate.crt -inkey ./private.key -name wifosell -out wifosell_com.p12
keytool -importkeystore -deststorepass wifosellpass -destkeystore wifosell_com.jks -srckeystore wifosell_com.p12 -srcstoretype PKCS12

*/