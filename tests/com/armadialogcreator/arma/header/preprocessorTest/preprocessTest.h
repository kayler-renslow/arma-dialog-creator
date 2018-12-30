#define HELLO 3
#define STRINGIFY(s) #s
class clas{
    id=HELLO;
};
#ifdef HELLO
#undef HELLO
#endif

class clas2{
    name=STRINGIFY(myName);
};


