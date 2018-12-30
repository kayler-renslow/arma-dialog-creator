#define ERR_MSG(MSG) msg=MSG
class DevStuff
{
	class Errors
	{
		class InvalidArgument //an argument that was passed was a wrong type or was not allowed
		{
			ERR_MSG("Invalid Argument");
		};
		class IllegalState //a script's execution was fine until it reached a certain condition
		{
			ERR_MSG("Illegal State");
		};
		class ClassNotFound //tried fetching a class from a config file that did not exist
		{
			ERR_MSG("Class Not Found");
		};

	};
};
