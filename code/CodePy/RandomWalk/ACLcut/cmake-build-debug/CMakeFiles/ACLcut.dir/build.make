# CMAKE generated file: DO NOT EDIT!
# Generated by "NMake Makefiles" Generator, CMake Version 3.12

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE
NULL=nul
!ENDIF
SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "E:\procedure\CLion 2018.2.4\bin\cmake\win\bin\cmake.exe"

# The command to remove a file.
RM = "E:\procedure\CLion 2018.2.4\bin\cmake\win\bin\cmake.exe" -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles\ACLcut.dir\depend.make

# Include the progress variables for this target.
include CMakeFiles\ACLcut.dir\progress.make

# Include the compile flags for this target's objects.
include CMakeFiles\ACLcut.dir\flags.make

CMakeFiles\ACLcut.dir\main.cpp.obj: CMakeFiles\ACLcut.dir\flags.make
CMakeFiles\ACLcut.dir\main.cpp.obj: ..\main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/ACLcut.dir/main.cpp.obj"
	C:\PROGRA~2\MICROS~1\2017\COMMUN~1\VC\Tools\MSVC\1415~1.267\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoCMakeFiles\ACLcut.dir\main.cpp.obj /FdCMakeFiles\ACLcut.dir\ /FS -c E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\main.cpp
<<

CMakeFiles\ACLcut.dir\main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/ACLcut.dir/main.cpp.i"
	C:\PROGRA~2\MICROS~1\2017\COMMUN~1\VC\Tools\MSVC\1415~1.267\bin\Hostx86\x86\cl.exe > CMakeFiles\ACLcut.dir\main.cpp.i @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\main.cpp
<<

CMakeFiles\ACLcut.dir\main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/ACLcut.dir/main.cpp.s"
	C:\PROGRA~2\MICROS~1\2017\COMMUN~1\VC\Tools\MSVC\1415~1.267\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoNUL /FAs /FaCMakeFiles\ACLcut.dir\main.cpp.s /c E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\main.cpp
<<

# Object files for target ACLcut
ACLcut_OBJECTS = \
"CMakeFiles\ACLcut.dir\main.cpp.obj"

# External object files for target ACLcut
ACLcut_EXTERNAL_OBJECTS =

ACLcut.exe: CMakeFiles\ACLcut.dir\main.cpp.obj
ACLcut.exe: CMakeFiles\ACLcut.dir\build.make
ACLcut.exe: CMakeFiles\ACLcut.dir\objects1.rsp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable ACLcut.exe"
	"E:\procedure\CLion 2018.2.4\bin\cmake\win\bin\cmake.exe" -E vs_link_exe --intdir=CMakeFiles\ACLcut.dir --manifests  -- C:\PROGRA~2\MICROS~1\2017\COMMUN~1\VC\Tools\MSVC\1415~1.267\bin\Hostx86\x86\link.exe /nologo @CMakeFiles\ACLcut.dir\objects1.rsp @<<
 /out:ACLcut.exe /implib:ACLcut.lib /pdb:E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\cmake-build-debug\ACLcut.pdb /version:0.0  /machine:X86 /debug /INCREMENTAL /subsystem:console kernel32.lib user32.lib gdi32.lib winspool.lib shell32.lib ole32.lib oleaut32.lib uuid.lib comdlg32.lib advapi32.lib 
<<

# Rule to build all files generated by this target.
CMakeFiles\ACLcut.dir\build: ACLcut.exe

.PHONY : CMakeFiles\ACLcut.dir\build

CMakeFiles\ACLcut.dir\clean:
	$(CMAKE_COMMAND) -P CMakeFiles\ACLcut.dir\cmake_clean.cmake
.PHONY : CMakeFiles\ACLcut.dir\clean

CMakeFiles\ACLcut.dir\depend:
	$(CMAKE_COMMAND) -E cmake_depends "NMake Makefiles" E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\cmake-build-debug E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\cmake-build-debug E:\Code\CommunityDetection\code\CodePy\RandomWalk\ACLcut\cmake-build-debug\CMakeFiles\ACLcut.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles\ACLcut.dir\depend

