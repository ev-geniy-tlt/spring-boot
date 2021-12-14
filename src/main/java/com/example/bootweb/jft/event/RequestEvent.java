package com.example.bootweb.jft.event;

import com.example.bootweb.jft.Type;
import jdk.jfr.*;

@Name(Type.EVENT_NAME_PREFIX + "Request")
@Label("HTTP Request")
@Category({"HTTP", "Request"})
@Enabled
public class RequestEvent extends Event {
    @Label("HTTP method")
    public String method;
    @Label("HTTP URI")
    public String uri;
    @Label("HTTP Body Size")
    @DataAmount
    public long size;
}
