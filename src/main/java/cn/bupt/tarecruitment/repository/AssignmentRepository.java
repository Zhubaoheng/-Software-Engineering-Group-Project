package cn.bupt.tarecruitment.repository;

import cn.bupt.tarecruitment.model.AssignmentRecord;
import cn.bupt.tarecruitment.util.AppPaths;

import java.util.List;
import java.util.stream.Collectors;

public class AssignmentRepository extends JsonRepository<AssignmentRecord> {
    public AssignmentRepository() {
        super(AppPaths.ASSIGNMENTS_FILE, AssignmentRecord.class);
    }

    public List<AssignmentRecord> findAll() {
        return readAll();
    }

    public List<AssignmentRecord> findByApplicantId(String applicantId) {
        return findAll().stream()
                .filter(assignment -> applicantId != null && applicantId.equals(assignment.getApplicantId()))
                .collect(Collectors.toList());
    }

    public void save(AssignmentRecord record) {
        List<AssignmentRecord> assignments = new java.util.ArrayList<>(findAll());
        assignments.removeIf(item -> record.getAssignmentId().equals(item.getAssignmentId()));
        assignments.add(record);
        writeAll(assignments);
    }
}
