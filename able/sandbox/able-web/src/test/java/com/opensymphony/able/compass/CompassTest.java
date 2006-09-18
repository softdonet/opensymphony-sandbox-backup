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

import com.opensymphony.able.model.User;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.Resource;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * @version $Revision$
 */
public class CompassTest {

    protected Compass compass;

    @Test
    public void testCompassQuery() throws Exception {
        CompassTemplate template = new CompassTemplate(compass);

        final Class type = User.class;

        User answer = (User) template.execute(new CompassCallback() {
            public Object doInCompass(CompassSession session) {
                CompassHits hits = session.find("london");
                int count = hits.length();
                assertTrue(count > 0);
                Resource resource = hits.resource(0);
                String id = resource.getId();
                return session.load(type, id);
            }
        });
    }
}
