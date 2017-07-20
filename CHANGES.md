**Added:**
* Edit control renderer
* Edit control data creator

**Changed:**
* static can now support multi line
* in canvas editor: when a control is no longer visible, it is removed from the selection
* removed auto submit from color array editor popup
* color array editor  popup now has a transparency grid to help show there is transparency

**Fixed:**
* sometimes the option value editor was becoming too big and wasn't scaling according to the editor container
* non multi lined text wasn't rendering in correct spot upon renderer init
* double clicking a control that was selected but the mouse over control wasn't matched with the selected control resulted in the editor popup not showing

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