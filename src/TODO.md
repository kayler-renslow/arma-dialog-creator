Features
-----------------------------------
* for when specifying properties of controls, list the required ones and have a button that says "add optional property" and it lists the optional ones.
    * There can also be a button that says "add custom property" and it will prompt to fill out the name, type, and value(s)
* for extend classes in controls, check if x and y and things were inherited. do not cache the old values. possible have the renderer have a listener on the control properties for x and y
    * think about how these data structures are going to be created from description.ext and how they will be saved. this could be an issue
* check access property for controls and see if properties can be inherited and such https://community.bistudio.com/wiki/Dialog_Control#Dialogs
* have a color theme maker so that you don't need to constantly have to set colors to a macro. Maybe even have a big default archive for all attributes? Maybe just place them inside the lookup thingy?
* go back through the lookup table and remove redundant entries after you figure out the requirements and options for controls
* for preview, have a control state toggler. Also, for action on buttons, have popup window show with action text.
* add check to see if safeZone should be applied to control(s). This is checked by seeing if x,y,w,h properties are < 0 or >  1
* for new control, it isn't proper to say "Required" since the new control can allow for partial implementation
* have help button implementation for all popups
* allow for setting the current pixel location values for controls (then convert the x,y,w,h to new safeZone values based off pixel values)

General
-----------------------------------
* create documentation for all java classes and **well** document it
* for creating a macro, there should be only one Color option, one SQF code string option - this is to prevent confusion. DO NOT delete the property types. There should just be a behind-the-scenes check
* Arma 3 won't allow .png images. All images must be .paa, or .jpg. When we do an export, convert all .png to .paa
    * it would be convenient to store the original url to the converted.paa so that the .png won't need to be re-converted
    * [https://resources.bisimulations.com/wiki/Dialogs_Static#Images](https://resources.bisimulations.com/wiki/Dialogs_Static#Images)
* preview's resolution should be independent from the editor.
* for extending control, make sure that you implement the xml loading feature to set extended control as well


* for ControlClass.getInheritedProperties(), we should detect if they are explicitly redefined in a class, or just inherited. There should probably be two methods

* for display properties, should be able to set them somewhere. Also, in export dialog, a way to apply changes to display properties
    * also, we need to save display's class name and make sure that is put inside the export dialog
* add "Export Complete" popup after export is done


Bugs
-----------------------------------
* ControlPropertiesEditorPane ARRAY editor is broken (not implemented)
* select many things in tree view and drag into folder
* moving control group in display throws exception
* square scaling: doesn't scale up and back down with same snap amount
