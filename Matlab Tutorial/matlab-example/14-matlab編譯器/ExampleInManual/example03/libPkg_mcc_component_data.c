/*
 * MATLAB Compiler: 4.4 (R2006a)
 * Date: Fri Feb 08 19:43:27 2008
 * Arguments: "-B" "macro_default" "-W" "lib:libPkg" "-T" "link:exe" "mrank"
 * "printmatrix" "mrankp.c" "main_for_lib.c" 
 */

#include "mclmcr.h"

#ifdef __cplusplus
extern "C" {
#endif
const unsigned char __MCC_libPkg_session_key[] = {
        '2', '5', '5', '8', 'B', 'F', '4', '1', '6', '3', 'E', '6', '8', 'A',
        '3', '9', '4', '3', 'A', '5', 'F', '8', 'E', 'B', '9', '6', '4', 'A',
        '6', 'D', '0', '8', '5', '1', 'E', 'D', '4', '5', '4', '7', '7', '3',
        'D', '1', 'C', '3', '9', '9', '4', '9', '1', '1', '2', '4', '9', 'A',
        'C', 'D', '2', '7', 'E', '9', '9', 'F', 'D', 'D', 'A', '9', '6', 'C',
        'D', '3', 'C', '4', '3', 'D', 'E', '3', 'A', '3', '0', 'A', 'F', '7',
        'A', 'D', 'E', '9', '0', 'B', 'A', '9', 'C', 'F', '9', '6', 'E', '7',
        'F', '1', '2', 'C', '7', 'A', '4', 'E', 'B', '8', 'E', 'A', '5', '0',
        '4', 'C', 'D', '3', '2', '9', 'E', '3', 'B', '0', '9', '5', 'C', 'F',
        '0', '4', '6', '0', 'A', '7', '8', '5', 'A', '9', '8', 'A', '0', '4',
        '5', '4', '5', '9', 'F', '7', 'F', '1', 'E', '4', '5', '2', 'D', '0',
        '5', '1', 'C', '6', '1', 'A', 'E', 'F', 'C', '6', 'A', 'D', '3', '3',
        'F', 'B', '3', 'B', '9', '4', '5', '0', '8', '4', '7', 'A', 'A', 'B',
        '1', '7', '1', '5', 'B', 'D', '0', 'A', 'B', 'A', '6', '6', '0', '0',
        '6', '9', 'D', 'F', 'E', '6', '6', '1', '0', '3', '5', '0', '2', 'A',
        '1', 'F', '5', '2', '4', '1', 'B', '8', '3', 'D', '7', '2', 'D', '6',
        '3', '8', 'D', '2', '2', '5', '7', '4', '8', 'C', 'A', 'A', '4', '7',
        '0', '8', 'F', 'D', 'B', '7', '8', '4', '1', '5', '6', '7', '4', 'A',
        'C', 'B', '0', '1', '\0'};

const unsigned char __MCC_libPkg_public_key[] = {
        '3', '0', '8', '1', '9', 'D', '3', '0', '0', 'D', '0', '6', '0', '9',
        '2', 'A', '8', '6', '4', '8', '8', '6', 'F', '7', '0', 'D', '0', '1',
        '0', '1', '0', '1', '0', '5', '0', '0', '0', '3', '8', '1', '8', 'B',
        '0', '0', '3', '0', '8', '1', '8', '7', '0', '2', '8', '1', '8', '1',
        '0', '0', 'C', '4', '9', 'C', 'A', 'C', '3', '4', 'E', 'D', '1', '3',
        'A', '5', '2', '0', '6', '5', '8', 'F', '6', 'F', '8', 'E', '0', '1',
        '3', '8', 'C', '4', '3', '1', '5', 'B', '4', '3', '1', '5', '2', '7',
        '7', 'E', 'D', '3', 'F', '7', 'D', 'A', 'E', '5', '3', '0', '9', '9',
        'D', 'B', '0', '8', 'E', 'E', '5', '8', '9', 'F', '8', '0', '4', 'D',
        '4', 'B', '9', '8', '1', '3', '2', '6', 'A', '5', '2', 'C', 'C', 'E',
        '4', '3', '8', '2', 'E', '9', 'F', '2', 'B', '4', 'D', '0', '8', '5',
        'E', 'B', '9', '5', '0', 'C', '7', 'A', 'B', '1', '2', 'E', 'D', 'E',
        '2', 'D', '4', '1', '2', '9', '7', '8', '2', '0', 'E', '6', '3', '7',
        '7', 'A', '5', 'F', 'E', 'B', '5', '6', '8', '9', 'D', '4', 'E', '6',
        '0', '3', '2', 'F', '6', '0', 'C', '4', '3', '0', '7', '4', 'A', '0',
        '4', 'C', '2', '6', 'A', 'B', '7', '2', 'F', '5', '4', 'B', '5', '1',
        'B', 'B', '4', '6', '0', '5', '7', '8', '7', '8', '5', 'B', '1', '9',
        '9', '0', '1', '4', '3', '1', '4', 'A', '6', '5', 'F', '0', '9', '0',
        'B', '6', '1', 'F', 'C', '2', '0', '1', '6', '9', '4', '5', '3', 'B',
        '5', '8', 'F', 'C', '8', 'B', 'A', '4', '3', 'E', '6', '7', '7', '6',
        'E', 'B', '7', 'E', 'C', 'D', '3', '1', '7', '8', 'B', '5', '6', 'A',
        'B', '0', 'F', 'A', '0', '6', 'D', 'D', '6', '4', '9', '6', '7', 'C',
        'B', '1', '4', '9', 'E', '5', '0', '2', '0', '1', '1', '1', '\0'};

static const char * MCC_libPkg_matlabpath_data[] = 
    { "libPkg/", "toolbox/compiler/deploy/", "$TOOLBOXMATLABDIR/general/",
      "$TOOLBOXMATLABDIR/ops/", "$TOOLBOXMATLABDIR/lang/",
      "$TOOLBOXMATLABDIR/elmat/", "$TOOLBOXMATLABDIR/elfun/",
      "$TOOLBOXMATLABDIR/specfun/", "$TOOLBOXMATLABDIR/matfun/",
      "$TOOLBOXMATLABDIR/datafun/", "$TOOLBOXMATLABDIR/polyfun/",
      "$TOOLBOXMATLABDIR/funfun/", "$TOOLBOXMATLABDIR/sparfun/",
      "$TOOLBOXMATLABDIR/scribe/", "$TOOLBOXMATLABDIR/graph2d/",
      "$TOOLBOXMATLABDIR/graph3d/", "$TOOLBOXMATLABDIR/specgraph/",
      "$TOOLBOXMATLABDIR/graphics/", "$TOOLBOXMATLABDIR/uitools/",
      "$TOOLBOXMATLABDIR/strfun/", "$TOOLBOXMATLABDIR/imagesci/",
      "$TOOLBOXMATLABDIR/iofun/", "$TOOLBOXMATLABDIR/audiovideo/",
      "$TOOLBOXMATLABDIR/timefun/", "$TOOLBOXMATLABDIR/datatypes/",
      "$TOOLBOXMATLABDIR/verctrl/", "$TOOLBOXMATLABDIR/codetools/",
      "$TOOLBOXMATLABDIR/helptools/", "$TOOLBOXMATLABDIR/winfun/",
      "$TOOLBOXMATLABDIR/demos/", "$TOOLBOXMATLABDIR/timeseries/",
      "$TOOLBOXMATLABDIR/hds/", "toolbox/local/", "toolbox/compiler/",
      "toolbox/database/database/", "toolbox/optim/" };

static const char * MCC_libPkg_classpath_data[] = 
    { "java/jar/toolbox/database.jar" };

static const char * MCC_libPkg_libpath_data[] = 
    { "" };

static const char * MCC_libPkg_app_opts_data[] = 
    { "" };

static const char * MCC_libPkg_run_opts_data[] = 
    { "" };

static const char * MCC_libPkg_warning_state_data[] = 
    { "" };


mclComponentData __MCC_libPkg_component_data = { 

    /* Public key data */
    __MCC_libPkg_public_key,

    /* Component name */
    "libPkg",

    /* Component Root */
    "",

    /* Application key data */
    __MCC_libPkg_session_key,

    /* Component's MATLAB Path */
    MCC_libPkg_matlabpath_data,

    /* Number of directories in the MATLAB Path */
    36,

    /* Component's Java class path */
    MCC_libPkg_classpath_data,
    /* Number of directories in the Java class path */
    1,

    /* Component's load library path (for extra shared libraries) */
    MCC_libPkg_libpath_data,
    /* Number of directories in the load library path */
    0,

    /* MCR instance-specific runtime options */
    MCC_libPkg_app_opts_data,
    /* Number of MCR instance-specific runtime options */
    0,

    /* MCR global runtime options */
    MCC_libPkg_run_opts_data,
    /* Number of MCR global runtime options */
    0,
    
    /* Component preferences directory */
    "libPkg_7CE7AF4E3127AA91E7F352743CDCF72F",

    /* MCR warning status data */
    MCC_libPkg_warning_state_data,
    /* Number of MCR warning status modifiers */
    0,

    /* Path to component - evaluated at runtime */
    NULL

};

#ifdef __cplusplus
}
#endif


