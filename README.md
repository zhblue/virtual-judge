virtual-judge
=============

Holding contests using problems from other OJs!!

## Changes

Comparing to [the original version](https://github.com/chaoshxxu/virtual-judge/tree/9dc0be82ed7e05dc17b7f042a935bf3db8435ce4) of virtual-judge source code, there are the following changes in this version:

- Fix the support for ACdream, Aizu, Codeforces, NBUT, POJ, URAL and UVA OJs
- Add support for CodeforcesGym OJ
- Add support for MathJax
- Update language information of code submission
- Automatically download the PDF files of problem description to server so that clients can view them without the Internet access
- Other small changes


## How to deploy

- Click [here](https://github.com/chaoshxxu/virtual-judge/wiki/How-to-deploy-your-own-Virtual-Judge) to visit the official wiki page and follow the instructions.
Notice that since this version of virtual-judge add support for CodeforcesGym, you should add an OJ called "CFGym" in `remote_accounts.json` like [this](https://gist.github.com/hnshhslsh/eb79be0d2a436a16cec77ff2f8552f7e) to provide accounts for it, or it may fail to start.

## 修改说明

与 [原版](https://github.com/chaoshxxu/virtual-judge/tree/9dc0be82ed7e05dc17b7f042a935bf3db8435ce4) virtual-judge 源码相比, 该本版有以下修改：
- 修复对ACdream, Aizu, Codeforces, NBUT, POJ, URAL 和 UVA 这些OJ的支持
- 添加对CodeforcesGym该OJ的支持
- 添加对MathJax的支持（用于显示公式，默认使用互联网上的版本，需要离线请自行下载并更新`script.min.js`中的路径）
- 更新代码提交时可选的语言
- 自动下载题目描述的PDF文件到服务器，以便客户端在没有互联网的情况下浏览（题目使用PDF文档显示时把PDF文件离线到服务器上，客户端从VJ服务器上显示PDF而不是原始OJ服务器，以供在只有服务器有互联网访问，客户端只有跟服务器的内网连接时可以正常使用，这是我本人的部署环境，一些代码会为此为目标环境而编写）
- 其它小变化

## 部署说明
- 点击 [这里](https://github.com/chaoshxxu/virtual-judge/wiki/How-to-deploy-your-own-Virtual-Judge) 访问官方wiki页面按照指令进行安装。
注意因为本版virtual-judge添加了对CodeforcesGym的支持,所以你应该在`remote_accounts.json`文件中添加一个叫做CFGym的OJ，像[这样](https://gist.github.com/hnshhslsh/eb79be0d2a436a16cec77ff2f8552f7e)，来给这个OJ提供账号(可以把CF的直接复制一遍)，否则VJ可能无法启动。
