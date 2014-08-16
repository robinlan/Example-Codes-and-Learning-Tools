/* Copyright (C) 1999-2001 Quality Quorum, Inc.
   Copyright (C) 2002 Chris Liechti and Steve Underwood.
 
   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:
 
     1. Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
     2. Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
     3. The name of the author may not be used to endorse or promote products
        derived from this software without specific prior written permission.
 
   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
   WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
   MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
   EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
   OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
   WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
   OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
   ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 
   QQI can be contacted as qqi@world.std.com
   
   Implementation of serial connection used on target side.
  
   Exported Data:
     None
  
   Imported Data:
     None     
  
   Static Data:
     serial_xXXX    - static data representing status of serial 
  
   Global Functions:  
     serial_xXXX    - see definitions in serial_xXXX
  
   Static Functions:  
     None
  
   
   $Id: serial.c,v 1.2 2003/12/11 12:42:23 XHKJAMES Exp $ */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif /* HAVE_CONFIG_H */

#include <assert.h>

#include <stdint.h>
#if defined(WIN32)
#include <windows.h>
#else
#include <stdio.h>
#include <string.h>
#include <termios.h>
#include <errno.h>

#ifdef HAVE_FCNTL_H
#include <fcntl.h>
#endif /* HAVE_FCNTL_H */

#ifdef HAVE_UNISTD_H
#include <unistd.h>
#include <sys/types.h>
#endif /* HAVE_UNISTD_H */

#ifdef HAVE_SYS_TIME_H
#include <sys/time.h>
#else
#include <time.h>
#endif /* HAVE_SYS_TIME_H */

#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#endif

#include "serial.h"

#if !defined(FALSE)
#define FALSE 0
#endif
#if !defined(TRUE)
#define TRUE (!FALSE)
#endif

#define SERIAL_BUFF_SIZE    8192
#define MAX_PORTS           32

static int initialised = FALSE;

typedef struct
{
    /* Serial device file descriptor */
#if defined(WIN32)
    HANDLE handle;
#else
    int fd;
#endif
    /* Input buffer */
    char serial_in_buf[SERIAL_BUFF_SIZE];
    /* Pointer to the current char in buffer */
    char *serial_in_ptr;
    /* Number of characters in buffer */
    int serial_in_count;
} port_data_t;

static port_data_t ports[MAX_PORTS] =
{
    {
#if defined(WIN32)
        INVALID_HANDLE_VALUE,
#else
        -1,
#endif
        { 0 },
        NULL,
        0
    }
};

/* Check check whether baud is an acceptable rate */
int serial_check_baud(int baud)
{
    switch (baud)
    {
    case 50:
    case 75:
    case 110:
    case 134:
    case 150:
    case 1800:
    case 2400:
    case 4800:
    case 9600:
    case 19200:
    case 38400:
    case 57600:
    case 115200:
#if defined(B230400)
    case 230400:
#endif
#if defined(B460800)
    case 460800:
#endif
#if defined(B921600)
    case 921600:
#endif
        return  TRUE;
    }

    return  FALSE;
}

/* Open serial connection */
int serial_open(char *name, int baud, int parity, int bits)
{
#if defined(WIN32)
    DCB dcb;
    BOOL res;
    COMMTIMEOUTS ctm;
    int port;

    assert(name != NULL);
    if (!initialised)
    {
        for (port = 0;  port < MAX_PORTS;  port++)
        {
            ports[port].handle = INVALID_HANDLE_VALUE;
            ports[port].serial_in_buf[0] = '\0';
            ports[port].serial_in_ptr = ports[port].serial_in_buf;
            ports[port].serial_in_count = 0;
        }
        initialised = TRUE;
    }
    for (port = 0;  port < MAX_PORTS;  port++)
    {
        if (ports[port].handle == INVALID_HANDLE_VALUE)
            break;
    }
    if (port >= MAX_PORTS)
        return  -1;
    /* Open port with exclusive access and no security attributes */
    ports[port].handle = CreateFile(name,
                                    GENERIC_READ | GENERIC_WRITE,
                                    0,
                                    NULL,
                                    OPEN_EXISTING,
                                    FILE_ATTRIBUTE_NORMAL,
                                    NULL);
    if (ports[port].handle == INVALID_HANDLE_VALUE)
        return  -2;

    ports[port].serial_in_ptr = ports[port].serial_in_buf;
    /* Set raw mode */
    if (!(res = GetCommState(ports[port].handle, &dcb)))
    {
        CloseHandle(ports[port].handle);
        ports[port].handle = INVALID_HANDLE_VALUE;
        return  -3;
    }

    dcb.BaudRate        = baud;
    dcb.fBinary         = TRUE;
    dcb.fParity         = (parity)  ?  TRUE  :  FALSE;
    dcb.fOutxCtsFlow    = FALSE;
    dcb.fOutxDsrFlow    = FALSE;
    dcb.fDtrControl     = DTR_CONTROL_ENABLE;
    dcb.fDsrSensitivity = FALSE;
    dcb.fOutX           = FALSE;
    dcb.fInX            = FALSE;
    dcb.fErrorChar      = FALSE;
    dcb.fNull           = FALSE;
    dcb.fRtsControl     = RTS_CONTROL_ENABLE;
    dcb.fAbortOnError   = FALSE;
    dcb.ByteSize        = bits;
    switch (parity)
    {
    case 0:
        dcb.Parity          = NOPARITY;
        break;
    case 1:
        dcb.Parity          = ODDPARITY;
        break;
    case 2:
        dcb.Parity          = EVENPARITY;
        break;
    }
    dcb.StopBits        = ONESTOPBIT;

    if (!(res = SetCommState(ports[port].handle, &dcb)))
    {
        CloseHandle(ports[port].handle);
        ports[port].handle = INVALID_HANDLE_VALUE;
        return  -4;
    }

    /* Reset timeouts */
    ctm.ReadIntervalTimeout         = 20; /* Allow 20ms between characters */
    ctm.ReadTotalTimeoutConstant    = 0;  /* This one will be changed later */
    ctm.ReadTotalTimeoutMultiplier  = 0;
    ctm.WriteTotalTimeoutConstant   = 0;
    ctm.WriteTotalTimeoutMultiplier = 0;

    if (!(res = SetCommTimeouts(ports[port].handle, &ctm)))
    {
        assert(0);
        CloseHandle(ports[port].handle);
        ports[port].handle = INVALID_HANDLE_VALUE;
        return  -5;
    }
#else
    int ret;
    int port;
    struct termios termios;

    assert(name != NULL);
    if (!initialised)
    {
        for (port = 0;  port < MAX_PORTS;  port++)
        {
            ports[port].fd = -1;
            ports[port].serial_in_buf[0] = '\0';
            ports[port].serial_in_ptr = ports[port].serial_in_buf;
            ports[port].serial_in_count = 0;
        }
        initialised = TRUE;
    }
    for (port = 0;  port < MAX_PORTS;  port++)
    {
        if (ports[port].fd < 0)
            break;
    }
    if (port >= MAX_PORTS)
        return  -1;

    if (strchr (name, ':') != NULL)
    {
        /* We do not support TCP connections right now */
        return  -1;
    }

    if ((ports[port].fd = open(name, O_RDWR)) < 0)
    {
        ports[port].fd = -1;
        return  -1;
    }

    /* Set raw mode */
    do
    {
        ret = tcgetattr(ports[port].fd, &termios);
    }
    while (ret < 0  &&  errno == EINTR);

    if (ret < 0)
    {
        close(ports[port].fd);
        ports[port].fd = -1;
        return  -1;
    }

    termios.c_iflag     = 0;
    termios.c_oflag     = 0;
    termios.c_lflag     = 0;
    termios.c_cflag     = CLOCAL | CREAD | CS8;
    termios.c_cc[VMIN]  = 0;
    termios.c_cc[VTIME] = 0;

    switch (baud)
    {
    case 50:
        ret = cfsetospeed(&termios, B50);
        break;
    case 75:
        ret = cfsetospeed(&termios, B75);
        break;
    case 110:
        ret = cfsetospeed(&termios, B110);
        break;
    case 134:
        ret = cfsetospeed(&termios, B134);
        break;
    case 150:
        ret = cfsetospeed(&termios, B150);
        break;
    case 200:
        ret = cfsetospeed(&termios, B200);
        break;
    case 1800:
        ret = cfsetospeed(&termios, B1800);
        break;
    case 2400:
        ret = cfsetospeed(&termios, B2400);
        break;
    case 4800:
        ret = cfsetospeed(&termios, B4800);
        break;
    case 9600:
        ret = cfsetospeed(&termios, B9600);
        break;
    case 19200:
        ret = cfsetospeed(&termios, B19200);
        break;
    case 38400:
        ret = cfsetospeed(&termios, B38400);
        break;
    case 57600:
        ret = cfsetospeed(&termios, B57600);
        break;
    case 115200:
        ret = cfsetospeed(&termios, B115200);
        break;
#if defined(B230400)
    case 230400:
        ret = cfsetospeed(&termios, B230400);
        break;
#endif
#if defined(B460800)
    case 460800:
        ret = cfsetospeed(&termios, B460800);
        break;
#endif
#if defined(B921600)
    case 921600:
        ret = cfsetospeed(&termios, B921600);
        break;
#endif
    }

    if (ret < 0)
    {
        close(ports[port].fd);
        ports[port].fd = -1;
        return  -1;
    }

    do
    {
        ret = tcsetattr(ports[port].fd, TCSANOW, &termios);
    }
    while (ret < 0  &&  errno == EINTR);

    if (ret < 0)
    {
        close(ports[port].fd);
        ports[port].fd = -1;
        return  -1;
    }
#endif

    ports[port].serial_in_ptr = ports[port].serial_in_buf;
    ports[port].serial_in_count = 0;
    return  port;
}

/* Close serial connection */
void serial_close(int port)
{
    assert(port >= 0  &&  port < MAX_PORTS);
#if defined(WIN32)
    if (ports[port].handle != INVALID_HANDLE_VALUE)
    {
        CloseHandle(ports[port].handle);
        ports[port].handle = INVALID_HANDLE_VALUE;
    }
#else
    if (ports[port].fd >= 0)
    {
        close(ports[port].fd);
        ports[port].fd = -1;
    }
#endif
}

/* Receive character, timeout is in in milliseconds, -1 means wait
   forever.  Returns error code if timeout or error, and charcter code 
   othervise */
int serial_getchar(int port, int timeout)
{
    assert(port >= 0  &&  port < MAX_PORTS);
    assert(timeout == -1  ||  timeout >= 0);

    if (ports[port].serial_in_count > 0)
    {
        ports[port].serial_in_count--;
        return (*ports[port].serial_in_ptr++ & 0xff);  /* 8-bit transparent */
    }

    ports[port].serial_in_count = serial_read(port, ports[port].serial_in_buf, sizeof(ports[0].serial_in_buf), timeout);
    if (ports[port].serial_in_count < 0)
    {
        /* Error */
        return  RP_VAL_SERIALGETCHARRET_ERR;
    }
    if (ports[port].serial_in_count == 0)
    {
        /* Timeout */
        return  RP_VAL_SERIALGETCHARRET_TMOUT;
    }

    ports[port].serial_in_ptr = ports[port].serial_in_buf;
    ports[port].serial_in_count--;

    return (*ports[port].serial_in_ptr++ & 0xff);
}

/* Receive character, timeout is in in milliseconds, -1 means wait
   forever.  Returns error code if timeout or error, and charcter code 
   othervise */
int serial_read(int port, char *buf, size_t len, int timeout)
{
#if defined(WIN32)
    int ret;
    COMMTIMEOUTS ctm;
    DWORD dtmp;
    BOOL res;

    assert(port >= 0  &&  port < MAX_PORTS);
    assert(ports[port].handle != INVALID_HANDLE_VALUE);
    assert(timeout == -1  ||  timeout >= 0);

    if (!(res = GetCommTimeouts(ports[port].handle, &ctm)))
    {
        assert(0);
        return  -1;
    }

    assert(ctm.ReadIntervalTimeout == 20);
    assert(ctm.ReadTotalTimeoutMultiplier == 0);
    assert(ctm.WriteTotalTimeoutConstant == 0);
    assert(ctm.WriteTotalTimeoutMultiplier == 0);

    ctm.ReadTotalTimeoutConstant = timeout;

    if (!(res = SetCommTimeouts(ports[port].handle, &ctm)))
    {
        assert(0);
        return  -1;
    }

    if (!(res = ReadFile(ports[port].handle, buf, len, &dtmp, NULL)))
        return  -1;

    ret = dtmp;
#else
    int ret;

    assert(port >= 0  &&  port < MAX_PORTS);
    assert(ports[port].fd >= 0);
    assert(timeout == -1  ||  timeout >= 0);

    if (timeout != -1)
    {
        struct timeval tv;
        struct timeval end;
        struct timeval cur;
        struct timezone tz;
        fd_set rset;

        /* It is quite possible that targets would install
           some signal handlers, so we have to ignore EINTR
           in all cases */

        /* Let us calculate the timeout */
        tv.tv_sec = timeout/1000;
        tv.tv_usec = (timeout%1000)*1000;

        /* Let us get current time */
        if ((ret = gettimeofday(&cur, &tz)) < 0)
            return -1;

        /* Let us figure out end time */
        end.tv_sec  = cur.tv_sec + tv.tv_sec;
        end.tv_usec = cur.tv_usec + tv.tv_usec;
        if (end.tv_usec >= 1000000)
        {
            end.tv_sec++;
            end.tv_usec -= 1000000;
        }

        assert(end.tv_usec < 1000000);

        for (;;)
        {
            FD_ZERO(&rset);
            FD_SET(ports[port].fd, &rset);

            if ((ret = select(ports[port].fd + 1, &rset, NULL, NULL, &tv)) > 0)
            {
                assert(FD_ISSET(ports[port].fd, &rset));
                break;
            }

            if (ret == 0)
            {
                /* Timeout */
                return  0;
            }

            if (ret < 0  &&  errno != EINTR)
                return  -1;

            /* We have been interrupted by a signal */

            /* We have to recalculate the timeout */
            if ((ret = gettimeofday(&cur, &tz)) < 0)
                return  -1;

            if (cur.tv_sec > end.tv_sec
                ||
                (cur.tv_sec == end.tv_sec  &&  cur.tv_usec >= end.tv_usec))
            {
                tv.tv_sec  = 0;
                tv.tv_usec = 0;

                continue;
            }

            tv.tv_sec = end.tv_sec - cur.tv_sec;

            if (cur.tv_usec <= end.tv_usec)
            {
                tv.tv_usec = end.tv_usec - cur.tv_usec;
            }
            else
            {
                assert(tv.tv_sec > 0);

                tv.tv_sec--;
                tv.tv_usec = end.tv_usec + 1000000 - cur.tv_usec;
            }
        }
    }
    else
    {
        /* Forever */
        for (;;)
        {
            fd_set rset;

            FD_ZERO(&rset);
            FD_SET(ports[port].fd, &rset);

            if ((ret = select(ports[port].fd + 1, &rset, NULL, NULL, NULL)) > 0)
            {
                assert(FD_ISSET(ports[port].fd, &rset));
                break;
            }

            if (ret < 0  &&  errno != EINTR)
                return  -1;

            assert(ret != 0);
            assert(errno == EINTR);
        }
    }

    /* We have to ignore EINTR here too */
    do
    {
        ret = read(ports[port].fd, buf, len);
    }
    while (ret < 0  &&  errno == EINTR);
#endif

    return ret;
}

/* Send character */
void serial_putchar(int port, int c)
{
    char b[4];
#if defined(WIN32)
    DWORD dtmp;

    assert(port >= 0  &&  port < MAX_PORTS);
    assert(ports[port].handle != INVALID_HANDLE_VALUE);

    b[0] = c & 0xff;

    WriteFile(ports[port].handle, b, 1, &dtmp, NULL);
#else
    int ret;

    assert(port >= 0  &&  port < MAX_PORTS);
    assert(ports[port].fd >= 0);

    b[0] = c & 0xff;

    ret = 0;
    do
    {
        ret = write(ports[port].fd, b, 1);
    }
    while (ret < 0  &&  errno == EINTR);
#endif
}

/* Send buffer */
int serial_write(int port, char *buf, size_t len)
{
#if defined(WIN32)
    BOOL res;
    DWORD dtmp;

    assert(port >= 0  &&  port < MAX_PORTS);
    assert(ports[port].handle != INVALID_HANDLE_VALUE);
    assert(buf != NULL);
    assert(len > 0);

    res = WriteFile(ports[port].handle, buf, len, &dtmp, NULL);
    if (!res  ||  dtmp != len)
        return  FALSE;
#else
    int ret;

    assert(port >= 0  &&  port < MAX_PORTS);
    assert(ports[port].fd >= 0);
    assert(buf != NULL);
    assert(len > 0);

    ret = 0;
    do
    {
        ret = write(ports[port].fd, buf, len);
    }
    while (ret < 0  &&  errno == EINTR);

    if (ret != (int) len)
        return  FALSE;
#endif
    return  TRUE;
}

/* Discard input */
void serial_flushinput(int port)
{
    assert(port >= 0  &&  port < MAX_PORTS);
#if defined(WIN32)
    assert(ports[port].handle != INVALID_HANDLE_VALUE);

    PurgeComm(ports[port].handle, PURGE_RXCLEAR);
#else
    assert(ports[port].fd >= 0);

    tcflush(ports[port].fd, TCIFLUSH);
#endif
    ports[port].serial_in_count = 0;
}

/* Send break */
void serial_sendbreak(int port)
{
    assert(port >= 0  &&  port < MAX_PORTS);
#if defined(WIN32)
    assert(ports[port].handle != INVALID_HANDLE_VALUE);

    SetCommBreak(ports[port].handle);
    Sleep(500);
    ClearCommBreak(ports[port].handle);
#else
    assert(ports[port].fd >= 0);

    tcsendbreak(ports[port].fd, 0);
#endif
}
