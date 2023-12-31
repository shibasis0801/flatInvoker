#pragma once

#include <jni.h>
#include <fbjni/fbjni.h>
#include <fbjni/ByteBuffer.h>
#include <flatbuffers/flexbuffers.h>
#include <core-cpp/base.h>

#define JAVA_DESCRIPTOR(fqcn) static auto constexpr kJavaDescriptor = fqcn;
namespace jni = facebook::jni;
