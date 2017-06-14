**Added:**
* 

**Changed:**
* 

**Fixed:**
* major performance hit while running ExpressionInterpeter tests
    * added method to shutdown internal thread pool

**Notes:**
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**
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
* June 12: we need tests for definedProperties(), eventProperties(), etc
* June 12: for changelg optimization, do not have a optimizer class. have the change registrar optimize the changes
    * if the last change was the same type and the time between the changes is < 300ms, then modify the previous change
* June 13: ControlPropertiesEditorPane doesn't properly initialize inheritted/overridden properties (inherited properties have option to be inherited)
* June 13: We need to not save temporary properties when saving project
* June 13: instead of storing documentation in the ControlProperties, have a dedicated class to fetch the documentation
  and store it there. We then can fetch the documentation when a ControlPropertiesEditorPopup is opened.
  We can also have a class that caches the documentation in the case that multiple controls of the same type are opened.
    * We could just use ResourceBundle, have a method in the Documentation class that gets documentation for a property
      with the given locale. Also, we should just store the .properties file as such: ControlPropertyLookup.name=documentation
      
    