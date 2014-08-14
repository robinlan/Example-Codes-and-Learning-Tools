function varargout = gui03(varargin)
% GUI03 M-file for gui03.fig
%      GUI03, by itself, creates a new GUI03 or raises the existing
%      singleton*.
%
%      H = GUI03 returns the handle to a new GUI03 or the handle to
%      the existing singleton*.
%
%      GUI03('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in GUI03.M with the given input arguments.
%
%      GUI03('Property','Value',...) creates a new GUI03 or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before gui03_OpeningFunction gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to gui03_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Copyright 2002-2003 The MathWorks, Inc.

% Edit the above text to modify the response to help gui03

% Last Modified by GUIDE v2.5 07-Aug-2004 13:27:22

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @gui03_OpeningFcn, ...
                   'gui_OutputFcn',  @gui03_OutputFcn, ...
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


% --- Executes just before gui03 is made visible.
function gui03_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to gui03 (see VARARGIN)

% Choose default command line output for gui03
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes gui03 wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = gui03_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on selection change in soundFile.
function soundFile_Callback(hObject, eventdata, handles)
% hObject    handle to soundFile (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = get(hObject,'String') returns soundFile contents as cell array
%        contents{get(hObject,'Value')} returns selected item from soundFile

value=get(hObject, 'value');
switch value
    case 1
        load chirp.mat
    case 2
        load handel.mat
    case 3
        load laughter.mat
end
plot((1:length(y))/Fs, y);
handles.y=y;
handles.Fs=Fs;
guidata(hObject, handles);

% --- Executes during object creation, after setting all properties.
function soundFile_CreateFcn(hObject, eventdata, handles)
% hObject    handle to soundFile (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc
    set(hObject,'BackgroundColor','white');
else
    set(hObject,'BackgroundColor',get(0,'defaultUicontrolBackgroundColor'));
end


% --- Executes on button press in closeButton.
function closeButton_Callback(hObject, eventdata, handles)
% hObject    handle to closeButton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

close(gcbf);

% --- Executes on button press in playButton.
function playButton_Callback(hObject, eventdata, handles)
% hObject    handle to playButton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

if isfield(handles, 'y')
    y=handles.y;
    Fs=handles.Fs;
    sound(y, Fs);
end


% --------------------------------------------------------------------
function one_Callback(hObject, eventdata, handles)
% hObject    handle to one (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


