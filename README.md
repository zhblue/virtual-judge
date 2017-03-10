
virtual-judge
=============

Holding contests using problems from other OJs!!  
[点这里跳到中文版](#修改说明)

## Changes

Comparing to [the original version](https://github.com/chaoshxxu/virtual-judge/tree/9dc0be82ed7e05dc17b7f042a935bf3db8435ce4) of virtual-judge source code, there are the following changes in this version:

- Fix the support for ACdream, Aizu, Codeforces, CSU, NBUT, POJ, SCU, SPOJ, URAL and UVA OJs
- Add support for CodeforcesGym and Tyvj OJ
- Add support for MathJax
- Update language information of code submission
- Automatically download the PDF and image files of problem description to server so that clients can view them without the Internet access
- Other small changes


## How to deploy

- Click [here](https://github.com/chaoshxxu/virtual-judge/wiki/How-to-deploy-your-own-Virtual-Judge) to visit the official wiki page and follow the instructions.  
  Notice that since this version of virtual-judge add support for CodeforcesGym and Tyvj, you should add two OJs called `CFGym` and `Tyvj` in `remote_accounts.json` like [this](https://gist.github.com/hnshhslsh/eb79be0d2a436a16cec77ff2f8552f7e) to provide accounts for them.
- Click [here](https://www.myblog.link/2017/01/09/VJudge-On-Windows-X64/) to visit the unofficial rapid deployment guide for Windows x64 (in Chinese).

## 修改说明

与 [原版](https://github.com/chaoshxxu/virtual-judge/tree/9dc0be82ed7e05dc17b7f042a935bf3db8435ce4) virtual-judge 源码相比, 该本版有以下修改：
- 修复对ACdream, Aizu, Codeforces, CSU, NBUT, POJ, SCU, SPOJ, URAL 和 UVA 这些OJ的支持
- 添加对CodeforcesGym以及Tyvj这些OJ的支持
- 添加对MathJax的支持（用于显示公式，默认使用互联网上的版本，需要离线请自行下载并更新`script.min.js`中的路径）
- 更新代码提交时可选的语言
- 自动下载题目描述的PDF文件以及图片到服务器，以便客户端在没有互联网的情况下浏览（题目使用PDF文档显示或者题面包含图片时把PDF或者图片文件离线到服务器上，客户端从VJ服务器上显示PDF及图片而不是原始OJ服务器，以供在只有服务器有互联网访问，客户端只有跟服务器的内网连接时可以正常使用，这是我本人的部署环境，一些代码会为此为目标环境而编写）
- 其它小变化

## 部署说明

- 点击 [这里](https://github.com/chaoshxxu/virtual-judge/wiki/How-to-deploy-your-own-Virtual-Judge) 访问官方wiki页面按照指令进行安装。  
  注意因为本版virtual-judge添加了对CodeforcesGym以及Tyvj的支持，所以你应该在`remote_accounts.json`文件中添加叫做`CFGym`以及`Tyvj`的OJ，像[这样](https://gist.github.com/hnshhslsh/eb79be0d2a436a16cec77ff2f8552f7e)，来给这些OJ提供账号(CFGym的账号可以把CF的直接复制一遍)。

- 点击 [这里](https://www.myblog.link/2017/01/09/VJudge-On-Windows-X64/) 访问非官方的Windows x64下的快速部署说明及资源下载。  
  其包含已编译的virtual-judge网站以及适用于Windows x64的JRE、Tomcat、MySQL、Redis等完整运行环境，可以在几分钟内完成部署。

