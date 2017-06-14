Features
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
* when choosing stringtable key macro, the preview area should show all strings (original, english, etc)

General
-----------------------------------
* create documentation for all java classes and **well** document it
* for creating a macro, there should be only one Color option, one SQF code string option - this is to prevent confusion. DO NOT delete the property types. There should just be a behind-the-scenes check
* Arma 3 won't allow .png images. All images must be .paa, or .jpg. When we do an export, convert all .png to .paa
    * it would be convenient to store the original url to the converted.paa so that the .png won't need to be re-converted
    * [https://resources.bisimulations.com/wiki/Dialogs_Static#Images](https://resources.bisimulations.com/wiki/Dialogs_Static#Images)
* preview's resolution should be independent from the editor.
* preview should have background image
* ............................................
* for display properties, should be able to set them somewhere. Also, in export dialog, a way to apply changes to display properties
    * also, we need to save display's class name and make sure that is put inside the export dialog
* maybe instead of hardcoding percentages for snap, we have expression evaluating (safeZoneW * .25 is 25% of canvas width, where 0.25 is 25% of viewport width)
* ............................................
* ControlProperty custom data isn't saved to file
* when creating a new custom control, we should have option to set what the custom control will extend (if any)
* doesn't save nested classes in control xml writing and also doesn't load them
* when loading keys from stringtable, check for improper key ids (missing str_ or duplicate ids) 
* save enabled and visible states for control saving to xml, and load them back!
* stringtable editor: reload and save to file need error dialogs
* save stringtable langauge (adc menu bar) to project.xml file
* string table editor: edit containers
* ............................................
* use proper text editor for Image and Hex Color
* instead of ValueObserver.removeListener(), create an expiration checker method such that when it returns true, the observer removes it
  * this will make garbage collection faster and and reduce risk of memory leaks. Also, its cleaner than storing the listeners
* create JUnit tests for ControlClass and ControlProperty

Bugs
-----------------------------------
* ControlPropertiesEditorPane ARRAY editor is broken (not implemented)
* select many things in tree view and drag into folder
* moving control group in display throws exception
* square scaling: doesn't scale up and back down with same snap amount
* in StringTableEditorPopup, going to configuration tab and clicking on preview language combobox will freeze application
* convert font to String type. Make font invalid. Convert back to font type