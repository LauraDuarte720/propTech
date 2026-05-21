package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.AdminActionLog;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.AdminActionLogRepository;
import co.edu.uniquindio.com.proptech.structures.stack.Stack;
import org.springframework.stereotype.Repository;

@Repository
public class AdminActionLogRepositoryImpl implements AdminActionLogRepository {

    private final PropTech propTech;

    public AdminActionLogRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public AdminActionLog save(AdminActionLog log) {
        propTech.getAdminHistory().push(log);
        return log;
    }

    @Override
    public Stack<AdminActionLog> getHistory() {
        return propTech.getAdminHistory();
    }


    @Override
    public boolean isEmpty() {
        return propTech.getAdminHistory().isEmpty();
    }

    @Override
    public AdminActionLog pop() {
        return propTech.getAdminHistory().pop();
    }
}