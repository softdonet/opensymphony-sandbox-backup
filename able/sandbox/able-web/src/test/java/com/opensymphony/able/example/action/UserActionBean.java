package com.opensymphony.able.example.action;

import com.opensymphony.able.action.JpaCrudActionSupport;
import com.opensymphony.able.example.model.TestUser;
import com.opensymphony.able.test.MockEntityManager;
import org.springframework.orm.jpa.JpaTemplate;

/**
 * @version $Revision$
*/
public class UserActionBean extends JpaCrudActionSupport<TestUser> {

    public UserActionBean() {
        super(new JpaTemplate(new MockEntityManager()));
    }
}
