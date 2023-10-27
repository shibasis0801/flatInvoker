#include <flatbuffers/flexbuffers.h>
#include <iostream>

int main() {
    // Build a FlexBuffer
    flexbuffers::Builder flexbuilder;
    flexbuilder.String("Hello, FlexBuffers!");
    flexbuilder.Finish();

    // Get the buffer
    auto buffer = flexbuilder.GetBuffer();

    // Read back from the buffer
    auto root = flexbuffers::GetRoot(buffer);
    auto message = root.AsString().str();

    // Print the message
    std::cout << message << std::endl;

    return 0;
}



/*
 {
    className: "",
    functionName: "",
    payload: "flatbuffer"
 }

*/