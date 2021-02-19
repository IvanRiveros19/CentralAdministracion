-- MySQL dump 10.13  Distrib 5.7.26, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: central_autobuses
-- ------------------------------------------------------
-- Server version	5.7.26-log

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
-- Table structure for table `cdestino`
--

DROP TABLE IF EXISTS `cdestino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cdestino` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(80) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOMBRE` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cdestino`
--

/*!40000 ALTER TABLE `cdestino` DISABLE KEYS */;
INSERT INTO `cdestino` VALUES (4,'HUAYACOCOTLA VER.'),(1,'MEXICO D.F. NORTE (CENTRAL NORTE )'),(2,'PACHUCA HGO.'),(3,'PAHUATLAN PUE.'),(5,'QUERETARO QRO.');
/*!40000 ALTER TABLE `cdestino` ENABLE KEYS */;

--
-- Table structure for table `cempresa`
--

DROP TABLE IF EXISTS `cempresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cempresa` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOMBRE` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cempresa`
--

/*!40000 ALTER TABLE `cempresa` DISABLE KEYS */;
INSERT INTO `cempresa` VALUES (5,'ADO'),(3,'CONEXION'),(2,'ESTRELLA BLANCA'),(4,'FUTURA');
/*!40000 ALTER TABLE `cempresa` ENABLE KEYS */;

--
-- Table structure for table `corigen`
--

DROP TABLE IF EXISTS `corigen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `corigen` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(80) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOMBRE` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corigen`
--

/*!40000 ALTER TABLE `corigen` DISABLE KEYS */;
INSERT INTO `corigen` VALUES (2,'ÁLAMO'),(1,'TULANCINGO, HGO.');
/*!40000 ALTER TABLE `corigen` ENABLE KEYS */;

--
-- Table structure for table `treporte`
--

DROP TABLE IF EXISTS `treporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treporte` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NUMERO` int(11) NOT NULL,
  `HORA_SALIDA` time NOT NULL,
  `CORIGEN_ID` int(11) NOT NULL,
  `CDESTINO_ID` int(11) NOT NULL,
  `CEMPRESA_ID` int(11) NOT NULL,
  `TIPO_SERVICIO` varchar(80) NOT NULL,
  `TIPO_CORRIDA` varchar(2) NOT NULL,
  `NUMERO_ECONOMICO` int(11) NOT NULL,
  `NUMERO_PASAJEROS` int(11) NOT NULL,
  `NUMERO_SALIDA` int(11) NOT NULL,
  `CANCELADA` bit(1) NOT NULL DEFAULT b'0',
  `FECHA` date NOT NULL,
  `CREATED_DATE` date NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `corigen_treporte_idx` (`CORIGEN_ID`),
  KEY `cdestino_treporte_idx` (`CDESTINO_ID`),
  KEY `cempresa_treporte_idx` (`CEMPRESA_ID`),
  CONSTRAINT `cdestino_treporte_fk` FOREIGN KEY (`CDESTINO_ID`) REFERENCES `cdestino` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cempresa_treporte_fk` FOREIGN KEY (`CEMPRESA_ID`) REFERENCES `cempresa` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `corigen_treporte_fk` FOREIGN KEY (`CORIGEN_ID`) REFERENCES `corigen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treporte`
--

/*!40000 ALTER TABLE `treporte` DISABLE KEYS */;

/*!40000 ALTER TABLE `treporte` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-19 15:35:35
