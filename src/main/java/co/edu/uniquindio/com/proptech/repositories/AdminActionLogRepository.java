package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.AdminActionLog;
import co.edu.uniquindio.com.proptech.structures.stack.Stack;

public interface AdminActionLogRepository {

    AdminActionLog save(AdminActionLog log);

    Stack<AdminActionLog> getHistory();

    boolean isEmpty();
}