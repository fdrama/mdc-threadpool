# Docker

## 	Docker概述

### 基本介绍  

> Docker 是一个开源的应用容器引擎，基于 Go 语言 并遵从 Apache2.0 协议开源。
>
> Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。
>
> 容器是完全使用沙箱机制，相互之间不会有任何接口（类似 iPhone 的 app）,更重要的是容器性能开销极低。
>
> Docker 从 17.03 版本之后分为 CE（Community Edition: 社区版） 和 EE（Enterprise Edition: 企业版），我们用社区版就可以了。官网：https://docs.docker.com/
>
> - Web 应用的自动化打包和发布。
>
> - 自动化测试和持续集成、发布。
>
> - 在服务型环境中部署和调整数据库或其他的后台应用。
>
> - 从头编译或者扩展现有的 OpenShift 或 Cloud Foundry 平台来搭建自己的 PaaS 环境。

### 容器化技术  
  


> 虚拟化技术特点：1.资源占用多 2.冗余步骤多 3.启动很慢
> 
> 容器化技术：容器化技术不是模拟的一个完整的操作系统
>
> 比较Docker和虚拟机的不同：
>
> 1.传统虚拟机，虚拟出硬件，运行一个完整的操作系统，然后在这个系统上安装和运行软件。
>
> 2.Docker容器内的应用直接运行在宿主机的内容，容器是没有自己的内核的，也没有虚拟硬件。
>
> 3.每个容器都是相互隔离的，每个容器都有属于自己的文件系统，互不影响。

### 术语

> 镜像	 image		   
>
> 容器 	container	利用容器技术，独立运行一个或一组应用，通过镜像创建
>
> 仓库	repository	存放镜像的地方

 ## Docker 安装

```
# 查看系统内核
uname -r

# 移除安装的docker
sudo yum remove docker \
docker-client \
docker-client-latest \
docker-common \
docker-latest \
docker-latest-logrotate \
docker-logrotate \
docker-engine

# 2.安装需要的工具包              
sudo yum install -y yum-utils

# 3.设置镜像地址
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
# 4.安装docker  ce 社区版
sudo yum install docker-ce docker-ce-cli containerd.io
# 5.启动
sudo systemctl start docker
# 6.查看是否安装成功
docker version 

# 7.hello-world
sudo docker run hello-world
# 8.查看容器
docker ps  

# 9.remove 
sudo yum remove docker-ce docker-ce-cli containerd.io

sudo rm -rf /var/lib/docker
sudo rm -rf /var/lib/containerd

```

## Docker镜像加速器

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://4ar8rmrv.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

## 启动docker 

```
sudo systemctl daemon-reload
# 启动docker
sudo systemctl start docker
# 重启docker
sudo systemctl restart docker
# 开机启动docker
sudo systemctl enable docker
```



## Docker基本命令

```
https://docs.docker.com/reference/

docker version	# 显示docker版本信息
docker info 	# 显示docker系统信息
docker $cmd --help 
```

### Docker镜像命令

```
docker images	# 查看所有镜像

仓库源			镜像标签	镜像id		 镜像创建时间		镜像大小
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
hello-world   latest    feb5d9fea6a5   3 months ago   13.3kB

docker serach	# 在docker hub搜索镜像

NAME                              DESCRIPTION                                     STARS   mysql                             MySQL is a widely used, open-source relation…   11984
OFFICIAL   						  AUTOMATED
[OK]

docker search mysql --filter=STARS=3000

docker pull	镜像名:tag[]	# 下载镜像


Using default tag: latest	# 默认latest
latest: Pulling from library/mysql	
72a69066d2fe: Pull complete	# 分层下载 联合文件系统
93619dbc5b36: Pull complete
99da31dd6142: Pull complete
626033c43d70: Pull complete
37d5d7efb64e: Pull complete
ac563158d721: Pull complete
d2ba16033dad: Pull complete
688ba7d5c01a: Pull complete
00e060b6d11d: Pull complete
1c04857f594f: Pull complete
4d7cfa90e6ea: Pull complete
e0431212d27d: Pull complete
Digest: sha256:e9027fe4d91c0153429607251656806cc784e914937271037f7738bd5b8e7709	# 签名
Status: Downloaded newer image for mysql:latest
docker.io/library/mysql:latest	# 真实地址

# 等价
docker pull mysql
docker pull docker.io/library/mysql:latest


docker pull mysql:5.7 
[admin@iZ2ze5vrnucj8nymuzcy95Z root]$ docker pull mysql:5.7
5.7: Pulling from library/mysql 
72a69066d2fe: Already exists
93619dbc5b36: Already exists
99da31dd6142: Already exists
626033c43d70: Already exists
37d5d7efb64e: Already exists
ac563158d721: Already exists
d2ba16033dad: Already exists
0ceb82207cd7: Pull complete
37f2405cae96: Pull complete
e2482e017e53: Pull complete
70deed891d42: Pull complete
Digest: sha256:f2ad209efe9c67104167fc609cca6973c8422939491c9345270175a300419f94
Status: Downloaded newer image for mysql:5.7
docker.io/library/mysql:5.7


docker rmi -f $id $id	#删除镜像
docker rmi -f $(docker images -aq)  #删除全部镜像
```



###  Docker容器命令

```
# 新建容器并启动
docker run  [可选参数] image
# 参数名称
-- name=Name  容器名称
-it				使用交互方式运行
-d 	--detach	后台方式运行
-p				
	-p ip:主机端口:容器端口
	-p 主机端口:容器端口
	-p 容器端口


# 查看容器
docker ps 命令
	# 列出正在运行的容器
-a 	# 显示所有运行的容器+历史运行的容器
-n=? # 显示最近运行的容器
-q 	#只显示容器的编号

[admin@iZ2ze5vrnucj8nymuzcy95Z root]$ docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED          STATUS                     PORTS     NAMES
8c402b23474f   centos         "/bin/bash"   15 seconds ago   Exited (0) 7 seconds ago             optimistic_williams


# 退出容器
exit  # 容器停止并退出
Ctrl + P + Q  # 容器不停止退出
# 删除容器
docker rm 容器id 	# 删除指定容器
docker rm -f $(docker ps -aq) # 删除所有容器
docker ps -a -q|xargs docker rm # 删除所有容器

# 启动容器
docker start 容器id
# 重启容器
docker restart 容器id
# 停止当前正在运行的容器
docker stop 容器id
# 强制结束当前运行的容器
docker kill 容器id

# 后台启动容器

docker run -d 镜像名

常见问题: docker ps  发现该镜像停止了
# docker 容器使用后台运行，就必须要有一个前台进程, docker发现没有应用，就会自动停止
# nginx ,容器启动后，发现自己没有提供服务，就会立即停止，释放占用资源


# 查看容器日志
docker logs -t -f --tail 10 容器id

docker run -d centos /bin/sh -c "while true;do echo hello; sleep 1; done"

# 查看容器

docker inspect 容器id

# 进入容器
docker exec -it 容器id /bin/bash

docker attach 容器id

# 查看容器状态

docker stats

# 拷贝容器内文件
docker cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH

# 查看进程信息
docker top

# 部署Es + kibana

# 指定内存大小

docker run -d --name elasticsearch01 -p 9200:9200 -p 9300:9300 -e ES_JAVA_OPTS="-Xms128m -Xmx128m" -e "discovery.type=single-node" elasticsearch

```

## Docker可视化工具

- portainer
- Rancher (CI/CD 再用)

```
docker search portainer

docker pull portainerci/portainer  

docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock --restart=always --name prtainer portainer/portainer

```

## Docker镜像加载原理

```
UnionFS 联合文件系统
```

```
docker 镜像加载原理
```

```
docker 分层
```

##  Docker提交镜像

```
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
-a  --author
-m  --message

docker commit -a="fdrama" -m "add webapps app" 37c719e26fed tomcat01:1.0

```

## 容器数据卷

> 应用和环境打包成一个镜像！
>
> 数据，如果存在容器中，容器删除，数据就会丢失
>
> 容器之间可以实现数据共享 ！ Docker 容器中产生的数据会持久化
>
> 目录挂载， 将容器内的目录挂载到Linux，数据持久化和同步操作，容器间可以数据共享

```
1. 直接使用命令挂载 -v

docker run -it -v 主机目录:容器目录 -p 主机端口:容器端口


容器状态不论停止还是启动，都会同步挂载目录的数据

docker run  --name mysql -d -p 3306:3306 -v /home/mysql/conf:/etc/mysql/conf.d -v /home/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root mysql


docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag
2. 匿名挂载

-p 随机IP 
-v 不设置主机目录
docker run -d -P -name nginx01 -v /etc/nginx nginx
# 查看所有的volume 
docker volume ls

3. 具名挂载
docker run -d -P --name nginx02 -v 卷名:容器目录 

docker run -d -P --name nginx02 -v nginx-data:/etc/nginx/ nginx
# 查看挂载卷地址 通过具名地址
docker volume inspect nginx-data
# 默认地址
/var/lib/docker/volumes

ro	readonly
rw  readwrite

# ro 只能通过宿主机操作
# rw 默认
docker run -d -p --name nginx02 -v nginx-data:/etc/nginx/:ro nginx






```



## DockerFile

DockerFile 就是用来构建docker 镜像的构建文件！ 命令脚本！

```
FROM centos

VOLUME ["/volume01","/volume02"]

CMD echo "---end---"
CMD /bin/bash


docker build -f dockerfiletest -t fdrama/centos:1.0 .

docker inspect 容器id 查看挂载目录 monts


```

## 数据卷容器

```
# 容器间共享  挂载目录都是指向了宿主机的挂载目录

docker run -it -d --name docker03 --volumes-from 容器id 镜像id

```



## Docker网络

## Docker Compose

## Docker Swarm

## CI/CD jenkins 

 