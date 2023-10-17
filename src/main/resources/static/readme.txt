系统说明：
本系统适配7.6产品
本系统用于普通用户会议预约、临时会议召开、会议日程查看、管理员审核预约会议及会议统计和会议报表查
version:v1.0.6.20200429

修改bug：
1、添加部门下没有用户时 部门传过来的isLeaf为false,导致数据导入报错问题

功能：
1、用户登录、注销（普通用户、系统管理员、机构管理员）
2、获取字典（会议类型、会议模式、会议级别、会议模版、更多资源）
3、获取机构部门人员树
4、会议申请（普通用户机构会议申请定时会议、周期会议、长期会议）  
5、会议列表（普通用户查询、修改、删除，机构管理员审核，批量审核，会议详情查看）
6、召开会议（普通用户召开个人会议，获取召开会议详情，开始录像，结束录像，退出会议）注意：要先开启客户端
7、日程（获取月、周的日程，普通用户获取自己所在的会议,管理员获取所有的会议）
8、会议统计
9、暗黑模式、明亮模式切换
10、会议报表


安装步骤：
一、支持服务安装			
	nginx服务安装
		将nginx-1.17.7.tar.gz解压到/opt/avcon目录下
		cd /opt/avcon
		tar -zxvf  nginx-1.17.7.tar.gz
		cd nginx-1.17.7
		./configure		
		make
		make install
		
	nginx服务启动	
	## 检查配置文件是否正确
	# /usr/local/nginx/sbin/nginx -t
	# /usr/local/nginx/sbin/nginx -V # 可以看到编译选项
	  
	## 启动、关闭
	# /usr/local/nginx/sbin/nginx # 默认配置文件 conf/nginx.conf，-c 指定
	# /usr/local/nginx/sbin/nginx -s stop
	或 pkill nginx
	  
	## 重启，不会改变启动时指定的配置文件
	# ./sbin/nginx -s reload
	或 kill -HUP `cat /usr/local/nginx/logs/nginx.pid`

	
二、web 安装（需要nginx服务）
	        
	1、将website.tar.gz解压到/opt/avcon/website目录下(website 如何没有，先创建)
	   cd /opt/avcon
	   tar -zxvf website.tar.gz
	   
	2、修改nginx服务配置  nginx-1.17.7/conf/nginx.conf
	找到下面代码，修改ip端口(后台接口服务地址)
		location ~ /hgapi/ {
			proxy_pass  http://127.0.0.1:9999;
		}
	2、启动服务
	   /usr/local/nginx/sbin/nginx

	

	
		
 