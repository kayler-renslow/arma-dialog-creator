**Added:**
* the SQF value/cprop editor

**Changed:**
* made the raw value outline in editor container not appear when the property has options

**Fixed:**
* when changing projects, the preview window would have the wrong instance of canvas view
    * just made the popup window appear with a new instance each time
* background color of button control wasn't being initialized to what it actually was

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
* July 3: inherited values aren't always setting the editor's value correctly. Sometimes, the value is present but the editor isn't displaying that value
* July 4: file paths should be relative to the workspace, not the project!
    * ../img/pice.jpg should be img/pice.jpg
    * we need to do it this way because in mission files, the directory of the description.ext is the starting directory