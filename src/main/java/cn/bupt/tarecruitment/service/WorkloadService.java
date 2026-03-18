package cn.bupt.tarecruitment.service;

import cn.bupt.tarecruitment.model.WorkloadRow;
import cn.bupt.tarecruitment.model.WorkloadSummary;

import java.util.List;

public interface WorkloadService {
    WorkloadSummary buildSummary();

    List<WorkloadRow> buildRows();
}
