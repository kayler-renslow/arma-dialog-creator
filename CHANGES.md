**Added:**
* 

**Changed:**
* canvas editor now has a different, more subtle way of showing what controls are selected

**Fixed:**
* when duplicating a bg control, the control was placed in controls rather than bg controls
* application would freeze when creating new project
* internal error report when the file path for an image can't be relativized towards the project export directory
    * this can happen when using different drive letters.
    * for now, if this error occurs, we just ignore it and write the path stored in the SerializableValue
* re-double clicking a contorl in editor wouldn't show the popup editor
* double click could be trigged even when first click wasn't targeting a control and the second click was
* some render issues with blending an image that has transparency over one that doesnt (was making the non transparent one disappear)

**Notes:**
* May 31: we should have default value providers in one xml file for every control.
    * this will make xml files shorter (load faster for default value provider), and modularize things a lil
* June 11: exporter doesn't write any of the custom control classes
    * should we have an additional export place for that? Like export all of them to a different file.
      We'll need to consider macro merges if we separate into many files since custom class can have macros.
    * what if we made all custom control classes be shared across the workspace?
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
    * undoing raw value through changelog works, but editor container doesn't update
    * we should be able to set default values specifically for properties inside nested classes
        * ShortcutPos.w should not be equal to the normal w
* July 7: we should support inheriting nested classes.
    * be sure to update HeaderToProject
    * HeaderToProject is creating 2 of RscText for VehicleShopMenu