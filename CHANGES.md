**Added:**
* Convert value dialog

**Changed:**
*  

**Fixed:**
* 

**Notes:**
* May 31: we should have default value providers in one xml file for every control.
    * this will make xml files shorter (load faster for default value provider), and modularize things a lil
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
* June 13: ControlPropertiesEditorPane doesn't properly initialize inherited/overridden properties options (inherited properties have option to be inherited)
* June 13: We need to not save temporary properties when saving project
