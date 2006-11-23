package com.opensymphony.able.demo.action;

import com.opensymphony.able.demo.model.Build;
import com.opensymphony.able.action.JpaCrudActionBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.action.*;

import java.io.StringReader;
import java.util.List;

/**
 * @autor Kjetil H.Paulsen
 * Date: 19.nov.2006
 */
public class CurrentBuildAction extends JpaCrudActionBean<Build> {

    public CurrentBuildAction() {
        super(Build.class);
    }

    List<Build> builds;

    @DefaultHandler
    public Resolution currentBuild() {
        String result = "";
        builds = getService().findAll();
        if (builds != null && !builds.isEmpty()) {
            result = builds.get(builds.size() -1).getTag();
        }
        return new StreamingResolution("text", new StringReader(result));
    }

    public Resolution buildHistory() {
        String result = "";
        builds = getService().findAll();
        for (Build build : builds) {
            result += build.getBuild() + "&nbsp;" + build.getDescription() + "<br>";
        }
        return new StreamingResolution("text", new StringReader(result));
    }
}

