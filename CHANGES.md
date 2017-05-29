**Added:**
* 

**Changed:**
* made expression interpret return future
* made expression interpreter use thread pool

**Fixed:**
* 

**Notes:**
* may 9 3:37 : project xml loader can find classes to extend, but it isn't working for imported projects
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**
* may 24 : we need to support __EXEC
    * we need to make sure that values created in __EXEC are passed between multiple files (don't destroy them after each file preprocess action)
* may 28: if we wanted to support case insensitivity for commands and variables for expression interpreter, convert the entire file to lowercase
    (except strings) and make the grammar match the lowercase commands 