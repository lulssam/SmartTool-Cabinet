-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: gestaoferramentas
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `armario`
--

DROP TABLE IF EXISTS `armario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `armario` (
  `nArmario` int NOT NULL,
  `capacidade` int NOT NULL,
  `estado` varchar(50) NOT NULL,
  `id_gestor` int DEFAULT NULL,
  PRIMARY KEY (`nArmario`),
  KEY `id_gestor` (`id_gestor`),
  CONSTRAINT `armario_ibfk_1` FOREIGN KEY (`id_gestor`) REFERENCES `gestor` (`id_func`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `armario`
--

LOCK TABLES `armario` WRITE;
/*!40000 ALTER TABLE `armario` DISABLE KEYS */;
INSERT INTO `armario` VALUES (101,50,'Operacional',1);
/*!40000 ALTER TABLE `armario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `armazem`
--

DROP TABLE IF EXISTS `armazem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `armazem` (
  `id_Armazem` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id_Armazem`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `armazem`
--

LOCK TABLES `armazem` WRITE;
/*!40000 ALTER TABLE `armazem` DISABLE KEYS */;
INSERT INTO `armazem` VALUES (1),(2);
/*!40000 ALTER TABLE `armazem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backoffice`
--

DROP TABLE IF EXISTS `backoffice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backoffice` (
  `id_func` int NOT NULL,
  PRIMARY KEY (`id_func`),
  CONSTRAINT `backoffice_ibfk_1` FOREIGN KEY (`id_func`) REFERENCES `funcionario` (`id_func`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backoffice`
--

LOCK TABLES `backoffice` WRITE;
/*!40000 ALTER TABLE `backoffice` DISABLE KEYS */;
INSERT INTO `backoffice` VALUES (3);
/*!40000 ALTER TABLE `backoffice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ferramenta`
--

DROP TABLE IF EXISTS `ferramenta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ferramenta` (
  `idFerramenta` int NOT NULL AUTO_INCREMENT,
  `estado` varchar(50) NOT NULL,
  `disponibilidade` varchar(50) NOT NULL,
  `nArmario` int DEFAULT NULL,
  `id_Armazem` int DEFAULT NULL,
  `nome_tipo` varchar(50) NOT NULL,
  PRIMARY KEY (`idFerramenta`),
  KEY `nArmario` (`nArmario`),
  KEY `id_Armazem` (`id_Armazem`),
  CONSTRAINT `ferramenta_ibfk_1` FOREIGN KEY (`nArmario`) REFERENCES `armario` (`nArmario`) ON DELETE SET NULL,
  CONSTRAINT `ferramenta_ibfk_2` FOREIGN KEY (`id_Armazem`) REFERENCES `armazem` (`id_Armazem`) ON DELETE SET NULL,
  CONSTRAINT `tipoFerramenta_fk1` FOREIGN KEY (`nome_tipo`) REFERENCES `tipo_ferramenta` (`nome_tipo`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ferramenta`
--

LOCK TABLES `ferramenta` WRITE;
/*!40000 ALTER TABLE `ferramenta` DISABLE KEYS */;
INSERT INTO `ferramenta` VALUES (1,'Excelente','Disponível',101,1),(2,'Usado','Emprestada',101,1);
/*!40000 ALTER TABLE `ferramenta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario` (
  `id_func` int NOT NULL AUTO_INCREMENT,
  `nomeCompleto` varchar(150) NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id_func`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionario`
--

LOCK TABLES `funcionario` WRITE;
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` VALUES (1,'João Silva','joao.silva@empresa.pt'),(2,'Maria Santos','maria.santos@empresa.pt'),(3,'Carlos Costa','carlos.costa@empresa.pt');
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gestor`
--

DROP TABLE IF EXISTS `gestor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gestor` (
  `id_func` int NOT NULL,
  PRIMARY KEY (`id_func`),
  CONSTRAINT `gestor_ibfk_1` FOREIGN KEY (`id_func`) REFERENCES `funcionario` (`id_func`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gestor`
--

LOCK TABLES `gestor` WRITE;
/*!40000 ALTER TABLE `gestor` DISABLE KEYS */;
INSERT INTO `gestor` VALUES (1);
/*!40000 ALTER TABLE `gestor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requisicao`
--

DROP TABLE IF EXISTS `requisicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requisicao` (
  `idRequesicao` int NOT NULL AUTO_INCREMENT,
  `dhRequesicao` datetime NOT NULL,
  `dhDevolucao` datetime DEFAULT NULL,
  `id_tecnico` int NOT NULL,
  PRIMARY KEY (`idRequesicao`),
  KEY `id_tecnico` (`id_tecnico`),
  CONSTRAINT `requisicao_ibfk_1` FOREIGN KEY (`id_tecnico`) REFERENCES `tecnico` (`id_func`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requisicao`
--

LOCK TABLES `requisicao` WRITE;
/*!40000 ALTER TABLE `requisicao` DISABLE KEYS */;
INSERT INTO `requisicao` VALUES (1,'2024-05-12 10:00:00',NULL,2);
/*!40000 ALTER TABLE `requisicao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requisicao_ferramenta`
--

DROP TABLE IF EXISTS `requisicao_ferramenta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requisicao_ferramenta` (
  `idRequesicao` int NOT NULL,
  `idFerramenta` int NOT NULL,
  PRIMARY KEY (`idRequesicao`,`idFerramenta`),
  KEY `idFerramenta` (`idFerramenta`),
  CONSTRAINT `requisicao_ferramenta_ibfk_1` FOREIGN KEY (`idRequesicao`) REFERENCES `requisicao` (`idRequesicao`) ON DELETE CASCADE,
  CONSTRAINT `requisicao_ferramenta_ibfk_2` FOREIGN KEY (`idFerramenta`) REFERENCES `ferramenta` (`idFerramenta`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requisicao_ferramenta`
--

LOCK TABLES `requisicao_ferramenta` WRITE;
/*!40000 ALTER TABLE `requisicao_ferramenta` DISABLE KEYS */;
INSERT INTO `requisicao_ferramenta` VALUES (1,2);
/*!40000 ALTER TABLE `requisicao_ferramenta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tecnico`
--

DROP TABLE IF EXISTS `tecnico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tecnico` (
  `id_func` int NOT NULL,
  PRIMARY KEY (`id_func`),
  CONSTRAINT `tecnico_ibfk_1` FOREIGN KEY (`id_func`) REFERENCES `funcionario` (`id_func`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tecnico`
--

LOCK TABLES `tecnico` WRITE;
/*!40000 ALTER TABLE `tecnico` DISABLE KEYS */;
INSERT INTO `tecnico` VALUES (2);
/*!40000 ALTER TABLE `tecnico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_ferramenta`
--

DROP TABLE IF EXISTS `tipo_ferramenta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_ferramenta` (
  `idFerramenta` int NOT NULL,
  `nome` varchar(100) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `descricao` text,
  PRIMARY KEY (`idFerramenta`,`nome`),
  CONSTRAINT `tipo_ferramenta_ibfk_1` FOREIGN KEY (`idFerramenta`) REFERENCES `ferramenta` (`idFerramenta`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_ferramenta`
--

LOCK TABLES `tipo_ferramenta` WRITE;
/*!40000 ALTER TABLE `tipo_ferramenta` DISABLE KEYS */;
INSERT INTO `tipo_ferramenta` VALUES (1,'Berbequim Percussão','Elétrica','Modelo industrial 20V'),(2,'Alicate de Corte','Manual','Alicate isolado 1000V');
/*!40000 ALTER TABLE `tipo_ferramenta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `view_inventario_detalhado`
--

DROP TABLE IF EXISTS `view_inventario_detalhado`;
/*!50001 DROP VIEW IF EXISTS `view_inventario_detalhado`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_inventario_detalhado` AS SELECT 
 1 AS `idFerramenta`,
 1 AS `Nome_Tipo`,
 1 AS `categoria`,
 1 AS `estado`,
 1 AS `disponibilidade`,
 1 AS `Armario`,
 1 AS `Armazem`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_mapa_emprestimos`
--

DROP TABLE IF EXISTS `view_mapa_emprestimos`;
/*!50001 DROP VIEW IF EXISTS `view_mapa_emprestimos`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_mapa_emprestimos` AS SELECT 
 1 AS `idRequesicao`,
 1 AS `Tecnico`,
 1 AS `Ferramenta`,
 1 AS `Data_Saida`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_inventario_detalhado`
--

/*!50001 DROP VIEW IF EXISTS `view_inventario_detalhado`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_inventario_detalhado` AS select `f`.`idFerramenta` AS `idFerramenta`,`tf`.`nome` AS `Nome_Tipo`,`tf`.`categoria` AS `categoria`,`f`.`estado` AS `estado`,`f`.`disponibilidade` AS `disponibilidade`,`f`.`nArmario` AS `Armario`,`f`.`id_Armazem` AS `Armazem` from (`ferramenta` `f` join `tipo_ferramenta` `tf` on((`f`.`idFerramenta` = `tf`.`idFerramenta`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_mapa_emprestimos`
--

/*!50001 DROP VIEW IF EXISTS `view_mapa_emprestimos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_mapa_emprestimos` AS select `r`.`idRequesicao` AS `idRequesicao`,`func`.`nomeCompleto` AS `Tecnico`,`tf`.`nome` AS `Ferramenta`,`r`.`dhRequesicao` AS `Data_Saida` from (((`requisicao` `r` join `funcionario` `func` on((`r`.`id_tecnico` = `func`.`id_func`))) join `requisicao_ferramenta` `rf` on((`r`.`idRequesicao` = `rf`.`idRequesicao`))) join `tipo_ferramenta` `tf` on((`rf`.`idFerramenta` = `tf`.`idFerramenta`))) where (`r`.`dhDevolucao` is null) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-15 19:05:53
