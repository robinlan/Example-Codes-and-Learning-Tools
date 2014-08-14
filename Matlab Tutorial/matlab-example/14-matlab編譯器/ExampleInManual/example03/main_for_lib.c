/*
 * This is the main wrapper for all C library functions.
 * This wrapper has conditional code as is necessary for
 * macintosh platform.
 */

#include "main_for_lib.h" /* for the definition of the structure inputs */

#ifdef __APPLE_CC__
#include <CoreFoundation/CoreFoundation.h>
#include <pthread.h>
#endif

void *run_main(void *);

int main(int ac, const char* av[])
{
    inputs in;
    in.ac = ac;
    in.av = av;
    in.err = 0;
#ifdef __APPLE_CC__
    pthread_t id;
    pthread_create(&id, NULL, run_main, &in);

    CFRunLoopSourceContext sourceContext;
    sourceContext.version         = 0;
    sourceContext.info            = NULL;
    sourceContext.retain          = NULL;
    sourceContext.release         = NULL;
    sourceContext.copyDescription = NULL;
    sourceContext.equal           = NULL;
    sourceContext.hash            = NULL;
    sourceContext.schedule        = NULL;
    sourceContext.cancel          = NULL;
    sourceContext.perform         = NULL;

    CFRunLoopSourceRef sourceRef = CFRunLoopSourceCreate(NULL, 0, &sourceContext);
    CFRunLoopAddSource(CFRunLoopGetCurrent(), sourceRef, kCFRunLoopCommonModes);
    CFRunLoopRun();
#else
    run_main(&in);
#endif
    return in.err;
}
