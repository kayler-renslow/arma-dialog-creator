**Added:**
* 

**Changed:**
* removed a lot of unnecessary checks for termination of expression evaluator
    * evaluator runs much faster now

**Fixed:**
* 

**Notes:**
* may 9 3:37 : project xml loader can find classes to extend, but it isn't working for imported projects
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**
* may 28: if we wanted to support case insensitivity for commands and variables for expression interpreter, convert the entire file to lowercase
    (except strings) and make the grammar match the lowercase commands
* May 31: HeaderToProject, createCustomControlClass is working, however, the wrong ControlPropertyLookup instances are being used.
    * As soon the control extends the custom control class, it is trying to find a lookup to inherit from that isn't there.
    * We need to locate all lookups with the same name and combine them into one lookup intelligently
* May 31: we should have documentation for control's properties and default value providers in one xml file for every control.
    * this will make documentation easier, xml files shorter (load faster for default value provider), and modularize things a lil
    * for the documentation, we should have documentation for each property by id and not store it in ControlPropertyLookup
    * MergePropertyException for attempting to merge a property and it failing
* May 31: for converting a value into another, have a convert fail dialog that will have an editor that will have the ControlPropertyEditor
    for the converted value and the convert from value. The user can then edit either to get the result they want