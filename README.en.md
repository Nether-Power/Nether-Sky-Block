# 下界空岛 | Nether Sky Block

[![Minecraft](https://img.shields.io/badge/Minecraft-1.19-66ccff)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Fabric-0.14.8-fcd217)](https://fabricmc.net/use/installer/)
[![Fabric API](https://img.shields.io/badge/FabricAPI-0.57.0+1.19-b2cf87)](https://modrinth.com/mod/fabric-api)
[![Version](https://img.shields.io/badge/Version-1.1.0-9787c5)](https://github.com/Nether-Power/Nether-Sky-Block/releases/latest)
[![Discord](https://img.shields.io/badge/Discord-nEt5QTRYTN-c6574b)](https://discord.gg/nEt5QTRYTN)

[![Build Mod](https://github.com/Nether-Power/Nether-Sky-Block/actions/workflows/build.yml/badge.svg)](https://github.com/Nether-Power/Nether-Sky-Block/actions/workflows/build.yml/badge.svg)
[![license](https://img.shields.io/github/license/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/blob/main/LICENSE)
[![pull request](https://img.shields.io/github/issues-pr/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/pulls)
[![fork](https://img.shields.io/github/forks/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/network/members)
[![star](https://img.shields.io/github/stars/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/stargazers)
[![issue](https://img.shields.io/github/issues/Nether-Power/Nether-Sky-Block)](https://github.com/Nether-Power/Nether-Sky-Block/issues)
[![Java](https://img.shields.io/badge/Java-17-yellow)](https://docs.microsoft.com/java/openjdk/download)

## [中文](README.md) | English

## Install

### Client

* Download and install `Minecraft 1.19` , install `Fabric 0.14.8` .
* From [Release](https://github.com/Nether-Power/Nether-Sky-Block/releases/latest) download the last release `mod`
  and `datapack` , then put the `mod` to `mods` directory.
* Download `Fabric API 0.57.0+1.19`, then put it to `mods` directory.
* When you create new world, Set `World Type` to `SkyLand` in `More World Options` , and add the DataPack.
* After entering the world, type `/skyland gamerule true` command to enable Nether-Sky-Block settings.

### Server

* Download and install `Minecraft Server 1.19` , install `Fabric 0.14.8` .
* From [Release](https://github.com/Nether-Power/Nether-Sky-Block/releases/latest) download the last release `mod`
  and `datapack` , then put the `mod` to `mods` directory and put the `datapack` to `world/datapacks` directory.
* Download `Fabric API 0.57.0+1.19`, then put it to `mods` directory.
* Set `level-type` to `skyland:skyland` in `server.properties` .
* After starting the server, type `/skyland gamerule true` command in console to enable Nether-Sky-Block settings.

## Features

### MOD

- Set three dimensions to void
- Replace overworld with nether
- Removed the experimental world warning
- All features of one key switch
    - `/skyland gamerule <true|false>`
- Ignore the influence of LC value on monster spawn efficiency
    - `/gamerule qnmdLC true`
- Increase the spawning probability of Wandering Trader merchants by 10 times
    - `/gamerule chieftainMode true`
- When creating a map, an initial platform will be created on the y-axis 64(A crimson nylium, a warped nylium, crimson
  fungus is planted above the crimson nylium, and an apple in item frame is hung above the warped nylium)
- When the first player first entered the world, he was given 32 bone meal
- Zombie villager is generated when zombified piglin and zombie trigger zombie reinforcements
    - `/gamerule villagerReinforcements true`
- The nether will generate patrol
    - `/gamerule netherPatrol true`
- Soul Sand Valley spawn Witch (extremely low probability)
- Bastion Remnant spawn Pillager
- Basalt Deltas spawn Slime
- The lava beside the Water Cauldron (full of water) is converted into obsidian and consumes all the water in the Water
  Cauldron
    - `/gamerule waterCauldron true`
- Enable the nether biome to spawn Wandering Trader riding Strider（When the highest point of a player's 40 blocks radius
  is lava, there is a 10% probability of spawn every 20min. When the first spawn fails, the probability of subsequent
  spawn increases to 30%）
    - `/gamerule netherTrader true`
- Delete the Emerald for Pumpkin Pie transaction when the Farmer Villager apprentice level is removed
- When you kill the resurrected Ender Dragon, you will refresh the Shulker above the Exit portal
    - `/gamerule killDragonSpawnShulker true`
- When you kill the Ender Dragon, you will drop an Elytra with the remaining 1% durability
    - `/gamerule killDragonDropElytra true`
- Generate the End Portal in the nether
    - The world will generate `8` End Portal frames with a radius of `1024` centered on `spawnpoint`
- Anvil Handle
    - `/gamerule anvilHandle true`
    - Wash the basalt that consumes all water into Dripstone Block in the Water Cauldron (full of water)
    - Press a Moss Block and a Dirt into a Grass Block
- The Raid can be triggered in the nether
- Items no longer scatter when you death
- Use bone meal on the side of the coral block to make it grow a coral fan, and use bone meal on the top to grow coral
- Dig powder snow to get snowballs
- The Snow Golem standing on the water will turn the water into ice
    - `/gamerule iceGolem true`
- Sneak in composer can produce bone meal
    - `/gamerule ComposterBoneMeal true`
- The following animals will be spawn on the Grass Block of the Nether Waste
  - Cow
  - Sheep
  - Llama
  - Wolf
  - Horse
  - Donkey
- The FoodLevel before death will be remembered after death
  - `/gamerule memoryFoodLevel true`
- Minimum FoodLevel after death and resurrection (effective after the rule `memoryFoodLevel` is turned on, the default is `4`)
  - `/gamerule respawnMinFoodLevel 4`
- The Wandering Trader deals in additional items
- You can containing powder snow in Basalt Deltas

| **Item**    | **Count** | **Price** | **Transactions Count** |
|-------------|-----------|-----------|------------------------|
| Turtle Egg  | 1         | 16        | 4                      |
| Tall Flower | 1         | 8         | 8                      |
| Cocoa Bean  | 1         | 16        | 8                      |
| Bamboo      | 1         | 16        | 8                      |
| Nether Wart | 1         | 32        | 4                      |

### DATAPACK

- recipes
    - Blast furnace recipe of replacing Smooth Stone with Obsidian
    - Burn the Nether Wart Block in the blast furnace to obtain the Nether Wart Block
    - Use the stone cutter to cut the dripstone block to obtain dripstone
- Modify piglin transaction
    - Delete Fire Resistance Potion trading
    - Increase Blackstone trading probability
    - Reduce Water Bottle trading probability
    - Added Glowstone Dust trading
    - Delete Ender Pearl trading
- When owning a Hero of the Village Status Effect
    - the Mason Villager has a probability to throw Lava Bucket (weight 3)
    - the Toolsmith Villager has a probability to throw Netherite Scrap (weight 3)

## Contributor

* Planner
    * [朽白](https://space.bilibili.com/178682437)
* Programmer
    * [古镇天Gugle](https://space.bilibili.com/19822751)
    * [Cjsah](https://space.bilibili.com/19170004)
    * [DancingSnow](https://space.bilibili.com/302121711)
    * [Huaji_MUR233](https://space.bilibili.com/434118309)

## Powered by

* [AnvilCraft-SkyLandMod](https://github.com/Dubhe-Studio/AnvilCraft-SkyLandMod)
