//The passed tokens are used to define the sounds' class and sound name, as well as its path and subtitle:
#define MAKE_SOUND(type,word) \
  class Sound_##type##_##word {  \
    name = "";  \
    sound[] = {"\sounds\type\word.ogg", 5, 1}; \
    titles[] = {0, "type says 'word'"}; \
  };

// CfgSounds is expanded with  8 custom sounds, defined via the macro above
class CfgSounds {
  MAKE_SOUND(Man,one);
  MAKE_SOUND(Man,two);
};
