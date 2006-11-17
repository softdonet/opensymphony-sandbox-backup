/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.compass;

import com.opensymphony.able.action.QueryStrategy;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An implementation of {@link QueryStrategy} which uses Compass to perform queries.
 *
 * @version $Revision$
 */
public class CompassQueryStrategy implements QueryStrategy {
    private CompassTemplate template;

    public CompassQueryStrategy(Compass compass) {
        this(new CompassTemplate(compass));
    }

    public CompassQueryStrategy(CompassTemplate template) {
        this.template = template;
    }

    public List execute(final Class type, final String query) {
        return (List) template.execute(new CompassCallback() {
            public Object doInCompass(CompassSession session) {
                CompassHits hits = session.find(query);
                int size = hits.length();
                if (size < 1) {
                    return Collections.emptyList();
                }
                Set ids = new HashSet(size);
                List answer = new ArrayList(size);
                for (int i = 0; i < size; i++) {
                    Resource resource = hits.resource(i);
                    String id = resource.getId();
                    if (!ids.contains(id)) {
                        ids.add(id);
                        Object value = session.load(type, id);
                        answer.add(value);
                    }
                }
                return answer;
            }
        });
    }
}
