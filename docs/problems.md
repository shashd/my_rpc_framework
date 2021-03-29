# 记忆开发过程中遇到的问题

- lombok的引入的问题
- xml中配置的问题，关于parent和其中的那些groupId等需要进行配置的内容
    - 是我的创建项目的时候的配置出现了问题还是什么其他的因素导致的？
     
  
整个的my_rpc_framework也需要设置为source root
这样才可以应用到meven的部分
而且也是需要一个pom文件来声明依赖的

子项目生成的设置
group: github.zzz
artifact: rpc-api...
package: github.zzz.rpc.api...

设置好每个子项目中的parent的内容
    但是为什么要这样设置呢？如果不设置是否可以？

子项目中还是检测不到父的内容，那是少了maven的build的过程?


项目过程中是否要从最简单的netty的demo来开始创建
这样的话之后出问题也可能定位，或者是比较熟悉一点

不对，应该都是可以实现的

todo: 整理下这个最简单的socket通信的代码
然后就可以参照rpc来进行演化了
整理好之后commit提交下
