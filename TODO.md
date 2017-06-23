Ideas
-----------------------------------
* for when specifying properties of controls, list the required ones and have a button that says "add optional property" and it lists the optional ones.
    * There can also be a button that says "add custom property" and it will prompt to fill out the name, type, and text(s)
* check access property for controls and see if properties can be inherited and such https://community.bistudio.com/wiki/Dialog_Control#Dialogs
* have a color theme maker so that you don't need to constantly have to set colors to a macro. Maybe even have a big default archive for all attributes? Maybe just place them inside the lookup thingy?
* go back through the lookup table and remove redundant entries after you figure out the requirements and options for controls
* for preview, have a control state toggler. Also, for action on buttons, have popup window show with action text.
* add check to see if safeZone should be applied to control(s). This is checked by seeing if x,y,w,h properties are < 0 or >  1
* for new control, it isn't proper to say "Required" since the new control can allow for partial implementation
* have help button implementation for all popups
* allow for setting the current pixel location values for controls (then convert the x,y,w,h to new safeZone values based off pixel values)
* maybe instead of hardcoding percentages for snap, we have expression evaluating (safeZoneW * .25 is 25% of canvas width, where 0.25 is 25% of viewport width)

General
-----------------------------------
* when loading keys from stringtable, check for improper key ids (missing str_ or duplicate ids) 
* stringtable editor: reload and save to file need error dialogs

Bugs
-----------------------------------
* select many things in tree view and drag into folder
* extendControlClass doesn't append inherited nested classes