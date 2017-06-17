**Added:**
* 

**Changed:**
* better merge functionality for Changelog updates that involve position updates to a control
    * an issue was happening that not all relevant position related properties (x,y,w,h) were
    being stored so the undo/redo actions weren't undo/redo to the correct old state

**Fixed:**
* 

**Notes:**
* May 31: we should have default value providers in one xml file for every control.
    * this will make xml files shorter (load faster for default value provider), and modularize things a lil
* May 31: for converting a value into another, have a convert fail dialog that will have an editor that will have the ControlPropertyEditor
    for the converted value and the convert from value. The user can then edit either to get the result they want
* June 2: editor tree view: we don't need to iterate through the entire tree to find the TreeItem since it is placed in the dataContext of the control
    * remember that the controls can move from one tree to another
    * there could be a data race (not the threaded kind) between the 2 trees on the cached tree values
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
* June 12: we need tests for definedProperties(), eventProperties(), etc
* June 13: ControlPropertiesEditorPane doesn't properly initialize inheritted/overridden properties (inherited properties have option to be inherited)
* June 13: We need to not save temporary properties when saving project
    