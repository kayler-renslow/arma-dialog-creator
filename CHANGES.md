**Added:**
* more ControlClass tests

**Changed:**
* 

**Fixed:**
* 

**Notes:**
* may 9 3:37 : project xml loader can find classes to extend, but it isn't working for imported projects
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**
* May 31: HeaderToProject, createCustomControlClass is working, however, the wrong ControlPropertyLookup instances are being used.
    * As soon the control extends the custom control class, it is trying to find a lookup to inherit from that isn't there.
    * We need to locate all lookups with the same name and combine them into one lookup intelligently
* May 31: we should have documentation for control's properties and default value providers in one xml file for every control.
    * this will make documentation easier, xml files shorter (load faster for default value provider), and modularize things a lil
    * for the documentation, we should have documentation for each property by id and not store it in ControlPropertyLookup
    * MergePropertyException for attempting to merge a property and it failing
* May 31: for converting a value into another, have a convert fail dialog that will have an editor that will have the ControlPropertyEditor
    for the converted value and the convert from value. The user can then edit either to get the result they want
* June 2: editor tree view: we don't need to iterate through the entire tree to find the TreeItem since it is placed in the dataContext of the control
    * remember that the controls can move from one tree to another
    * there could be a data race between the 2 trees
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
* June 12: when a temporary property is created for inheritance (inherit property that doesn't exist in ControlClass),
    we need to mark it custom in the save. If we don't mark the property temporary, then when the class loses the inherited
    property, it won't think its temporary.