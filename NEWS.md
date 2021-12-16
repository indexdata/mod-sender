## 2021-12-16 v1.6.1
 * Upgrade to RMB 33.0.4 and Log4j 2.16.0 (MODSENDER-44)

## 2021-06-11 v1.6.0
 * Upgrade to RMB 33.0.0 and Vertx 4.1.0 (MODSENDER-40)

## 2020-03-09 v1.5.0
* Upgrade to RMB v32.1.0 and Vertx 4.0.0 (MODSENDER-37)

## 2020-10-08 v1.4.0
 * Upgrade to RMB v31.0.2 and JDK11 (MODSENDER-35)
 * Fix notices for patrons with custom fields (MODSENDER-30)
 * Allow additional fields in user schema (MODSENDER-33)

## 2020-06-11 v1.3.0
 * MODSENDER-28 Update RMB version to 30.0.2 and Vertx to 3.9.1

## 2019-04-12 v1.2.0
 * MODSENDER-24 Update RMB version
 * MODSENDER-23 Use JVM features to manage container memory
 * FOLIO-2321 Remove old MD metadata 
 * MODSENDER-21 mod-sender does not purge tenant data
 * MODSENDER-20 mod-sender leaks WebClient objects
 * FOLIO-2256 enable kube-deploy pipeline for platform-core modules
 * FOLIO-2234 Add LaunchDescriptor settings
 * MODSENDER-16 Performance testing - initial setup for performance tests

## 2019-06-11 v1.1.0
 * Add links to README additional info (FOLIO-473)
 * Initial module metadata (FOLIO-2003)
 
## 2018-11-07 v1.0.0
 * Endpoint **POST /message-delivery** was implemented
 * DeliveryVerticle class was implemented to register delivery channels
 * Email delivery channel was registered with name 'email'
 
 | METHOD |  URL                          | DESCRIPTION                                                       |
 |--------|-------------------------------|-------------------------------------------------------------------|
 | POST   | /message-delivery             | Message delivery endpoint                                         |
