/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.jaxb;

import net.sourceforge.stripes.action.Resolution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * A {@link Resolution} for rendering the entities using JAXB
 * <p/>
 * version $Revision: 1 $
 */
public class JaxbResolution implements Resolution {

    private JaxbTemplate template;
    private List list;
    private Object element;
    private String contentType = "application/xml";

    public JaxbResolution(JaxbTemplate template, Object element) {
        this.template = template;
        this.element = element;
    }

    public JaxbResolution(JaxbTemplate template, List list) {
        this.template = template;
        this.list = list;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(contentType);
        if (element != null) {
            template.writeElement(response.getOutputStream(), element);
        }
        else {
            template.write(response.getOutputStream(), list);
        }
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
