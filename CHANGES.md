**Added:**
* 

**Changed:**
* optimized editor component tree view so that selecting doesn't lag a lot
    * now uses datacontent inside arma control for caching treeitem

**Fixed:**
* text not being rendered at the center of control

**Notes:**
* may 9 3:37 : project xml loader can find classes to extend, but it isn't working for imported projects
* may 9: we need to implement dialog conversion error for dialog import dialog
* **may 10: we need extensive tests for ControlClass that test inheritance in many different ways!**