cmake_minimum_required(VERSION 3.22.1)
set(PROJECT_NAME "FlatInvokerReact")
project(${PROJECT_NAME})

set(NODE_MODULES "../../tester-react/node_modules")
set(REACT_NATIVE "${NODE_MODULES}/react-native")

include_directories(
        ${REACT_NATIVE}/React
        ${REACT_NATIVE}/React/Base
        ${REACT_NATIVE}/ReactCommon/jsi
        ${REACT_NATIVE}/ReactCommon/jsi/jsi
        ${REACT_NATIVE}/ReactAndroid/src/main/java/com/facebook/react/turbomodule/core/jni
        ${REACT_NATIVE}/ReactCommon/callinvoker
        .
)

add_subdirectory(
        "../../flatinvoker-core/cpp" # pathToDirectory
        ${CMAKE_CURRENT_BINARY_DIR}/flatInvoker-build # pathForBuildDirectory
)


set(CMAKE_VERBOSE_MAKEFILE ON)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

file(GLOB JNI_SRC "droid/*")
file(GLOB TYPE_SRC "types/*")
file(GLOB MODULE_SRC "modules/*")
file(GLOB NATIVE_SRC "native/*")
file(GLOB COMMON_SRC "*")
file(GLOB JNI_BINDINGS "droid/bindings/*")

add_compile_options(-Wall -Werror -fexceptions -frtti -fPIC)
add_library(
        ${PROJECT_NAME}
        SHARED
        ${JNI_BINDINGS}
        ${JNI_SRC}
        ${TYPE_SRC}
        ${MODULE_SRC}
        ${COMMON_SRC}
        ${NATIVE_SRC}
        ${REACT_NATIVE}/ReactCommon/jsi/jsi/jsi.cpp
        ${REACT_NATIVE}/ReactAndroid/src/main/java/com/facebook/react/turbomodule/core/jni/ReactCommon/CallInvokerHolder.cpp
)

set_target_properties(
        ${PROJECT_NAME} PROPERTIES
        CXX_STANDARD 20
        CXX_EXTENSIONS OFF
        POSITION_INDEPENDENT_CODE ON
)

find_library(
        log-lib
        log
)

find_package(fbjni REQUIRED CONFIG)

target_link_libraries(
        ${PROJECT_NAME}
        android
        flatbuffers
        fbjni::fbjni
        ${log-lib}
)