cmake_minimum_required(VERSION 3.22.1)
project(bifrost)
set(CMAKE_CXX_STANDARD 20)

include(FetchContent)
FetchContent_Declare(
        flatbuffers
        GIT_REPOSITORY https://github.com/google/flatbuffers.git
        GIT_TAG        master
)

FetchContent_GetProperties(flatbuffers)
if(NOT flatbuffers_POPULATED)
    FetchContent_Populate(flatbuffers)
    add_subdirectory(${flatbuffers_SOURCE_DIR} ${flatbuffers_BINARY_DIR})
endif()

add_executable(flatrpc rpc-generator/main.cpp
        core-cpp/base.h)
target_link_libraries(flatrpc flatbuffers)


add_library(bifrost rpc-runtime/library.cpp
        core-cpp/base.h)
target_link_libraries(bifrost flatbuffers)


