**Added:**
* exitWith tests
* support for __EXEC in preprocessor
* more preprocessor and expr interpreter tests

**Changed:**
* made preprocessor not error when finding unknown macro
* preprocessor now writes the results to a file instead of storing in a list.
    * the file is the processFile+".preprocessed"

**Fixed:**
* Interpreter.terminateAll() didn't actually work
    * added test for terminateAll

**Notes:**
* may 9 3:37 : project xml loader can find classes to extend, but it isn't working for imported projects
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**
* may 28: if we wanted to support case insensitivity for commands and variables for expression interpreter, convert the entire file to lowercase
    (except strings) and make the grammar match the lowercase commands
* may 29 :support str command