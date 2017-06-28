**Added:**
* "new folder" to create menu in main menu

**Changed:**
* 

**Fixed:**
* moving folders that have controls didn't change the controls' order in the display

**Notes:**
* May 31: we should have default value providers in one xml file for every control.
    * this will make xml files shorter (load faster for default value provider), and modularize things a lil
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
* June 23:
    - Header: we need tests for +=
* June 26: can't edit nested classes in editor pane. need to implement the "edit" menu item action
        * setting HintRep's parent class to null doens't properly update the editor pane
        * remove the nested class from static later because its for testing