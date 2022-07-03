# 下界空岛 | Nether Sky Block

[![Minecraft](https://img.shields.io/badge/Minecraft-1.19-66ccff)](https://www.minecraft.net/)
[![Minecraft](https://img.shields.io/badge/Fabric-0.14.8-fcd217)](https://fabricmc.net/use/installer/)
[![Minecraft](https://img.shields.io/badge/FabricAPI-0.57.0+1.19-b2cf87)](https://modrinth.com/mod/fabric-api)

[![Build Mod](https://github.com/Nether-Power/Nether-Sky-Block/actions/workflows/build.yml/badge.svg)](https://github.com/Nether-Power/Nether-Sky-Block/actions/workflows/build.yml/badge.svg)
[![license](https://img.shields.io/github/license/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/blob/main/LICENSE)
[![pull request](https://img.shields.io/github/issues-pr/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/pulls)
[![fork](https://img.shields.io/github/forks/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/network/members)
[![star](https://img.shields.io/github/stars/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/stargazers)
[![issue](https://img.shields.io/github/issues/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/issues)
[![Java](https://img.shields.io/badge/Java-17-yellow)](https://docs.microsoft.com/java/openjdk/download)

## 中文 | [English](README.en.md)

## Install

### Client

* 下载安装 `Minecraft 1.19`, 安装 `Fabric 0.14.8`
* 从 [Release](https://github.com/Nether-Power/Nether-Sky-Block/releases/latest) 下载最新发行版 `mod` 和 `datapack`, 并将 `mod` 放入 `mods` 文件夹
* 下载 `Fabric API 0.57.0+1.19`, 并放入 `mods` 文件夹
* 创建世界时, 在 `更多选项` 中将 `世界类型` 调为 `空岛` 并添加数据包
* 进入进入世界后，输入 `/skyland gamerule true` 命令打开地狱空岛的所需规则

### Server

* 下载安装 `Minecraft Server 1.19`, 安装 `Fabric 0.14.8`
* 从 [Release](https://github.com/Nether-Power/Nether-Sky-Block/releases/latest) 下载最新发行版 `mod` 和 `datapack`, 并分别放入 `mods` 与 `world/datapacks` 文件夹
* 下载 `Fabric API 0.57.0+1.19`, 并放入 `mods` 文件夹
* 创建世界时, 在 `server.properties` 中将 `level-type` 调为 `skyland:skyland`
* 开启服务器后，在控制台输入 `/skyland gamerule true` 命令打开地狱空岛的所需规则

## Features

### 模组

- 将三个维度设置为虚空
- 将主世界生成替换为下界
- 去除了实验性世界警告
- 一键开关所有特性
  - `/skyland gamerule <true|false>`
- 忽略LC值对刷怪效率的影响
  - `/gamerule qnmdLC true`
- 使流浪商人的刷新概率提升10倍
  - `/gamerule chieftainMode true`
- 创建地图时会在Y轴64创建一个初始平台(一个绯红菌岩、一个诡异菌岩、绯红菌岩上方种植绯红菌、诡异菌岩上方挂着放着苹果的展示框)
- 第一个玩家第一次进入世界时给予32个骨粉
- 僵尸猪灵和僵尸触发僵尸增援时生成僵尸村民
  - `/gamerule villagerReinforcements true`
- 下界会生成灾厄巡逻队
  - `/gamerule netherPatrol true`
- 灵魂沙峡谷刷新女巫（概率极低）
- 堡垒遗迹刷新劫掠者
- 玄武岩三角洲刷新史莱姆
- 储水炼药锅（满水）旁的熔岩转化为黑曜石并消耗储水炼药锅内的全部水
  - `/gamerule waterCauldron true`
- 使下界群系可以生成骑着赤足兽的游商（玩家40格半径最高点为熔岩时，每20min有10%概率生成，当第一次生成失败时后续生成概率提高至30%）
  - `/gamerule netherTrader true`
- 删除农民学徒等级时的绿宝石换南瓜派交易
- 击杀复活的末影龙时会在祭坛上方刷新潜影贝
  - `/gamerule killDragonSpawnShulker true`
- 击杀末影龙时会掉落一个剩余1%耐久的鞘翅
  - `/gamerule killDragonDropElytra true`
- 下界内生成末地传送门
  - 世界内会以 `出生点` 为中心, 半径 `1024` 生成 `8` 个末地门框架
- 铁砧处理
  - `/gamerule anvilHandle true`
  - 珊瑚扇在有水炼药锅里用铁砧砸一下会消耗一层水并增加多一个珊瑚扇
  - 用铁砧砸一下滴水石块将其变为滴水石锥
  - 在满水的储水炼药锅内消耗全部水玄武岩洗涤为滴水石块
- 在下界里可以触发袭击
- 雪傀儡站在水上面会将水变为冰
  - `/gamerule iceGolem true`
- 站在堆肥桶中下蹲，可以产生骨粉
  - `/gamerule composterBoneMeal true`
- 死亡后会记忆死亡前的饱食度
  - `/gamerule memoryFoodLevel true`
- 死亡复活后最低饱食度（在规则 `memoryFoodLevel` 开启后有效, 默认为 `4`）
  - `/gamerule respawnMinFoodLevel 4`
- 游商会交易除基础物品外的额外物品

| **物品** | **数量** | **价格** | **交易次数** |
|--------|--------|--------|----------|
| 雪球     | 2      | 1      | 8        |
| 珊瑚扇    | 1      | 64     | 4        |
| 海龟蛋    | 1      | 16     | 4        |
| 高花     | 1      | 8      | 8        |
| 可可豆    | 1      | 16     | 8        |
| 竹子     | 1      | 16     | 8        |
| 地狱疣    | 1      | 32     | 4        |

### 数据包

- 配方
  - 用黑曜石替换平滑石的高炉合成
  - 高炉里烧下界疣块以获取下界岩
  - 用 9 个灵魂沙合成一个灵魂土
- 修改猪灵交易
  - 删除抗火药水
  - 增加黑石交易概率
  - 降低水瓶交易概率
  - 增加萤石粉交易
  - 删除末影珍珠交易
- 拥有村庄英雄时
  - 皮匠会有概率丢岩浆桶(权重3)
  - 工具匠会有概率丢下界合金碎片(权重3)
  - 农民会有概率丢小麦种子

## Contributor

* 策划
  * [朽白](https://space.bilibili.com/178682437)
* 代码
  * [古镇天Gugle](https://space.bilibili.com/19822751)
  * [Cjsah](https://space.bilibili.com/19170004)
  * [DancingSnow](https://space.bilibili.com/302121711)
  * [Huaji_MUR233](https://space.bilibili.com/434118309)

## Powered by

* [AnvilCraft-SkyLandMod](https://github.com/Dubhe-Studio/AnvilCraft-SkyLandMod)
