package com.nexus.service;

import com.nexus.entities.Log;
import com.nexus.repositories.ILogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    private final ILogRepository logRepository;

    @Autowired
    public LogService(ILogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * Get all logs
     * @return List of all logs
     */
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    /**
     * Get log by id
     * @param id Log id
     */
    public Optional<Log> getLogById(Long id) {
        return logRepository.findById(id);
    }

    /**
     * Create log
     * @param log Log
     * @return Log
     */
    public Log createLog(Log log) {
        return logRepository.save(log);
    }

    /**
     * Update log
     * @param id Log id
     * @param logDetails Log details
     * @return Log
     */
    public Optional<Log> updateLog(Long id, Log logDetails) {
        return logRepository.findById(id)
                .map(log -> {
                    log.setProject(logDetails.getProject());
                    log.setActivity(logDetails.getActivity());
                    return logRepository.save(log);
                });
    }

    /**
     * Delete log
     * @param id Log id
     */
    public void deleteLog(Long id) {
        logRepository.deleteById(id);
    }
}
