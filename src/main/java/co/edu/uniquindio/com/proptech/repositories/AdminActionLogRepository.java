package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.AdminActionLog;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import co.edu.uniquindio.com.proptech.structures.stack.Stack;

public interface AdminActionLogRepository {
    AdminActionLog save(AdminActionLog log);
    Queue<AdminActionLog> getHistory();
    boolean isEmpty();
    AdminActionLog pop();
    AdminActionLog peekUndo();
}