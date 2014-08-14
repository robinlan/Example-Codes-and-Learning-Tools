/*
 * MATLAB Compiler: 4.4 (R2006a)
 * Date: Fri Feb 08 19:12:43 2008
 * Arguments: "-B" "macro_default" "-m" "-W" "main" "-T" "link:exe" "main"
 * "mrank" 
 */

#include "mclmcr.h"

#ifdef __cplusplus
extern "C" {
#endif
const unsigned char __MCC_main_session_key[] = {
        '6', 'D', 'E', '6', '4', '2', 'B', '2', '5', '3', '7', '7', '0', 'D',
        '4', '2', '0', '7', '6', 'C', '3', '2', 'F', 'D', '2', 'A', 'D', 'F',
        'B', 'E', 'B', '7', '8', '5', 'B', 'A', '1', 'B', '0', '4', '0', '0',
        'B', '9', 'A', '2', '1', 'F', 'C', '7', 'E', '5', '2', 'C', 'A', '6',
        'A', 'E', '0', 'E', '6', 'A', '3', '2', '7', '0', 'F', 'A', 'A', '4',
        '3', '5', '3', 'A', '3', 'B', 'B', '0', '6', 'C', 'E', '4', 'A', '5',
        '8', '7', '2', '4', '1', '7', 'C', '3', '4', 'D', '2', 'F', '7', '0',
        'F', 'B', '7', 'A', 'E', '6', '0', '0', '5', 'E', '5', 'C', '8', '2',
        'E', '5', 'C', 'E', '5', '1', '5', '6', '4', '4', '6', '8', 'B', '1',
        '5', 'E', '9', '9', '1', '3', '0', '1', '2', 'C', '2', '4', '5', 'B',
        '6', '0', 'D', 'E', 'B', '2', '5', 'C', '9', '8', 'D', 'F', 'F', 'E',
        '6', '9', 'C', 'C', 'A', 'A', '7', '2', '6', '8', 'F', '0', '3', 'C',
        'B', '6', 'F', 'D', '9', '2', '6', '2', '0', '9', '9', '9', '4', '1',
        '3', '5', '5', '3', 'D', '2', 'E', '2', '3', 'E', '1', '6', '5', '6',
        'F', '0', '9', '2', '6', '0', 'C', '8', 'E', '0', '8', '0', 'A', '4',
        'A', '6', 'E', 'F', '2', '8', '3', 'F', '5', 'E', '3', '7', '4', '4',
        'A', '9', '1', '8', 'D', '4', 'E', '7', '0', '5', '8', 'D', 'A', '9',
        '7', '3', '0', '9', 'C', '0', 'A', '7', '5', 'F', '8', 'E', 'D', '8',
        '3', 'F', '7', '9', '\0'};

const unsigned char __MCC_main_public_key[] = {
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

static const char * MCC_main_matlabpath_data[] = 
    { "main/", "toolbox/compiler/deploy/", "$TOOLBOXMATLABDIR/general/",
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

static const char * MCC_main_classpath_data[] = 
    { "java/jar/toolbox/database.jar" };

static const char * MCC_main_libpath_data[] = 
    { "" };

static const char * MCC_main_app_opts_data[] = 
    { "" };

static const char * MCC_main_run_opts_data[] = 
    { "" };

static const char * MCC_main_warning_state_data[] = 
    { "" };


mclComponentData __MCC_main_component_data = { 

    /* Public key data */
    __MCC_main_public_key,

    /* Component name */
    "main",

    /* Component Root */
    "",

    /* Application key data */
    __MCC_main_session_key,

    /* Component's MATLAB Path */
    MCC_main_matlabpath_data,

    /* Number of directories in the MATLAB Path */
    36,

    /* Component's Java class path */
    MCC_main_classpath_data,
    /* Number of directories in the Java class path */
    1,

    /* Component's load library path (for extra shared libraries) */
    MCC_main_libpath_data,
    /* Number of directories in the load library path */
    0,

    /* MCR instance-specific runtime options */
    MCC_main_app_opts_data,
    /* Number of MCR instance-specific runtime options */
    0,

    /* MCR global runtime options */
    MCC_main_run_opts_data,
    /* Number of MCR global runtime options */
    0,
    
    /* Component preferences directory */
    "main_AAB32B4217C78CF56638170E21AD7D06",

    /* MCR warning status data */
    MCC_main_warning_state_data,
    /* Number of MCR warning status modifiers */
    0,

    /* Path to component - evaluated at runtime */
    NULL

};

#ifdef __cplusplus
}
#endif


