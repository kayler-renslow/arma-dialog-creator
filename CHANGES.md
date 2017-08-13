**Added:**
* JavaFX check to launcher

**Changed:**
* updated help urls and buttons

**Fixed:**
* 

**Notes:**
* June 23:
    - Header: we need tests for +=
* June 28: we should probably have an option to enable/disable sticky select, otherwise people will think its a bug
* July 4:
    * button can use style 48, but the text can only be a texture
* July 5: have tests that make sure changing a property from required to optional and vice versa doesn't break project loading and properly loads
    * need tests for changing nested classes from optional to required as well
    * undoing raw value through changelog works, but editor container doesn't update
* July 7: we should support inheriting nested classes (should be option in control property config popup).
    * be sure to update HeaderToProject
* We need to be able to REMOVE macros and custom control classes