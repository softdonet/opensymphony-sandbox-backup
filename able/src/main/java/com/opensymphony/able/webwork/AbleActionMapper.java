package com.opensymphony.able.webwork;

import com.opensymphony.util.TextUtils;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapper;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapping;
import com.opensymphony.webwork.dispatcher.mapper.DefaultActionMapper;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.RuntimeConfiguration;
import com.opensymphony.xwork.config.entities.ActionConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public class AbleActionMapper implements ActionMapper {
    public ActionMapping getMapping(HttpServletRequest request) {
        String path = request.getServletPath();

        if (path.contains(".") || "/".equals(path)) {
            return null;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        String[] parts = path.split("\\/");
        String prevPart = null;
        String name = "";
        for (String part : parts) {
            if (part.matches("-\\d+|\\d+")) {
                if (prevPart != null) {
                    params.put(prevPart + "Id", part);
                }
            } else {
                if (TextUtils.stringSet(part)) {
                    name = name + "/" + part;
                }
            }

            prevPart = part;
        }

        if (name.startsWith("/")) {
            name = name.substring(1);
        }

        // check for method names
        String method = null;
        if (name.contains("!")) {
            int bang = name.indexOf('!');
            method = name.substring(bang + 1);
            name = name.substring(0, bang);
        }

        ActionMapping mapping = new ActionMapping(name, "", method, params);

        DefaultActionMapper.handleSpecialParameters(request, mapping);


        // finally, make sure the request will yield an action
        RuntimeConfiguration rc = ConfigurationManager.getConfiguration().getRuntimeConfiguration();
        ActionConfig ac = rc.getActionConfig("", name);
        if (ac == null) {
            return null;
        }

        return mapping;
    }

    public String getUriFromActionMapping(ActionMapping mapping) {
        StringBuffer uri = new StringBuffer();

        String name = mapping.getName();
        String[] parts = name.split("\\/");
        Map params = mapping.getParams();
        if (params != null) {
            for (String part : parts) {
                uri.append("/").append(part);
                Object argVal = params.get(part + "Id");
                if (argVal != null) {
                    uri.append("/").append(argVal.toString());
                }
            }
        }

        return uri.toString();
    }
}
