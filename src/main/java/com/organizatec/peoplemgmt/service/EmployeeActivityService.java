package com.organizatec.peoplemgmt.service;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.domain.EmployeeActivity;
import com.organizatec.peoplemgmt.repo.EmployeeActivityRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço para gerenciar a lógica de negócio das atividades dos funcionários.
 */
@Service
public class EmployeeActivityService {

    private final EmployeeActivityRepo activityRepo;
    private final EmployeeRepo employeeRepo;

    public EmployeeActivityService(EmployeeActivityRepo activityRepo, EmployeeRepo employeeRepo) {
        this.activityRepo = activityRepo;
        this.employeeRepo = employeeRepo;
    }

    /**
     * Lista todas as atividades de um funcionário específico, ordenadas pela data de início.
     *
     * @param employeeId O ID do funcionário.
     * @return Uma lista de {@link EmployeeActivity}.
     */
    @Transactional(readOnly = true)
    public List<EmployeeActivity> listByEmployee(Long employeeId) {
        return activityRepo.findByEmployeeIdOrderByStartedAtDesc(employeeId);
    }

    /**
     * Inicia uma nova atividade para um funcionário.
     *
     * @param employeeId  O ID do funcionário que está iniciando a atividade.
     * @param title       O título da atividade.
     * @param description A descrição detalhada da atividade.
     * @param startedAt   A data e hora de início. Se for nulo, usa o momento atual.
     * @return A entidade {@link EmployeeActivity} criada e salva.
     * @throws IllegalArgumentException se o funcionário não for encontrado.
     */
    @Transactional
    public EmployeeActivity startActivity(Long employeeId, String title, String description, LocalDateTime startedAt) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));
        EmployeeActivity a = new EmployeeActivity();
        a.setEmployee(emp);
        a.setTitle(title);
        a.setDescription(description);
        a.setStartedAt(startedAt != null ? startedAt : LocalDateTime.now());
        return activityRepo.save(a);
    }

    /**
     * Finaliza uma atividade que já foi iniciada.
     *
     * @param activityId O ID da atividade a ser finalizada.
     * @param finishedAt A data e hora de término. Se for nulo, usa o momento atual.
     * @return A entidade {@link EmployeeActivity} atualizada.
     * @throws IllegalArgumentException se a atividade não for encontrada.
     */
    @Transactional
    public EmployeeActivity finishActivity(Long activityId, LocalDateTime finishedAt) {
        EmployeeActivity a = activityRepo.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Atividade não encontrada: " + activityId));
        a.setFinishedAt(finishedAt != null ? finishedAt : LocalDateTime.now());
        return activityRepo.save(a);
    }

    /**
     * Exclui uma atividade do sistema.
     *
     * @param activityId O ID da atividade a ser excluída.
     */
    @Transactional
    public void deleteActivity(Long activityId) {
        activityRepo.deleteById(activityId);
    }
}