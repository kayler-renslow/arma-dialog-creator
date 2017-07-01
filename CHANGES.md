**Added:**
* popup that appears when control prop editor config tried to inherit a property and nothing was inherited
* merge ControlClass.extendControlClass(null) with the override Changelog updates so that we can undo the extend
      change and also reinherit all the properties

**Changed:**
* 

**Fixed:**
* setting HintRep's parent class to null doesn't properly update the editor pane

**Notes:**
* May 31: we should have default value providers in one xml file for every control.
    * this will make xml files shorter (load faster for default value provider), and modularize things a lil
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
* June 23:
    - Header: we need tests for +=
* BUG: select many things in tree view and drag into folder
    * what if we remove the dragging crap and just have buttons that do the same thing?
* June 28: we should probably have an option to enable/disable sticky select, otherwise people will think its a bug
