package org.davpen.transaction;

import org.hibernate.Session;

public interface ISessionManager {

    Session getSession();
}
