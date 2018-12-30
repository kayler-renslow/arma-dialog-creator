author = "K-Town";
//loadScreen = "images\stslogo.jpg";
respawn = BASE;
class Header
{
	gameType = RPG;
};

wreckLimit = 5;

//the tab for the below include to to make sure that the tab doesn't prevent a macro match
    #include "largeTestRoot_configs.h"

class RscTitles;

#include "largeTestRoot_functions.h"
//this file in the directory is meant to test if the preprocessor correctly handles multiple directories
#include <dir/dirF.h>

class CfgSounds
{
	sounds[] = {};
	/*class SirenLong
	{
		name = "SirenLong";
		sound[] = {"\sounds\Siren_Long.ogg", 1.0, 1};
		titles[] = {};
	};*/
};

class CfgDebriefing
{
	/*class NotWhitelisted
	{
		title = "Mission Failed";
		subtitle = "You are not white-listed to use this slot";
		description = "You are not allowed to use this slot because you do not have the appropriate permissions, try another slot.";
		pictureBackground = "";
		picture = "";
		pictureColor[] = {0,0.3,0.6,1};
	};*/
	novalue=;//purposely doesn't have a value here
};
