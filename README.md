# SSLEchoReply
SSL echo reply client and server.  Can be used to verify a server side Java keystore file.

##How to build:
```
./gradlew clean build
```

##Example server invocation:
```
java -Djavax.net.ssl.keyStore=YOUR_KEYSTORE_FILE -Djavax.net.ssl.keyStorePassword=YOUR_KEYSTORE_PASS -jar EchoServer.jar 8888
```


##Example client invocation:
```
java -jar EchoClient.jar your.server.com 8888
```
