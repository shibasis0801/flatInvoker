#pragma once

#include <stdint.h>
#include <stdio.h>


// Every Language exposes a channel to receive input
struct PlatformChannel {
    virtual ~PlatformChannel() = default;
    virtual void send(const uint8_t *data, size_t count) = 0;
};
