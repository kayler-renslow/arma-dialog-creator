**Added:**
* tests that check for localization problems
* run configuration for pretending to run the application in a different default locale (French) 

**Changed:**
* 

**Fixed:**
* wrapper .exe java version issue (https://github.com/kayler-renslow/arma-dialog-creator/issues/9)
* comma vs period in different countries (https://github.com/kayler-renslow/arma-dialog-creator/issues/8)

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


**XSlider's border isn't correct at launch for "Lots of Controls project" (it's black instead of the texture color)**