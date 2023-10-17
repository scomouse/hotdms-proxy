
DROP TABLE IF EXISTS `hotdms_crawler`;
CREATE TABLE `hotdms_crawler` (
  `id` bigint NOT NULL,
  `name` varchar(50) NOT NULL,
  `url` varchar(200) NOT NULL,
  `xpath` varchar(200) NOT NULL,
  `timeInterval` int NOT NULL,
  `keymap` varchar(100) NOT NULL,
  `handlerClass` varchar(100) NOT NULL,
  `taskGroup` varchar(50) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `updateTime` timestamp NOT NULL,
  `lastCrawlTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for hotdms_netway
-- ----------------------------
DROP TABLE IF EXISTS `hotdms_netway`;
CREATE TABLE `hotdms_netway` (
  `id` bigint NOT NULL,
  `name` varchar(50) NOT NULL,
  `url` varchar(200) NOT NULL,
  `weight` int NOT NULL,
  `passCode` varchar(200) NOT NULL,
  `status` varchar(10) NOT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for hotdms_proxy
-- ----------------------------
DROP TABLE IF EXISTS `hotdms_proxy`;
CREATE TABLE `hotdms_proxy` (
  `id` bigint NOT NULL,
  `ip` varchar(50) NOT NULL,
  `port` int NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `speed` int NOT NULL,
  `ratio` int NOT NULL,
  `checkTime` timestamp NOT NULL,
  `createTime` timestamp NOT NULL,
  `status` varchar(10) NOT NULL,
  `source` varchar(50) NOT NULL,
  `flag` int NOT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for hotdms_task
-- ----------------------------
DROP TABLE IF EXISTS `hotdms_task`;
CREATE TABLE `hotdms_task` (
  `id` bigint NOT NULL,
  `name` varchar(50) NOT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `beanName` varchar(255) NOT NULL,
  `cronExp` varchar(50) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `lastRuntime` datetime DEFAULT NULL,
  `status` int NOT NULL,
  `needLog` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for hotdms_task_log
-- ----------------------------
DROP TABLE IF EXISTS `hotdms_task_log`;
CREATE TABLE `hotdms_task_log` (
  `id` bigint NOT NULL,
  `taskid` bigint NOT NULL,
  `time` datetime NOT NULL,
  `status` varchar(20) NOT NULL,
  `message` text,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `hotdms_crawler` VALUES ('1712767959868428381', 'hidemy', 'https://hidemy.io/cn/proxy-list/', '//div[@class=\'table_block\']/table/tbody/tr', '30', 'ip,port,address', 'XPathWebPageCrawler', 'crawler1', '0', '2023-10-13 17:51:18', '2023-10-17 11:27:00');
INSERT INTO `hotdms_crawler` VALUES ('1712768506998607899', 'seofangfa', 'https://proxy.seofangfa.com/', '//table[@class=\'table\']/tbody/tr', '30', 'ip,port,,address', 'XPathWebPageCrawler', 'crawler1', '0', '2023-10-13 17:53:28', '2023-10-17 16:20:03');
INSERT INTO `hotdms_crawler` VALUES ('1712768891133939723', 'zdaye', 'https://www.zdaye.com/free/{1-5}/', '//table[@id=\'ipc\']/tbody/tr', '30', 'ip,port,,address', 'XPathWebPageCrawler', 'crawler1', '0', '2023-10-13 17:55:00', '2023-10-17 16:20:02');

INSERT INTO `hotdms_netway` VALUES ('1713757557432074278', 'biqudao', 'https://www.biqudaoge.cc/bqge12276402/723934364.html', '20', '200', '0', '1');
INSERT INTO `hotdms_netway` VALUES ('1713757955924508756', 'ibiquge', 'http://www.ibiquge.cc/312/270447.html', '20', '200', '0', '2');
INSERT INTO `hotdms_netway` VALUES ('1713758196509786144', 'biquge66', 'https://www.biquge66.net/book/593/1035109.html', '20', '200', '0', '3');
INSERT INTO `hotdms_netway` VALUES ('1713759014424232027', 'bq3', 'http://www.bq3.top/xs/1575147/17255891.htm', '20', '200', '0', '4');
INSERT INTO `hotdms_netway` VALUES ('1713759401524936763', 'biquzw789', 'https://www.biquzw789.org/353/353829/197278603.html', '20', '200', '0', '5');

INSERT INTO `hotdms_task` VALUES ('1', '代理爬虫任务', 'crawler1', 'com.hotdms.proxy.schedule.ProxyCrawlerScheduleTask', '0 0/5 * * * ? ', '代理爬虫任务', '2023-10-10 21:07:55', '0', '');
INSERT INTO `hotdms_task` VALUES ('2', '代理有效性检测任务', 'check', 'com.hotdms.proxy.schedule.ExistProxyCheckScheduleTask', '0 0/5 * * * ?', '代理有效性检测任务', '2023-10-10 21:07:55', '0', '');
INSERT INTO `hotdms_task` VALUES ('3', '代理无效清理任务', 'clear', 'com.hotdms.proxy.schedule.ClearProxyScheduleTask', '0 0/5 * * * ?', '代理无效清理任务', '2023-10-10 21:07:55', '0', '');
