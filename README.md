# SSLEchoReply
SSL echo reply client and server.  Can be used to verify a server side Java keystore file.

##How to build:
```
./gradlew clean build
```

##Example server invocation:

With SSL
```
java -Djavax.net.ssl.keyStore=YOUR_KEYSTORE_FILE \
     -Djavax.net.ssl.keyStorePassword=YOUR_KEYSTORE_PASS \
     -jar EchoServer.jar 8888
```

Without SSL (but still port 8888)
```
java -jar EchoServer.jar -8888
```
##Example client invocation:

With SSL
```
java -jar EchoClient.jar your.server.com 8888
```

Without SSL (but still to server port 8888)
```
java -jar EchoClient.jar your.server.com -8888
```
