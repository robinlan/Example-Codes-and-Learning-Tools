/* A simple oscilloscope program, reading sample data from a serial
   port.
   
   This program was developed under Cygwin on Windows XP, and Linux.
   It requires the FLTK 1.1.4 toolkit and the pthreads library,
   available from www.cygwin.com. */

#include <math.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#include <pthread.h>

#include <FL/Fl.H>
#include <FL/fl_ask.H>
#include <FL/Fl_Overlay_Window.H>
#include <FL/Fl_Light_Button.H>
#include <FL/Fl_Slider.H>
#include <FL/Fl_Adjuster.H>
#include "../Cartesian.H"
#include <FL/fl_draw.H>

#include "serial.h"

pthread_t display_thread;
pthread_t serial_thread;

#define CAPTURE_SIZE    8192

double capture[CAPTURE_SIZE];
double plot[4096];
double plot2[4];

int last_used_sample = 0;
int next_capture_sample = 0;

Ca_X_Axis *time_axis;
Ca_Y_Axis *amp_axis;
Fl_Slider *trig_level;
Fl_Slider *x_scale;
Fl_Light_Button *rolling_mode;

Ca_Canvas *canvas;

int trigger_level = 2048;
int x_scaling = 1;
int triggered_mode = 1;

void display_update(void *)
{
    static Ca_Line *L_L = 0;
    static Ca_Line *P_P = 0;
    int i;
    int j;
    int trig_point;
    int contents;
    int start;
    int end;
    double sample;
    double last;

    plot2[0] = 0;
    plot2[1] = trigger_level;
    plot2[2] = 512;
    plot2[3] = trigger_level;
    if (P_P)
        delete P_P;
	P_P = new Ca_Line(2, plot2, 0, 0, FL_RED, CA_NO_POINT);

    trig_point = -1;
    contents = (next_capture_sample - last_used_sample + CAPTURE_SIZE) & (CAPTURE_SIZE - 1);

    if (triggered_mode)
    {
        /* Triggered display mode, triggering at the 20% point */
        if (contents >= 512)
        {
            /* Look for a trigger point */
            start = (last_used_sample + 100) & (CAPTURE_SIZE - 1);
            end = (next_capture_sample - 512 + 100) & (CAPTURE_SIZE - 1);
            last = capture[(start - 1) & (CAPTURE_SIZE - 1)];
            for (i = start;  i != end;  i++)
            {
                sample = capture[i];
                if (last < trigger_level  &&  sample >= trigger_level)
                {
                    trig_point = i;
                    break;
                }
                last = sample;
            }
            /* If no trigger for a long time, free run the capturing */
            if (trig_point < 0  &&  contents > CAPTURE_SIZE - 2000)
                trig_point = next_capture_sample - 512 + 100;
        }
        if (trig_point >= 0)
        {
            /* Move the trigger point back a bit, so we display some pre-trigger data */
            j = trig_point - 100;
            if (j < 0)
                j += CAPTURE_SIZE;
            for (i = 0;  i < 512;  i++)
            {
                plot[2*i] = i;
                plot[2*i + 1] = capture[j++];
                if (j >= CAPTURE_SIZE)
                    j = 0;
            }
            last_used_sample = j;
            amp_axis->current();

            if (L_L)
                delete L_L;
            L_L = new Ca_Line(512, plot, 0, 0, FL_BLUE, CA_NO_POINT);
        }
    }
    else
    {
        /* Rolling display mode */
        start = (next_capture_sample - 512) & (CAPTURE_SIZE - 1);
        end = next_capture_sample;
        j = start;
        for (i = 0;  i < 512;  i++)
        {
            plot[2*i] = i;
            plot[2*i + 1] = capture[j++];
            if (j >= CAPTURE_SIZE)
                j = 0;
        }
        last_used_sample = j;
        amp_axis->current();
    
        if (L_L)
            delete L_L;
        L_L = new Ca_Line(512, plot, 0, 0, FL_BLUE, CA_NO_POINT);
    }
    Fl::add_timeout(0.1, display_update);
};

void trig_level_callback(Fl_Widget *, void *)
{
    trigger_level = (int) (4096.0 - trig_level->value()*4096.0);
}

void x_scale_callback(Fl_Widget *, void *)
{
    x_scaling = (int) (x_scale->value()*200.0) + 1;
}

void rolling_callback(Fl_Widget *, void *)
{
    triggered_mode = !rolling_mode->value();
}

void *display_task(void *data)
{
    Fl_Double_Window *w = new Fl_Double_Window(560, 380, "Oscilloscope");
	Fl_Group *c = new Fl_Group(0, 35, 580, 345 );

    c->box(FL_DOWN_BOX);
    c->align(FL_ALIGN_TOP | FL_ALIGN_INSIDE);

    canvas = new Ca_Canvas(90, 75, 400, 225, "");
    canvas->box(FL_PLASTIC_DOWN_BOX);
    canvas->color(7);
    canvas->align(FL_ALIGN_TOP);
    Fl_Group::current()->resizable(canvas);
    canvas->border(15);

    time_axis = new Ca_X_Axis(95, 305, 395, 30, "Time");
    time_axis->align(FL_ALIGN_BOTTOM);
    time_axis->minimum(0.0);
    time_axis->maximum(512.0);
    time_axis->label_format("%g");
    time_axis->minor_grid_color(fl_gray_ramp(20));
    time_axis->major_grid_color(fl_gray_ramp(15));
    time_axis->label_grid_color(fl_gray_ramp(10));
    time_axis->grid_visible(CA_MINOR_GRID | CA_MAJOR_GRID | CA_LABEL_GRID);
    time_axis->major_step(50);
    time_axis->label_step(50);
	time_axis->axis_color(FL_BLACK);
	time_axis->axis_align(CA_BOTTOM | CA_LINE);

    amp_axis = new Ca_Y_Axis(10, 70, 78, 235, "Amp");
    amp_axis->align(FL_ALIGN_TOP);
    amp_axis->minor_grid_color(fl_gray_ramp(20));
    amp_axis->major_grid_color(fl_gray_ramp(15));
    amp_axis->label_grid_color(fl_gray_ramp(10));
	amp_axis->grid_visible(CA_LABEL_GRID | CA_ALWAYS_VISIBLE);
	amp_axis->minor_grid_style(FL_DOT);
	amp_axis->minimum(0);
    amp_axis->maximum(4096);
    amp_axis->current();

	trig_level = new Fl_Slider(500, 75, 15, 225, "Trig");
	trig_level->callback(&trig_level_callback);
    trig_level->box(FL_PLASTIC_DOWN_BOX);
    trig_level->value(0.5);

	x_scale = new Fl_Slider(170, 335, 225, 15, "X-scale");
	x_scale->callback(&x_scale_callback);
    x_scale->type(1);
    x_scale->box(FL_PLASTIC_DOWN_BOX);
    x_scale->value(0.0);

	rolling_mode = new Fl_Light_Button(450, 335, 75, 25, "Rolling");
	rolling_mode->callback(&rolling_callback);
    rolling_mode->box(FL_PLASTIC_UP_BOX);

    c->end();

    Fl_Group::current()->resizable(c);
    w->end();
    w->show();

    Fl::add_timeout(0, display_update);
	Fl::run();
	return NULL;
};

void *serial_task(void *data)
{
#if 1
    /* Real operation with a serial port */
    int port;
    uint8_t inbuf[1024];
    int i;
    int len;
    FILE *log;
    char *portname;
    static int skip = 0;

    log = fopen("log", "w");
    portname = (char *) data;
    if ((port = serial_open(portname, 115200, 0, 8)) < 0)
    {
        fl_alert("Failed to open serial port - %d.", port);
        exit(2);
    }
    for (;;)
    {
        len = serial_read(port, (char *) inbuf, sizeof(inbuf), 1000);
        if (len > 0)
        {
            for (i = 0;  i < len;  i++)
            {
                //fprintf(log, "%7d %f\n", inbuf[i], inbuf[i]*16.0);
                skip++;
                if (skip >= x_scaling)
                {
                    capture[next_capture_sample++] = inbuf[i]*16.0;
                    if (next_capture_sample >= CAPTURE_SIZE)
                        next_capture_sample = 0;
                    skip = 0;
                }
            }
        }
    }
#else
    /* Test operation, generating a waveform for display */
    static float phase = 0.0;
    static float phase_step = 0.030;
    static float phase_step_step = 0.00000;

    int i;
    int x;
    double sample;

    x = 0;
    for (;;)
    {
        Sleep(1);
        for (i = 0;  i < 10;  i++)
        {
            phase += phase_step;
            phase_step += phase_step_step;
            if (phase_step > 0.1)
                phase_step_step = -0.00001;
            else if (phase_step < -0.1)
                phase_step_step = 0.00001;
            sample = sin(phase)*2000.0;
            sample += 2048.0;
            capture[next_capture_sample++] = sample;
            next_capture_sample &= (CAPTURE_SIZE - 1);
        }
    }
#endif

    return NULL;
}

int main(int argc, char **argv) 
{
    pthread_attr_t attr;
    char *port;

    port = "com1";
    if (argc > 1)
        port = argv[1];

    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
    if (pthread_create(&display_thread, &attr, serial_task, port) < 0)
    {
        fl_alert("Failed to start display thread.");
        exit(2);
    }
    display_task(NULL);
}
