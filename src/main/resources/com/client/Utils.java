package com.client;

import java.util.SplittableRandom;

public class Utils {

    private static final SplittableRandom RANDOM = new SplittableRandom();

    public static boolean canLoadObjectModel(final int id) {
        return id != 31828 && (id < 33054 || id > 33057) && id != 34115 && id != 123057 && id != 123059 && id != 123445 && id != 123453 && id != 123454 && id != 123459 && id != 123044 && id != 123045 && id != 123443 && id != 123449 && id != 123451 && id != 123455 && id != 123740 && id != 33060 && id != 32326 && id != 33061 && id != 38421 && id != 38422 && id != 41452 && id != 41454 && id != 47363 && id != 79103 && id != 79104 && id != 85060 && id != 123370 && id != 123371 && id != 123373 && id != 123411 && id != 123413 && id != 123414 && id != 123415 && id != 123416 && id != 123417 && id != 123418 && id != 123419 && id != 123420 && id != 123422 && id != 123744 && id != 66969 && (id < 123650 || id > 123654) && id != 123726 && id != 123727 && id != 123729 && id != 122696 && id != 122727 && id != 122729 && id != 122731 && id != 122732 && id != 122728 && id != 115120 && id != 115127 && id != 2581 && id != 123372 && id != 114721 && id != 95685 && id != 95686 && id != 95688 && (id < 79623 || id > 79633) && id != 98228 && id != 86971 && id != 86972 && id != 115222 && id != 99696 && id != 88081 && id != 84833 && id != 78692 && id != 74868 && id != 95555 && id != 75524 && (id < 115167 || id > 115169) && id != 115206 && id != 119786 && id != 112238 && id != 114977 && id != 114842 && id != 115423 && id != 122309 && id != 122314 && id != 122338 && id != 122339 && id != 117077 && id != 116075 && id != 116077 && id != 116639 && id != 114675 && id != 114676 && id != 117332 && id != 24683 && id != 82391 && id != 82392 && id != 82591 && id != 89785 && id != 82688 && id != 68983 && id != 98192 && id != 103499 && id != 101336 && id != 82687 && id != 110640 && id != 72701 && id != 72884 && id != 20348 && id != 66496 && id != 104353 && (id < 78195 || id > 78210) && id != 94124 && id != 93172 && id != 116115 && id != 116116 && id != 112425 && id != 101846 && id != 40407 && id != 77992 && id != 77997 && id != 78009 && id != 78223 && id != 78230 && id != 98380 && id != 90204 && id != 76966 && id != 116808 && id != 122873 && id != 122874 && id != 122875 && id != 122986 && id != 122984 && id != 122014 && id != 121993 && id != 115129 && id != 121995 && id != 122985;
    }

    public static int getRandom(final int min, final int max) {
        return Utils.RANDOM.nextInt(min, (max == min) ? (max + 1) : max);
    }

    public static int getAreaSpriteId(int spriteId) {
        if (spriteId == 1289) {
            return 22440;
        } else if (spriteId == 1361) {
            return 22458;
        } else if (spriteId == 1362) {
            return 22362;
        } else if (spriteId == 1363) {
            return 22425;
        } else if (spriteId == 1364) {
            return 22365;
        } else if (spriteId == 1365) {
            return 22363;
        } else if (spriteId == 1366) {
            return 22391;
        } else if (spriteId == 1367) {
            return 22436;
        } else if (spriteId == 12241) {
            return 22336;
        } else if (spriteId == 12242) {
            return 22338;
        } else if (spriteId == 6540) {
            return 22445;
        } else if (spriteId == 6541) {
            return 22440;
        } else if (spriteId == 7910) {
            return 22441;
        } else if (spriteId == 6542) {
            return 22442;
        } else if (spriteId == 511) {
            return 511; // Change later -> player yellow dot
        } else if (spriteId == 6543) {
            return 22444;
        } else if (spriteId == 132) {
            return 132; // Prayer icon
        } else if (spriteId == 133) {
            return 133; // Prayer icon
        } else if (spriteId == 1353) {
            return 22462;
        } else if (spriteId == 510) {
            return 510; // Change later -> drop red dot
        } else if (spriteId == 6544) {
            return 22443;
        } else if (spriteId == 6976) {
            return 6976; // Idk what is that -> clan related
        } else if (spriteId == 13268) {
            return 22468;
        } else if (spriteId == 9600) {
            return 22339;
        } else if (spriteId == 9599) {
            return 22353;
        } else if (spriteId == 9598) {
            return 22351;
        } else if (spriteId == 9597) {
            return 22359;
        } else if (spriteId == 9596) {
            return 22355;
        } else if (spriteId == 9595) {
            return 22341; // Right farming icon?
        } else if (spriteId == 9594) {
            return 22349;
        } else if (spriteId == 9593) {
            return 22343;
        } else if (spriteId == 2178) {
            return 33853;
        } else if (spriteId == 9592) {
            return 22344;
        } else if (spriteId == 9591) {
            return 22357;
        } else if (spriteId == 9590) {
            return 22347;
        } else if (spriteId == 9589) {
            return 22356;
        } else if (spriteId == 9588) {
            return 22350;
        } else if (spriteId == 9587) {
            return 22348;
        } else if (spriteId == 9586) {
            return 22352;
        } else if (spriteId == 9585) {
            return 22340;
        } else if (spriteId == 9584) {
            return 22342;
        } else if (spriteId == 9583) {
            return 22358;
        } else if (spriteId == 9582) {
            return 22345;
        } else if (spriteId == 9581) {
            return 22346;
        } else if (spriteId == 1323) {
            return 22416;
        } else if (spriteId == 10141) {
            return 22460; // Lodestone icon - maybe change that one later
        } else if (spriteId == 1292) {
            return 22411;
        } else if (spriteId == 1339) {
            return 22463; // Transportation icon - maybe change that one layer
        } else if (spriteId == 1283) {
            return 22360;
        } else if (spriteId == 1284) {
            return 22365;
        } else if (spriteId == 1285) {
            return 22363;
        } else if (spriteId == 1286) {
            return 22367;
        } else if (spriteId == 1287) {
            return 22370;
        } else if (spriteId == 1288) {
            return 22458;
        } else if (spriteId == 1290) {
            return 22376;
        } else if (spriteId == 1291) {
            return 22381;
        } else if (spriteId == 1293) {
            return 22408;
        } else if (spriteId == 1295) {
            return 22467; // Dungeon entrance icon -> change that later
        } else if (spriteId == 1296) {
            return 22369;
        } else if (spriteId == 1297) {
            return 22373;
        } else if (spriteId == 1298) {
            return 22374;
        } else if (spriteId == 1299) {
            return 22365;
        } else if (spriteId == 1300) {
            return 22361;
        } else if (spriteId == 1301) {
            return 22371;
        } else if (spriteId == 1302) {
            return 22405;
        } else if (spriteId == 9386) {
            return 22436;
        } else if (spriteId == 1303) {
            return 22362;
        } else if (spriteId == 1304) {
            return 22380;
        } else if (spriteId == 1305) {
            return 22378;
        } else if (spriteId == 1306) {
            return 22384;
        } else if (spriteId == 1307) {
            return 22397;
        } else if (spriteId == 1308) {
            return 22383;
        } else if (spriteId == 1309) {
            return 22430;
        } else if (spriteId == 1310) {
            return 22389;
        } else if (spriteId == 1311) {
            return 22406;
        } else if (spriteId == 1312) {
            return 22398;
        } else if (spriteId == 1313) {
            return 22392;
        } else if (spriteId == 1314) {
            return 22395;
        } else if (spriteId == 1315) {
            return 22368;
        } else if (spriteId == 1316) {
            return 22419;
        } else if (spriteId == 1317) {
            return 22431;
        } else if (spriteId == 1318) {
            return 22415;
        } else if (spriteId == 1319) {
            return 22391; // Food shop
        } else if (spriteId == 1320) {
            return 22388; // Cookery shop
        } else if (spriteId == 1321) {
            return 22441; // Minigame icon -> change that later
        } else if (spriteId == 1322) {
            return 22433; // Water source
        } else if (spriteId == 1324) {
            return 22375; // Plateskirt shop
        } else if (spriteId == 1325) {
            return 22412; // Pottery wheel
        } else if (spriteId == 1326) {
            return 22417; // Windmill
        } else if (spriteId == 1327) {
            return 22381; // Mining shop
        } else if (spriteId == 1328) {
            return 22372; // Chainmail shop
        } else if (spriteId == 1329) {
            return 22379; // Silver shop
        } else if (spriteId == 1330) {
            return 22390; // Fur trader
        } else if (spriteId == 1331) {
            return 22394; // Spice shop
        } else if (spriteId == 1333) {
            return 22393; // Vegetable store
        } else if (spriteId == 1334) {
            return 22357; // Slayer master
        } else if (spriteId == 16363) {
            return 22420; // Not identified -> slayer tutor / contract
        } else if (spriteId == 1337) {
            return 22424; // Makeover mage
        } else if (spriteId == 1338) {
            return 22413; // Guide
        } else if (spriteId == 1336) {
            return 22435; // Farming patch
        } else if (spriteId == 1341) {
            return 22386; // Farming shop
        } else if (spriteId == 1342) {
            return 22414; // Loom
        } else if (spriteId == 1343) {
            return 22396; // Brewery
        } else if (spriteId == 1344) {
            return 22409; // Dairy churn
        } else if (spriteId == 1345) {
            return 22434; // Stagnant water source
        } else if (spriteId == 1340) {
            return 22461; // POH Portal - change that later
        } else if (spriteId == 1350) {
            return 22404; // Estate Agent
        } else if (spriteId == 1351) {
            return 22410; // Sawmill
        } else if (spriteId == 1352) {
            return 1352; // Crafting thing -> change that later
        } else if (spriteId == 1354) {
            return 22425; // Woodcutting stump
        } else if (spriteId == 1355) {
            return 1355; // Holiday event icon -> change that later
        } else if (spriteId == 1356) {
            return 22438; // Sandpit
        } else if (spriteId == 1357) {
            return 1357; // Task master - change that later
        } else if (spriteId == 1358) {
            return 22377; // Vambrace exchange
        } else if (spriteId == 1359) {
            return 22423; // Summoning obelisk
        } else if (spriteId == 1376) {
            return 22423; // Mini obelisk
        } else if (spriteId == 1360) {
            return 22364; // Summoning store
        } else if (spriteId == 1375) {
            return 22401; // Pet shop
        } else if (spriteId == 1377) {
            return 1377; // Distractions/Diversions - change that later
        } else if (spriteId == 1463) {
            return 22421; // Fired up beacon
        } else if (spriteId == 1464) {
            return 22407; // Music
        } else if (spriteId == 1778) {
            return 22400; // Photo booth
        } else if (spriteId == 1783) {
            return 22403; // Tools for games
        } else if (spriteId == 1784) {
            return 1784; // Seasonal activity - change that later
        } else if (spriteId == 6305) {
            return 22402; // Loyalty Rewards Shop
        } else if (spriteId == 7911) {
            return 22387; // Gravestone exchange
        } else if (spriteId == 17193) {
            return 33948; // Idk what is that one - change that later
        } else if (spriteId == 20341) {
            return 22437; // Wisp colony
        } else if (spriteId == 21711) {
            return 21711; // Inventor's workbench - change that later
        } else if (spriteId == 4374) {
            return 4374; // What is that one? - change that later
        } else if (spriteId == 4375) {
            return 33952; // What is that one? - change that later
        } else if (spriteId == 31172) {
            return 31172; // Lamp trader
        } else if (spriteId == 31170) {
            return 31170; // Corrupt altar - change that later
        } else if (spriteId == 31171) {
            return 31171; // Bonfire - change that later
        } else if (spriteId == 31173) {
            return 31173; // What is that one? - change that later
        } else if (spriteId == 31174) {
            return 31174; // What is that one? - change that later
        } else if (spriteId == 830) {
            return 33958; // What is that one? - change that later
        } else if (spriteId == 828) {
            return 33959; // What is that one? - change that later
        } else if (spriteId == 829) {
            return 33960; // What is that one? - change that later
        } else if (spriteId == 827) {
            return 33961; // What is that one? - change that later
        } else if (spriteId == 2179) {
            return 33962; // What is that one? - change that later
        } else if (spriteId == 1348) {
            return 33963; // Hunter store
        } else if (spriteId == 1346) {
            return 22343; // Hunter training
        } else if (spriteId == 11315) {
            return 33965; // What is that one? - change that later
        } else if (spriteId == 11317) {
            return 33966; // What is that one? - change that later
        } else {
            return spriteId == 9937 ? 9937 : spriteId;
        }
    }

}
