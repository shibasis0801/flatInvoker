cmake_minimum_required(VERSION 3.22.1)
project(flatInvoker)
set(CMAKE_CXX_STANDARD 20)

include_directories(.)

add_subdirectory(
        flatbuffers
        ${CMAKE_CURRENT_BINARY_DIR}/flatbuffers-build
)

file(GLOB runtime "rpc-runtime/*")
file(GLOB generator "rpc-generator/*")
file(GLOB core "core-cpp/*")

# This is the CLI to generate the data classes and interfaces
add_executable(
        flatrpc
        ${runtime}
        ${generator}
        ${core}
)
target_link_libraries(flatrpc flatbuffers)

# This is the runtime library that is added to targets
add_library(flatInvoker
        SHARED
        ${runtime}
        ${core}
)
target_link_libraries(flatInvoker
        flatbuffers)
target_include_directories(flatInvoker PUBLIC .)
