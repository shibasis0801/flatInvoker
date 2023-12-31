#pragma once

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif
    int reaktorTest();
    const char* getName();
    char* getNameCpp();
    // Used by kotlin to send byte array to cpp
    int sendByteArray(const uint8_t* bytes, int size);
#ifdef __cplusplus
}
#endif

// ExampleClass.h
#import <Foundation/Foundation.h>

@interface ExampleClass : NSObject
- (NSString *)getHelloWorld;
@end
