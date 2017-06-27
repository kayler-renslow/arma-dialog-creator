**Added:**
* 

**Changed:**
* made inherited properties in editor pane have a blue tint 

**Fixed:**
* extendControlClass doesn't append inherited nested classes

**Notes:**
* May 31: we should have default value providers in one xml file for every control.
    * this will make xml files shorter (load faster for default value provider), and modularize things a lil
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
* June 13: We need to not save temporary properties when saving project
* June 23:
    - Header: we need tests for +=
    - Instead of allowing user to add/remove properties, we could have "header inserts" the user can place in the
      control's exported class. Besides, why would there ever need to be custom properties? Everything you should need
      should be in the spec provider
    