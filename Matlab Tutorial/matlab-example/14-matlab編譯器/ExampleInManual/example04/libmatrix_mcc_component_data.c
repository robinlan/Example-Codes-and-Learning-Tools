/*
 * MATLAB Compiler: 4.4 (R2006a)
 * Date: Mon Feb 11 11:07:26 2008
 * Arguments: "-B" "macro_default" "-B" "csharedlib:libmatrix" "-W"
 * "lib:libmatrix" "-T" "link:lib" "addmatrix.m" "multiplymatrix.m"
 * "eigmatrix.m" "-v" 
 */

#include "mclmcr.h"

#ifdef __cplusplus
extern "C" {
#endif
const unsigned char __MCC_libmatrix_session_key[] = {
        '9', 'B', '6', '7', '2', '8', '4', '5', '3', 'C', '2', '6', 'C', '6',
        '8', '8', '1', '3', '3', 'D', '2', '9', '9', '1', 'E', '4', 'D', '5',
        'E', 'D', '0', '3', '2', '6', '6', 'D', '1', '4', '2', 'C', 'C', '0',
        'B', '7', 'D', '7', 'D', '8', '1', '9', 'C', 'D', '2', '5', '7', '6',
        'E', '1', '9', '0', '7', '0', 'A', '8', '7', '2', '6', 'F', 'D', '5',
        '4', '5', 'A', '7', '5', 'B', 'D', '1', '5', '1', '5', '2', 'F', 'C',
        'D', '9', 'D', '6', 'D', 'C', '6', '7', '9', 'D', '7', 'B', '7', '5',
        'D', '4', '4', 'D', 'D', '1', '2', '8', '6', '2', '3', '5', '3', '8',
        '5', '6', 'C', '9', 'F', '3', 'B', '2', '6', '8', 'B', '0', 'F', '8',
        'D', 'E', '8', 'A', '7', 'B', '1', '2', 'A', '6', '0', 'A', 'C', '6',
        'D', '5', '3', '4', '6', '9', '1', '0', '8', '7', 'F', '8', '0', '3',
        '8', '3', 'D', 'D', '9', 'C', 'D', 'F', '1', 'E', 'F', '3', 'C', '1',
        'F', '7', '4', 'C', 'C', '8', '3', 'B', '8', '6', 'C', '0', 'E', 'C',
        '5', 'C', 'F', 'A', '8', '0', '7', 'B', '8', '0', '4', 'C', 'D', 'B',
        'F', 'F', 'B', '7', '6', '8', '1', 'B', '6', 'A', '3', '7', '4', '4',
        '7', 'D', 'D', '1', '0', '6', '9', 'E', '5', '1', 'E', '3', '6', '5',
        '1', 'E', '5', 'D', 'C', '9', '0', 'A', 'B', '0', '2', 'D', '9', '2',
        '2', '9', '0', 'E', 'B', '3', '2', '8', '4', '1', '6', '3', 'E', '0',
        '0', '9', '5', '6', '\0'};

const unsigned char __MCC_libmatrix_public_key[] = {
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

static const char * MCC_libmatrix_matlabpath_data[] = 
    { "libmatrix/", "toolbox/compiler/deploy/", "$TOOLBOXMATLABDIR/general/",
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

static const char * MCC_libmatrix_classpath_data[] = 
    { "java/jar/toolbox/database.jar" };

static const char * MCC_libmatrix_libpath_data[] = 
    { "" };

static const char * MCC_libmatrix_app_opts_data[] = 
    { "" };

static const char * MCC_libmatrix_run_opts_data[] = 
    { "" };

static const char * MCC_libmatrix_warning_state_data[] = 
    { "" };


mclComponentData __MCC_libmatrix_component_data = { 

    /* Public key data */
    __MCC_libmatrix_public_key,

    /* Component name */
    "libmatrix",

    /* Component Root */
    "",

    /* Application key data */
    __MCC_libmatrix_session_key,

    /* Component's MATLAB Path */
    MCC_libmatrix_matlabpath_data,

    /* Number of directories in the MATLAB Path */
    36,

    /* Component's Java class path */
    MCC_libmatrix_classpath_data,
    /* Number of directories in the Java class path */
    1,

    /* Component's load library path (for extra shared libraries) */
    MCC_libmatrix_libpath_data,
    /* Number of directories in the load library path */
    0,

    /* MCR instance-specific runtime options */
    MCC_libmatrix_app_opts_data,
    /* Number of MCR instance-specific runtime options */
    0,

    /* MCR global runtime options */
    MCC_libmatrix_run_opts_data,
    /* Number of MCR global runtime options */
    0,
    
    /* Component preferences directory */
    "libmatrix_F36B4D3C308B9C22AA5F4D10018C4A01",

    /* MCR warning status data */
    MCC_libmatrix_warning_state_data,
    /* Number of MCR warning status modifiers */
    0,

    /* Path to component - evaluated at runtime */
    NULL

};

#ifdef __cplusplus
}
#endif


