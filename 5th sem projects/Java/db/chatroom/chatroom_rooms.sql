CREATE DATABASE  IF NOT EXISTS `chatroom` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `chatroom`;
-- MySQL dump 10.13  Distrib 5.5.9, for Win32 (x86)
--
-- Host: localhost    Database: chatroom
-- ------------------------------------------------------
-- Server version	5.5.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rooms` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `roomname` varchar(45) NOT NULL,
  `photo` varchar(150) NOT NULL,
  `category` varchar(45) NOT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (5,'Laptops','src/uploaded_pics/1562313614698.jpg','Technology'),(6,'Mobile','src/uploaded_pics/1562313650555.jpg','Technology'),(7,'Virtual Reality','src/uploaded_pics/1562313678209.png','Technology'),(8,'Cryptocurrency','src/uploaded_pics/1562313707000.jpg','Technology'),(9,'Cloud','src/uploaded_pics/1562313731921.jpg','Technology'),(10,'Agra','src/uploaded_pics/1562314131020.jpg','Places'),(11,'Srinagar','src/uploaded_pics/1562314186523.jpg','Places'),(12,'Goa','src/uploaded_pics/1562314220549.jpg','Places'),(13,'USA','src/uploaded_pics/1562314356486.jpg','Places'),(14,'Manali','src/uploaded_pics/1562314387812.jpg','Places'),(15,'Football','src/uploaded_pics/1562314455903.jpg','Sports'),(16,'Hockey','src/uploaded_pics/1562314487463.jpg','Sports'),(17,'BasketBall','src/uploaded_pics/1562314521468.jpg','Sports'),(18,'Wrestling','src/uploaded_pics/1562314544170.jpg','Sports'),(19,'Tennis','src/uploaded_pics/1562314565309.jpg','Sports');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-06 16:58:11
