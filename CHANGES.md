**Added:**
* Code value, if, true, false, array value, and select to expression interpreter

**Changed:**
* color of numbers in expression evaluator popup

**Fixed:**
*  

**Notes:**
* may 9 3:37 : project xml loader can find classes to extend, but it isn't working for imported projects
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**
* may 24 : we need to support __EXEC
    * we need to make sure that values created in __EXEC are passed between multiple files (don't destroy them after each file preprocess action)
* may 24: we need concat of arrays, subtract of arrays, ==, !=, <= <, >=, >, % ^