# jekyll安装

## 环境需要  
    
    
[官方说明](http://jekyllcn.com/docs/installation/)

                
## 安装步骤  

1. 查看系统版本   
    
        lsb_release -a  or  uname -r

2. 先安装ruby rubyGems   
    
    *RubyGems是Ruby的一个包管理器*

        sudo yum -y install ruby

        ruby -v
        gem -v 

    
    *gem 源替换*

        gem source -l
        gem source -r https://rubygems.org/
        gem source -a https://gems.ruby-china.com
        gem source -l

    
3. 检查python   

        python -V  

4. 安装nodejs  
        sudo yum -y install nodejs

        node -v

5. 安装jekyll  
   
        sudo gem install jekyll

6. 查看是否安装成功  
   
        jekyll -v

7. 更新jekyll    
    
        gem update jekyll
    
## 问题解决  

***报错信息***  

``` 
You might have to install separate package for the ruby development
environment, ruby-dev or ruby-devel for example.
```


*安装ruby开发包*

    sudo yum  -y install ruby-devel

***报错信息*** 

```
To see why this extension failed to compile, please check the mkmf.log which can be found here:

/usr/local/lib64/gems/ruby/eventmachine-1.2.7/mkmf.log

extconf failed, exit code 1

cannot read spec file ‘/usr/lib/rpm/redhat/redhat-hardened-cc1’: No such file or directory
```

*安装RPM软件包的管理工具*  

    sudo yum install redhat-rpm-config

***报错信息*** 

```
make: g++: Command not found
```
解决方法

*安装 C++编译器*

    sudo yum -y install g++  

    which g++


# jekyll 使用

## 常用命令
    jekyll --help
