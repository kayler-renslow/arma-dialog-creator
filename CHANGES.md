**Added:**
* 

**Changed:**
* updated ControlClass documentation
* Expression interpreter Env now supports insertion and deletion of identifier values

**Fixed:**
* nested macro references in macro bodies wasn't recursively checking for references 

**Notes:**
* may 9 3:37 : project xml loader can find classes to extend, but it isn't working for imported projects
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**
* may 24 : we need to support __EXEC