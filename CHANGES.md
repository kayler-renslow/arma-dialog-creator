**Added:**
* ability to set focused control in preview window

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
* June 23:
    - Header: we need tests for +=
* BUG: select many things in tree view and drag into folder
    * what if we remove the dragging crap and just have buttons that do the same thing?
* June 28: we should probably have an option to enable/disable sticky select, otherwise people will think its a bug
* July 3: inherited values aren't always setting the editor's value correctly. Sometimes, the value is present but the editor isn't displaying that value
* July 4:
    * file paths should be relative to the workspace, not the project!
        * ../img/pice.jpg should be img/pice.jpg
        * we need to do it this way because in mission files, the directory of the description.ext is the starting directory
    * header to project change the file paths to absolute ones for things like images to work
    * button can use style 48, but the text can only be a texture
* July 5: have tests that make sure changing a property from required to optional and vice versa doesn't break project loading and properly loads
    * need tests for changing nested classes from optional to required as well
    * need texture parser tests