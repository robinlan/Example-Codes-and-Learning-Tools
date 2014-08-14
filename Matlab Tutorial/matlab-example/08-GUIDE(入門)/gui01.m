function varargout = gui01(varargin)
% GUI01 M-file for gui01.fig
%      GUI01, by itself, creates a new GUI01 or raises the existing
%      singleton*.
%
%      H = GUI01 returns the handle to a new GUI01 or the handle to
%      the existing singleton*.
%
%      GUI01('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in GUI01.M with the given input arguments.
%
%      GUI01('Property','Value',...) creates a new GUI01 or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before gui01_OpeningFunction gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to gui01_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Copyright 2002-2003 The MathWorks, Inc.

% Edit the above text to modify the response to help gui01

% Last Modified by GUIDE v2.5 07-Aug-2004 11:49:00

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @gui01_OpeningFcn, ...
                   'gui_OutputFcn',  @gui01_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before gui01 is made visible.
function gui01_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to gui01 (see VARARGIN)

% Choose default command line output for gui01
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes gui01 wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = gui01_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on slider movement.
function mySlider_Callback(hObject, eventdata, handles)
% hObject    handle to mySlider (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider

position = num2str(get(handles.mySlider, 'Value'));
set(handles.myText, 'String', position);

% --- Executes during object creation, after setting all properties.
function mySlider_CreateFcn(hObject, eventdata, handles)
% hObject    handle to mySlider (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background, change
%       'usewhitebg' to 0 to use default.  See ISPC and COMPUTER.
usewhitebg = 1;
if usewhitebg
    set(hObject,'BackgroundColor',[.9 .9 .9]);
else
    set(hObject,'BackgroundColor',get(0,'defaultUicontrolBackgroundColor'));
end



function myText_Callback(hObject, eventdata, handles)
% hObject    handle to myText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of myText as text
%        str2double(get(hObject,'String')) returns contents of myText as a double

value = str2double(get(handles.myText, 'String'));
set(handles.mySlider, 'Value', value);

% --- Executes during object creation, after setting all properties.
function myText_CreateFcn(hObject, eventdata, handles)
% hObject    handle to myText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc
    set(hObject,'BackgroundColor','white');
else
    set(hObject,'BackgroundColor',get(0,'defaultUicontrolBackgroundColor'));
end


