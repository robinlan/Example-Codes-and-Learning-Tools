/*
 * MATLAB Compiler: 4.3 (R14SP3)
 * Date: Sat Jul 05 11:32:39 2008
 * Arguments: "-B" "macro_default" "-B" "csharedlib:matLib" "-W" "lib:matLib"
 * "-T" "link:lib" "myMatInv.m" "myMatMultiply.m" 
 */

#include "mclmcr.h"

#ifdef __cplusplus
extern "C" {
#endif
const unsigned char __MCC_matLib_session_key[] = {
        '6', '5', '7', '9', 'F', '2', 'B', '2', 'C', '3', '4', '1', 'D', 'A',
        '7', '1', '6', '0', 'B', 'F', '9', 'F', '3', '2', '1', '9', '9', 'E',
        '4', 'F', '8', '9', '2', '4', 'F', 'E', '5', 'C', '0', 'C', '2', '4',
        '9', 'C', '2', '6', '7', 'A', '4', 'F', '4', '1', '2', 'D', 'A', 'E',
        '0', '9', '9', '8', 'A', '1', 'E', '8', '6', '0', '1', 'E', '6', '4',
        '7', '9', 'C', 'F', '5', 'C', '9', '6', 'D', '4', 'A', '1', 'C', '1',
        '8', '0', 'F', 'B', '9', 'D', '1', '0', '2', '2', '8', 'F', 'F', '5',
        '3', '6', 'C', '0', 'E', '7', 'D', '2', '8', '0', '9', '1', '3', '1',
        '9', '9', '9', 'A', 'D', 'B', '7', '4', '9', '1', '5', '8', 'B', '6',
        '8', 'A', 'D', '4', '0', '8', '5', '3', '3', 'B', '3', 'E', 'C', 'F',
        'E', 'D', '7', '4', 'F', '9', '1', 'D', '6', '5', '1', '6', '3', '3',
        '7', '4', '6', 'F', '8', 'A', '0', 'F', 'D', '7', 'E', '8', 'D', '1',
        '3', '6', '9', '6', '2', 'C', 'D', '2', '1', 'F', 'E', '8', '2', '7',
        'D', 'C', 'E', '1', '4', 'E', 'A', 'E', 'C', 'E', '2', '7', '4', '4',
        '7', 'C', '8', '9', '0', 'D', '9', 'C', '9', 'C', '7', 'C', 'F', '5',
        '9', '6', 'B', 'E', '8', '3', 'B', '1', '2', 'A', '2', 'B', '8', '3',
        '5', '5', '1', 'C', '2', '7', 'B', 'E', '4', '7', '6', 'E', '9', 'A',
        '1', 'B', 'B', '9', 'C', '6', 'E', '9', '8', 'E', '3', '5', '1', '0',
        'A', 'B', '6', 'E', '\0'};

const unsigned char __MCC_matLib_public_key[] = {
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

static const char * MCC_matLib_matlabpath_data[] = 
    { "matLib/", "toolbox/compiler/deploy/", "$TOOLBOXMATLABDIR/general/",
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
      "$TOOLBOXMATLABDIR/hds/", "toolbox/local/", "toolbox/compiler/" };

static const char * MCC_matLib_classpath_data[] = 
    { "" };

static const char * MCC_matLib_libpath_data[] = 
    { "" };

static const char * MCC_matLib_app_opts_data[] = 
    { "" };

static const char * MCC_matLib_run_opts_data[] = 
    { "" };

static const char * MCC_matLib_warning_state_data[] = 
    { "" };


mclComponentData __MCC_matLib_component_data = { 

    /* Public key data */
    __MCC_matLib_public_key,

    /* Component name */
    "matLib",

    /* Component Root */
    "",

    /* Application key data */
    __MCC_matLib_session_key,

    /* Component's MATLAB Path */
    MCC_matLib_matlabpath_data,

    /* Number of directories in the MATLAB Path */
    34,

    /* Component's Java class path */
    MCC_matLib_classpath_data,
    /* Number of directories in the Java class path */
    0,

    /* Component's load library path (for extra shared libraries) */
    MCC_matLib_libpath_data,
    /* Number of directories in the load library path */
    0,

    /* MCR instance-specific runtime options */
    MCC_matLib_app_opts_data,
    /* Number of MCR instance-specific runtime options */
    0,

    /* MCR global runtime options */
    MCC_matLib_run_opts_data,
    /* Number of MCR global runtime options */
    0,
    
    /* Component preferences directory */
    "matLib_EBE74DD148EFAD14D47BA9E7216328EA",

    /* MCR warning status data */
    MCC_matLib_warning_state_data,
    /* Number of MCR warning status modifiers */
    0,

    /* Path to component - evaluated at runtime */
    NULL

};

#ifdef __cplusplus
}
#endif


