package com.opensymphony.able.example.action;

import com.opensymphony.able.example.model.User;
import com.opensymphony.able.action.JpaCrudActionSupport;
import com.opensymphony.able.test.MockEntityManager;
import org.springframework.orm.jpa.JpaTemplate;

/**
 * @version $Revision$
*/
public class UserActionBean extends JpaCrudActionSupport<User> {

    public UserActionBean() {
        super(new JpaTemplate(new MockEntityManager()));
    }
}
