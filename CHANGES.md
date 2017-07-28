**Added:**
* listbox default values

**Changed:**
*  

**Fixed:**
* 

**Notes:**
* June 23:
    - Header: we need tests for +=
* BUG: select many things in tree view and drag into folder
    * what if we remove the dragging crap and just have buttons that do the same thing?
* June 28: we should probably have an option to enable/disable sticky select, otherwise people will think its a bug
* July 4:
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