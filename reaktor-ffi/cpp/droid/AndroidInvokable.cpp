#include <droid/AndroidInvokable.h>
#include <common/Engine.h>

jni::global_ref<jni::JObject> instance;

struct JTester : public jni::JavaClass<JTester> {
    JAVA_DESCRIPTOR("Ldev/shibasis/flatinvoker/ffi/Tester;");

    static int sum() {
        auto method = instance->getClass()->getMethod<jlong(jbyteArray)>("invokeSync");
        flexbuffers::Builder builder;
        builder.Vector([&] {
            builder.String("FlexInvokable");
            builder.String("add");
            builder.Int(0);
            builder.Int(1);
            builder.Int(2);
        });
        builder.Finish();
        auto data = builder.GetBuffer();
        auto byteArray = jni::JArrayByte::newArray(data.size());
        byteArray->setRegion(0, data.size(), reinterpret_cast<const jbyte *>(data.data()));
        return method(instance, byteArray.release());
    }

    static int test(jni::alias_ref<JTester> self) {
        instance = jni::make_global(self);
        return sum();
    }

    static int testHermes(jni::alias_ref<JTester> self) {
        Engine::start();
        return 0;
    }

    static void registerNatives() {
        javaClassStatic()->registerNatives({
            makeNativeMethod("test", JTester::test),
            makeNativeMethod("testHermes", JTester::testHermes)
        });
    }
};

jint JNI_OnLoad(JavaVM *vm, void*) {
    return facebook::jni::initialize(vm, [] {
        JTester::registerNatives();
    });
}
