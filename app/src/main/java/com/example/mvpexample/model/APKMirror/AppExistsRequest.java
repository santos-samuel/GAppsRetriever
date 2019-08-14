package com.example.mvpexample.model.APKMirror;

import java.util.ArrayList;
import java.util.List;

public class AppExistsRequest
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<String> pnames;
    public List<String> exclude;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AppExistsRequest(
            List<String> pnames,
            List<String> exclude
    ) {
        this.pnames = pnames;
        this.exclude = exclude;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static List<String> excludeExperimental(
    ) {
        List<String> e = new ArrayList<>();
        e.add("alpha");
        e.add("beta");
        e.add("rc");
        e.add("test");
        e.add("other");
        return e;
    }
}
