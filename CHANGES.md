**Added:**
* option to clear selection while a folder is selected
* "Toggle Raw Value" menu option in ControlPropertyEditorContainer that can toggle between Raw Value and other value

**Changed:**
* removed "custom data" from ControlProperty and instead added a new PropertyType called Raw
    * SerializableValue class: SVRaw 

**Fixed:**
* stringtable search field wasn't handling +language correctly
    * was matching keys that didn't have the specified language despite the '+' requiring matching keys that
      have said specified language

**Notes:**
* May 31: we should have default value providers in one xml file for every control.
    * this will make xml files shorter (load faster for default value provider), and modularize things a lil
* May 31: for converting a value into another, have a convert fail dialog that will have an editor that will have the ControlPropertyEditor
    for the converted value and the convert from value. The user can then edit either to get the result they want
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
* June 13: ControlPropertiesEditorPane doesn't properly initialize inheritted/overridden properties (inherited properties have option to be inherited)
* June 13: We need to not save temporary properties when saving project
