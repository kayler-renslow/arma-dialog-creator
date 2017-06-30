**Added:**
* binary logical operators like || and && for expression
    * added tests for these as well

**Changed:**
* made Notifications automatically close the handler thread after a while of no notifications appearing

**Fixed:**
* Preprocessor didn't have its own UnaryCommandValueProvider instance in its env

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
* BUG: select many things in tree view and drag into folder
    * what if we remove the dragging crap and just have buttons that do the same thing?
* June 28: we should probably have an option to enable/disable sticky select, otherwise people will think its a bug